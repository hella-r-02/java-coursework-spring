package main.repository;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import main.entity.ArticleEntity;
import main.entity.BalanceEntity;
import main.entity.OperationEntity;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {
    private static final String articleName = "article_for_test";
    private static final String deleteArticleName = "delete_article";
    private static final String updateArticleName = "update_article";

    @Autowired
    private ArticleRepository repository;
    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(repository).isNotNull();
        assertThat(entityManager).isNotNull();
    }
    @DirtiesContext
    @Test
    public void should_find_by_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        assertThat(repository.findById(article.getId()).get()).isEqualTo(article);
    }
    @DirtiesContext
    @Test
    public void should_find_by_name() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        assertThat(repository.findByName(articleName)).isNotNull();
    }
    @DirtiesContext
    @Test
    public void should_find_by_operation_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(article).isEqualTo(repository.findByOperationId(operation.getId()).get());
    }
    @DirtiesContext
    @Test
    public void should_delete_by_name() {
        ArticleEntity article = new ArticleEntity(deleteArticleName);
        entityManager.persist(article);
        repository.deleteByName(deleteArticleName);
        assertThat(repository.findById(article.getId())).isEmpty();
    }
    @DirtiesContext
    @Test
    public void should_add_new_article() {
        repository.addNewArticle("article");
        assertThat(repository.findByName("article")).isNotEmpty();
    }

    @DirtiesContext
    @Test
    public void should_update_name() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        repository.updateArticle(article.getId(), updateArticleName);
        entityManager.refresh(article);
        assertThat(repository.findByName(updateArticleName).get(0)).isEqualTo(article);
    }
}
