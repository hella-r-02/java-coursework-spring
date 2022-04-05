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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import main.dto.OperationDTO;
import main.entity.Article;
import main.entity.User;
import main.utils.HttpHeadersUtils;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    User user;
    @Autowired
    HttpHeadersUtils httpHeadersUtils;

    @RequestMapping
    public String getArticles(Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        Article[] list = restTemplate.exchange("http://localhost:8080/article", HttpMethod.GET, entity, Article[].class).getBody();
        Article[] articlesForEach = restTemplate.exchange("http://localhost:8080/article/find", HttpMethod.GET, entity, Article[].class).getBody();
        model.addAttribute("token", user.getToken());
        model.addAttribute("articles", list);
        model.addAttribute("articles_for_search", articlesForEach);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(model);
        return "article/articles";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getArticleById(@PathVariable Long id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            Article article = restTemplate.exchange("http://localhost:8080/article/" + id, HttpMethod.GET, entity, Article.class).getBody();
            OperationDTO[] operationDTOList = restTemplate.exchange("http://localhost:8080/operation/find/article_id/" + id, HttpMethod.GET, entity, OperationDTO[].class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("article", article);
            model.addAttribute("operations", operationDTOList);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "article/article-id";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    public String deleteArticle(@PathVariable("id") int id, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/article/remove/" + id, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/article/";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editArticle(@PathVariable("id") Long id, Model model) {

        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            Article article = restTemplate.exchange("http://localhost:8080/article/" + id, HttpMethod.GET, entity, Article.class).getBody();
            model.addAttribute("token", user.getToken());
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
        return "article/article-edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editArticlePost(@PathVariable("id") Long id, @RequestBody String name, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(name, httpHeaders);
        try {
            restTemplate.postForEntity("http://localhost:8080/article/edit/" + id, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/article/" + id;
    }

    @GetMapping("/add")
    public String addArticle() {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        return "article/article-add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPostArticle(@RequestBody String name, Model model) {

        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(name, httpHeaders);
        try {
            restTemplate.postForEntity("http://localhost:8080/article/add", entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/article";
    }

    @RequestMapping(value = "/find/name", method = RequestMethod.POST)
    public String findArticleByNamePost(@RequestParam("articles") String name, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (name != null) {
            map.add("articleName", name);
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
        try {
            Article[] list = restTemplate.postForEntity("http://localhost:8080/article/find/name", entity, Article[].class).getBody();
            model.addAttribute("token", user.getToken());
            model.addAttribute("articles", list);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "article/article-find";
    }

    @RequestMapping(value = "{name}/remove_name", method = RequestMethod.POST)
    public String deleteArticleByNamePost(@PathVariable String name, Model model) {
        if (user.checkValidToken()) {
            user.setToken(null);
            return "invalid-token";
        }
        HttpHeaders httpHeaders = httpHeadersUtils.createHeaders(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            restTemplate.exchange("http://localhost:8080/article/" + name + "/remove_name", HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException exception) {
            model.addAttribute("error", exception.getStatusCode().getReasonPhrase());
            model.addAttribute("code", exception.getStatusCode().value());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject(model);
            return "error";
        }
        return "redirect:/article";
    }
}
