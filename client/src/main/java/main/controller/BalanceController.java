package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import main.dto.BalanceDTO;
import main.dto.OperationDTO;
import main.entity.Balance;
import main.entity.User;
import main.utils.HttpHeadersUtils;

@Controller
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    User user;

    @Autowired
    HttpHeadersUtils httpHeadersUtils;

    @RequestMapping
    public String getBalance(Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }

        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        BalanceDTO[] list = restTemplate.exchange("http://localhost:8080/balance", HttpMethod.GET, entity, BalanceDTO[].class).getBody();
        model.addAttribute("token", user.getToken());
        model.addAttribute("balance", list);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(model);
        return "balance/balance";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getBalanceById(@PathVariable Long id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            BalanceDTO balanceDTO = restTemplate.exchange("http://localhost:8080/balance/dto/" + id, HttpMethod.GET, entity, BalanceDTO.class).getBody();
            OperationDTO[] operationDTOList = restTemplate.exchange("http://localhost:8080/operation/find/balance_id/" + id, HttpMethod.GET, entity, OperationDTO[].class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("operations", operationDTOList);
            model.addAttribute("balance", balanceDTO);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "balance/balance-id";
    }

    @GetMapping("/add")
    public String addBalance() {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        return "balance/balance-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBalancePost(@RequestParam("debit") Double debit, @RequestParam("credit") Double credit, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (debit != null && credit != null) {
            map.add("debit", debit.toString());
            map.add("credit", credit.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                restTemplate.postForEntity("http://localhost:8080/balance/add", entity, String.class);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "redirect:/balance";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editBalance(@PathVariable("id") Long id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            Balance balance = restTemplate.exchange("http://localhost:8080/balance/" + id, HttpMethod.GET, entity, Balance.class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("balance", balance);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "balance/balance-edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editBalancePost(@PathVariable("id") Long id, @RequestParam("debit") Double debit, @RequestParam("credit") Double credit, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (debit != null && credit != null) {
            map.add("debit", debit.toString());
            map.add("credit", credit.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                restTemplate.postForEntity("http://localhost:8080/balance/edit/" + id, entity, String.class);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "redirect:/balance/" + id;
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String deleteBalance(@PathVariable("id") int id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/balance/remove/" + id, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/balance/";
    }

    @RequestMapping(value = "/find", method = {RequestMethod.GET, RequestMethod.POST})
    public String findBalanceByDebitCredit(@RequestParam("debit") Double debit, @RequestParam("credit") Double credit, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (debit != null && credit != null) {
            map.add("debit", debit.toString());
            map.add("credit", credit.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                BalanceDTO[] list = restTemplate.postForEntity("http://localhost:8080/balance/find", entity, BalanceDTO[].class).getBody();
                model.addAttribute("token", user.getToken());
                model.addAttribute("balance", list);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "balance/balance-find";
        } else {
            return "redirect:/error";
        }
    }
}
