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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import main.dto.BalanceDTO;
import main.dto.OperationDTO;
import main.entity.Article;
import main.entity.Operation;
import main.entity.User;
import main.utils.HttpHeadersUtils;

@Controller
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    User user;
    @Autowired
    HttpHeadersUtils httpHeadersUtils;

    @RequestMapping
    public String getOperation(Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        model.addAttribute("token", user.getToken());
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        OperationDTO[] list = restTemplate.exchange("http://localhost:8080/operation", HttpMethod.GET, entity, OperationDTO[].class).getBody();
        Article[] articles = restTemplate.exchange("http://localhost:8080/article/find", HttpMethod.GET, entity, Article[].class).getBody();
        model.addAttribute("articles", articles);
        model.addAttribute("operations", list);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(model);
        return "operation/operations";

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getOperationById(@PathVariable Long id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        model.addAttribute("token", user.getToken());
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            OperationDTO operationDTO = restTemplate.exchange("http://localhost:8080/operation/dto/" + id, HttpMethod.GET, entity, OperationDTO.class).getBody();
            BalanceDTO balanceDTO = restTemplate.exchange("http://localhost:8080/balance/find/operation_id/" + id, HttpMethod.GET, entity, BalanceDTO.class).getBody();
            Article article = restTemplate.exchange("http://localhost:8080/article/find/operation_id/" + id, HttpMethod.GET, entity, Article.class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("operation", operationDTO);
            model.addAttribute("balance", balanceDTO);
            model.addAttribute("article", article);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "operation/operation-id";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editOperation(@PathVariable("id") Long id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            Operation operation = restTemplate.exchange("http://localhost:8080/operation/" + id, HttpMethod.GET, entity, Operation.class).getBody();
            BalanceDTO[] balanceDTOList = restTemplate.exchange("http://localhost:8080/balance", HttpMethod.GET, entity, BalanceDTO[].class).getBody();
            Article[] articleList = restTemplate.exchange("http://localhost:8080/article", HttpMethod.GET, entity, Article[].class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("operation", operation);
            model.addAttribute("articles", articleList);
            model.addAttribute("balance", balanceDTOList);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "operation/operation-edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editOperationPost(@PathVariable("id") Long id, @RequestParam("article_id") Long article_id, @RequestParam("balance_id") Long balance_id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (article_id != null && balance_id != null) {
            map.add("article_id", article_id.toString());
            map.add("balance_id", balance_id.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                restTemplate.postForEntity("http://localhost:8080/operation/edit/" + id, entity, String.class);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "redirect:/operation/" + id;
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addOperation(Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            BalanceDTO[] balanceDTOList = restTemplate.exchange("http://localhost:8080/balance", HttpMethod.GET, entity, BalanceDTO[].class).getBody();
            Article[] articleList = restTemplate.exchange("http://localhost:8080/article", HttpMethod.GET, entity, Article[].class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("articles", articleList);
            model.addAttribute("balance", balanceDTOList);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "operation/operation-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPostOperation(@RequestParam("article_id") Long article_id, @RequestParam("balance_id") Long balance_id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (article_id != null && balance_id != null) {
            map.add("article_id", article_id.toString());
            map.add("balance_id", balance_id.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                restTemplate.postForEntity("http://localhost:8080/operation/add", entity, String.class);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "redirect:/operation/";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String deleteOperation(@PathVariable("id") int id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/operation/remove/" + id, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/operation/";

    }

    @RequestMapping(value = "/remove/article_id/{id}", method = RequestMethod.POST)
    public String deleteOperationByArticleId(@PathVariable("id") int id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/operation/remove/article_id/" + id, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/article/" + id;
    }

    @RequestMapping(value = "/remove/balance_id/{id}", method = RequestMethod.POST)
    public String deleteOperationByBalanceId(@PathVariable("id") int id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/operation/remove/balance_id/" + id, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/balance/" + id;
    }

    @RequestMapping(value = "/find/lower", method = {RequestMethod.POST, RequestMethod.GET})
    public String findOperationByLowerMargin(@RequestParam("amount_lower") Double amount, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (amount != null) {
            map.add("amount", amount.toString());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
            try {
                OperationDTO[] list = restTemplate.postForEntity("http://localhost:8080/operation/find/lower", entity, OperationDTO[].class).getBody();
                model.addAttribute("token", user.getToken());
                model.addAttribute("operations", list);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "operation/operation-find";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/find/upper", method = {RequestMethod.POST, RequestMethod.GET})
    public String findOperationByUpperMargin(@RequestParam("amount_upper") Double amount, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (amount != null) {
            map.add("amount", amount.toString());
            try {
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
                OperationDTO[] list = restTemplate.postForEntity("http://localhost:8080/operation/find/upper", entity, OperationDTO[].class).getBody();
                model.addAttribute("token", user.getToken());
                model.addAttribute("operations", list);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "operation/operation-find";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(value = "/find/name", method = {RequestMethod.POST, RequestMethod.GET})
    public String findOperationByArticleName(@RequestParam("name") String name, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (name != null) {
            map.add("name", name);
            try {
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
                OperationDTO[] list = restTemplate.postForEntity("http://localhost:8080/operation/find/name", entity, OperationDTO[].class).getBody();
                model.addAttribute("token", user.getToken());
                model.addAttribute("operations", list);
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
            } catch (HttpClientErrorException exception) {
                model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
                model.addAttribute("code", exception.getStatusCode().value());
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(model);
                return "error";
            }
            return "operation/operation-find";
        } else {
            return "redirect:/error";
        }
    }
}
