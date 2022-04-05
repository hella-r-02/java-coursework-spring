package main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.entity.OperationEntity;

@Repository
public interface OperationRepository extends CrudRepository<OperationEntity, Long> {
    Optional<OperationEntity> findById(Long id);

    @Query(value = "Select * from operations " +
            "join balance bal on operations.balance_id = bal.ID " +
            "where  balance_id in (select balance_id from balance where bal.amount<=:margin) ", nativeQuery = true)
    List<OperationEntity> findWithUpperMarginOfAmount(@Param("margin") Double amount);

    @Query(value = "Select * from operations " +
            "join balance bal on operations.balance_id = bal.ID " +
            "where  balance_id in (select balance_id from balance where bal.amount>=:margin) ", nativeQuery = true)
    List<OperationEntity> findWithLowerMarginOfAmount(@Param("margin") Double amount);

    @Query(value = "Select * from operations " +
            "join articles ar on operations.article_id = ar.ID " +
            "where  article_id in (select article_id from articles where ar.name=:name) ", nativeQuery = true)
    List<OperationEntity> findByArticleName(@Param("name") String name);

    List<OperationEntity> findByArticleId(@Param("id") Long id);

    List<OperationEntity> findByBalanceId(@Param("id") Long id);

    @Modifying
    @Transactional
    void deleteByArticleId(Long id);

    @Modifying
    @Transactional
    void deleteByBalanceId(Long id);

    @Modifying
    @Transactional
    @Query(value = "insert into operations (article_id, debit, credit, create_date,balance_id) " +
            "values (:article_id,:debit,:credit,:create_date,:balance_id)", nativeQuery = true)
    void addNewOperation(@Param("article_id") Long article_id, @Param("debit") Double debit, @Param("credit") Double credit,
                         @Param("create_date") LocalDateTime create_date, @Param("balance_id") Long balance_id);

    @Modifying
    @Transactional
    @Query(value = "update operations set article_id=:article_id, debit=:debit, credit=:credit,  balance_id=:balance_id " +
            "where operations.id=:id", nativeQuery = true)
    void updateOperation(@Param("id") Long id, @Param("article_id") Long article_id, @Param("debit") Double debit,
                         @Param("credit") Double credit, @Param("balance_id") Long balance_id);
}

