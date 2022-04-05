package main.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import main.entity.User;
import main.web.AuthRequest;
import main.web.SignUpRequest;


@Controller
public class MainController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    User user;

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/signin")
    private String signIn() {
        return "main/signIn";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signInPost(@RequestParam("name") String userName, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        if (userName.isEmpty() || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("isFailAuth", true);
            return "redirect:/signin";
        }
        HttpHeaders headersForEdit = new HttpHeaders();
        headersForEdit.setContentType(MediaType.APPLICATION_JSON);
        AuthRequest authRequest = new AuthRequest(userName, password);
        HttpEntity<AuthRequest> entityForEdit = new HttpEntity<>(authRequest, headersForEdit);
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/signin", entityForEdit, Map.class);
            String token = (String) responseEntity.getBody().get("token");
            Integer timeout = (Integer) responseEntity.getBody().get("timeout");
            Long createDate = (Long) responseEntity.getBody().get("createDate");
            user.setToken(token);
            user.setUserName(userName);
            user.setTimeout(timeout);
            user.setCreateDateToken(createDate);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("isFailAuth", true);
            return "redirect:/signin";
        }
        return "redirect:/operation";
    }

    @GetMapping("/signup")
    private String signUp() {
        return "main/signUp";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUpPost(@RequestParam("name") String userName, @RequestParam("password") String password, @ModelAttribute("adminRole") String adminRole, RedirectAttributes redirectAttributes) {
        if (userName.isEmpty() || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("isFailAuth", true);
            return "redirect:/signin";
        }
        HttpHeaders headersForEdit = new HttpHeaders();
        headersForEdit.setContentType(MediaType.APPLICATION_JSON);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        if (!adminRole.isEmpty()) {
            roles.add("ROLE_ADMIN");
        }
        SignUpRequest authRequest = new SignUpRequest(userName, password, roles);
        HttpEntity<SignUpRequest> entityForEdit = new HttpEntity<>(authRequest, headersForEdit);
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/signup", entityForEdit, Map.class);
            String token = (String) responseEntity.getBody().get("token");
            Integer timeout = (Integer) responseEntity.getBody().get("timeout");
            Long createDate = (Long) responseEntity.getBody().get("createDate");
            user.setToken(token);
            user.setUserName(userName);
            user.setCreateDateToken(new Date().getTime());
            user.setTimeout(timeout);
            user.setCreateDateToken(createDate);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("isFailSignUp", true);
            return "redirect:/signup";
        }
        return "redirect:/operation";
    }

    @RequestMapping(value = "/signout")
    public String logOut() {
        if (user.getToken() != null) {
            user.setToken(null);
        }
        return "redirect:/operation";
    }

    @GetMapping(value = "/personal-account")
    public String personalAccount(Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        model.addAttribute("token", user.getToken());
        model.addAttribute("username", user.getUserName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(model);
        return "main/personal-account";
    }

    @RequestMapping(value = "/valid_token", method = RequestMethod.GET)
    public String getValidToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user.getUserName());
        HttpEntity<MultiValueMap<String, String>> entityForEdit = new HttpEntity<>(map, httpHeaders);
        String token = restTemplate.postForEntity("http://localhost:8080/auth/valid_token", entityForEdit, String.class).getBody();
        user.setToken(token);
        user.setCreateDateToken(new Date().getTime());
        return "redirect:/operation";
    }
}
