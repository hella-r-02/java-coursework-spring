package main.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.entity.ArticleEntity;
import main.exception.ArticleNotFoundException;
import main.exception.NameIsEmptyException;
import main.repository.ArticleRepository;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<ArticleEntity> findAllArticles() {
        return (List<ArticleEntity>) articleRepository.findAll();
    }

    @Override
    public List<ArticleEntity> findAllAUniqueArticles() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(ArticleEntity::getName, article -> article, (article, temp) -> article))
                .values().stream().collect(Collectors.toList());
    }

    @Override
    public List<ArticleEntity> findByName(String name) {
        if (!name.isEmpty()) {
            return articleRepository.findByName(name);
        } else {
            throw new NameIsEmptyException("name is empty");
        }
    }

    @Override
    public ArticleEntity findByOperationId(Long id) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findByOperationId(id);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new ArticleNotFoundException("article not found");
        }
    }

    @Override
    public void deleteByName(String name) {
        if (!name.isEmpty()) {
            List<ArticleEntity> optionalArticle = articleRepository.findByName(name);
            if (!optionalArticle.isEmpty()) {
                articleRepository.deleteByName(name);
            }
        } else {
            throw new NameIsEmptyException("name is empty");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            articleRepository.deleteById(id);
        } else {
            throw new ArticleNotFoundException("article not found");
        }
    }

    @Override
    public void updateArticle(Long id, String name) {
        if (!name.isEmpty()) {
            Optional<ArticleEntity> optionalArticle = articleRepository.findById(id);
            if (optionalArticle.isPresent()) {
                articleRepository.updateArticle(id, name);
            } else {
                throw new ArticleNotFoundException("article not found");
            }
        } else {
            throw new NameIsEmptyException("name is empty");
        }
    }

    @Override
    public void addArticleByName(String name) {
        if (!name.isEmpty()) {
            articleRepository.addNewArticle(name);
        } else {
            throw new NameIsEmptyException("name is empty");
        }
    }

    @Override
    public ArticleEntity findById(Long id) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new ArticleNotFoundException("article not found");
        }
    }
}
