package main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import main.entity.ArticleEntity;
import main.exception.ArticleNotFoundException;
import main.exception.NameIsEmptyException;
import main.exception.OperationNotFoundException;
import main.service.ArticleService;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping()
    public ResponseEntity<List<ArticleEntity>> getArticles() {
        List<ArticleEntity> list = articleService.findAllArticles();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ArticleEntity>> getUniqueArticles() {
        return new ResponseEntity<>(articleService.findAllAUniqueArticles(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ArticleEntity> getArticleById(@PathVariable("id") Long id) {
        try {
            ArticleEntity articleEntity = articleService.findById(id);
            return new ResponseEntity<>(articleEntity, HttpStatus.OK);
        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }

    @PostMapping(value = "/remove/{id}")
    public ResponseEntity<ArticleEntity> removeArticle(@PathVariable("id") Long id) {
        try {
            articleService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }

    @PostMapping(value = "/{name}/remove_name")
    public ResponseEntity<ArticleEntity> removeByName(@PathVariable String name) {
        try {
            articleService.deleteByName(name);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        } catch (NameIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is empty");
        }
    }

    @PostMapping(value = "/edit/{id}")
    public ResponseEntity<ArticleEntity> editArticle(@PathVariable("id") Long id, @RequestParam String name) {
        try {
            articleService.updateArticle(id, name);
            return new ResponseEntity<>(articleService.findById(id), HttpStatus.OK);
        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        } catch (NameIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is empty");
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<List<ArticleEntity>> addArticle(@RequestParam String name) {
        try {
            articleService.addArticleByName(name);
            return new ResponseEntity<>(articleService.findAllArticles(), HttpStatus.OK);
        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }

    @PostMapping(value = "/find/name")
    public ResponseEntity<List<ArticleEntity>> findByName(@RequestParam String articleName) {
        try {
            List<ArticleEntity> list = articleService.findByName(articleName);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        } catch (NameIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is empty");
        }
    }

    @GetMapping(value = "/find/operation_id/{id}")
    public ResponseEntity<ArticleEntity> findArticleByOperationId(@PathVariable("id") Long id) {
        try {
            ArticleEntity article = articleService.findByOperationId(id);
            return new ResponseEntity<>(article, HttpStatus.OK);
        } catch (OperationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }
}
