package main.service;

import java.util.List;

import main.dto.BalanceDTO;
import main.entity.BalanceEntity;

public interface BalanceService {
    List<BalanceDTO> findAllBalance();

    BalanceEntity findById(Long id);

    BalanceDTO findByIdTODTO(Long id);

    BalanceDTO findByOperationIdDTO(Long id);

    void deleteById(Long id);

    void addNewBalance(Double debit, Double credit);

    void updateBalance(Long id, Double debit, Double credit);

    List<BalanceDTO> findByDebitCredit(Double debit, Double credit);

}
