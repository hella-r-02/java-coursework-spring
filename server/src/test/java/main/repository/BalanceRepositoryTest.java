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
public class BalanceRepositoryTest {

    private static final String articleName = "article_for_test";

    @Autowired
    private BalanceRepository repository;
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
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, 0.0);
        entityManager.persist(balance);
        assertThat(repository.findById(balance.getId()).get()).isEqualTo(balance);
    }

    @DirtiesContext
    @Test
    public void should_find_by_debit_credit() {
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 300.0, 100.0, 200.0);
        entityManager.persist(balance);
        assertThat(repository.findByDebitCredit(balance.getDebit(), balance.getCredit()).get(0)).isEqualTo(balance);
    }

    @DirtiesContext
    @Test
    public void should_find_by_operation_id() {
        ArticleEntity article = new ArticleEntity(articleName);
        entityManager.persist(article);
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, 0.0);
        entityManager.persist(balance);
        OperationEntity operation = new OperationEntity(article, 100.0, 100.0, LocalDateTime.now(), balance);
        entityManager.persist(operation);
        assertThat(balance).isEqualTo(repository.findByOperationId(operation.getId()).get());
    }

    @DirtiesContext
    @Test
    public void should_add_new_balance() {
        repository.addNewBalance(LocalDateTime.now(), 100.0, 100.0, 0.0);
        assertThat(repository.findByDebitCredit(100.0, 100.0).get(0)).isNotNull();
    }

    @DirtiesContext
    @Test
    public void should_update_balance() {
        BalanceEntity balance = new BalanceEntity(LocalDateTime.now(), 100.0, 100.0, 0.0);
        entityManager.persist(balance);
        repository.updateBalance(balance.getId(), 200.0, 100.0, 100.0);
        entityManager.refresh(balance);
        assertThat(repository.findById(balance.getId()).get().getDebit()).isEqualTo(200.0);
    }
}

