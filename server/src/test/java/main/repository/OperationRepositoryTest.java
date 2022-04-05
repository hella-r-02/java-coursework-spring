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
public class OperationRepositoryTest {
    private static final String articleName = "article_for_test";

    @Autowired
    private OperationRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void injectedComponentsAreNotNull() {
        assertThat(repository).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    public void should_find_by_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findById(operation.getId()).get()).isEqualTo(operation);
    }

    @Test
    public void should_find_with_upper_margin_of_amount() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findWithUpperMarginOfAmount(100.0)).isNotEmpty();
    }

    @Test
    public void should_find_with_lower_margin_of_amount() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findWithLowerMarginOfAmount(0.0)).isNotEmpty();
    }

    @Test
    public void should_find_by_article_name() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findByArticleName(articleName)).isNotEmpty();
    }

    @Test
    public void should_find_by_article_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findByArticleId(article.getId()).get(0)).isEqualTo(operation);
    }

    @Test
    public void should_find_by_balance_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(repository.findByBalanceId(balance.getId()).get(0)).isEqualTo(operation);
    }

    @Test
    public void should_delete_by_article_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        repository.deleteByArticleId(article.getId());
        assertThat(repository.findById(operation.getId())).isEmpty();
    }

    @Test
    public void should_delete_by_balance_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        repository.deleteByBalanceId(balance.getId());
        assertThat(repository.findById(operation.getId())).isEmpty();
    }

    @Test
    @DirtiesContext
    public void should_add_new_operation() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        repository.addNewOperation(article.getId(), balance.getDebit(), balance.getCredit(), balance.get_create_date(), balance.getId());
        assertThat(repository.findByArticleId(article.getId()).get(0).getArticle()).isEqualTo(article);
    }

    @Test
    @DirtiesContext
    public void should_update_operation() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, (double) 0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        ArticleEntity article2 = new ArticleEntity("article_for_operation");
        entityManager.persist(article2);
        repository.updateOperation(operation.getId(), article2.getId(), balance.getDebit(), balance.getCredit(), balance.getId());
        entityManager.refresh(operation);
        assertThat(operation.getArticle()).isEqualTo(article2);
    }
}

