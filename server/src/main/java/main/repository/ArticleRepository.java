package main.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.entity.ArticleEntity;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {
    List<ArticleEntity> findByName(String name);

    @Query(value = "select * from articles " +
            "join operations op on op.article_id=articles.id " +
            "where op.id=:id", nativeQuery = true)
    Optional<ArticleEntity> findByOperationId(@Param("id") Long id);

    @Modifying
    @Transactional
    void deleteByName(String name);

    @Modifying
    @Transactional
    @Query(value = "update articles  set name=:name where articles.id=:id", nativeQuery = true)
    int updateArticle(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "insert into articles (name) values (:name)", nativeQuery = true)
    void addNewArticle(@Param("name") String name);

    Optional<ArticleEntity> findById(Long id);

}
