package main.service;

import java.util.List;

import main.entity.ArticleEntity;

public interface ArticleService {
    List<ArticleEntity> findAllArticles();

    List<ArticleEntity> findAllAUniqueArticles();

    List<ArticleEntity> findByName(String name);

    ArticleEntity findByOperationId(Long id);

    void deleteByName(String name);

    void deleteById(Long id);

    void updateArticle(Long id,String name);

    void addArticleByName(String name);

    ArticleEntity findById(Long id);
}
