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

import main.entity.BalanceEntity;

@Repository
public interface BalanceRepository extends CrudRepository<BalanceEntity, Long> {
    Optional<BalanceEntity> findById(Long id);

    @Query(value = "select * from balance " +
            "join operations op on op.balance_id=balance.id " +
            "where op.id=:id", nativeQuery = true)
    Optional<BalanceEntity> findByOperationId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "insert into balance (create_date, debit, credit, amount) " +
            "values (:create_date,:debit,:credit,:amount)", nativeQuery = true)
    void addNewBalance(@Param("create_date") LocalDateTime create_date, @Param("debit") Double debit, @Param("credit") Double credit, @Param("amount") Double amount);

    @Modifying
    @Transactional
    @Query(value = "update balance set debit=:debit, credit=:credit, amount=:amount " +
            "where balance.id=:id", nativeQuery = true)
    void updateBalance(@Param("id") Long id, @Param("debit") Double debit, @Param("credit") Double credit, @Param("amount") Double amount);

    @Query(value = "select * from balance " +
            "where debit=:debit and credit=:credit", nativeQuery = true)
    List<BalanceEntity> findByDebitCredit(@Param("debit") Double debit, @Param("credit") Double credit);

}

