package main.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dto.BalanceDTO;
import main.entity.BalanceEntity;
import main.exception.BalanceNotFoundException;
import main.exception.CreditIsEmptyException;
import main.exception.CreditNotPositive;
import main.exception.DebitIsEmptyException;
import main.exception.DebitNotPositive;
import main.repository.BalanceRepository;
import main.utils.MappingUtilsBalance;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private MappingUtilsBalance mappingUtilsBalance;

    @Override
    public List<BalanceDTO> findAllBalance() {
        return StreamSupport.stream(balanceRepository.findAll().spliterator(), false).map(mappingUtilsBalance::mapToBalanceDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public BalanceEntity findById(Long id) {
        Optional<BalanceEntity> optionalBalance = balanceRepository.findById(id);
        if (optionalBalance.isPresent()) {
            return optionalBalance.get();
        } else {
            throw new BalanceNotFoundException("balance not found");
        }
    }

    @Override
    public BalanceDTO findByIdTODTO(Long id) {
        Optional<BalanceEntity> optionalBalance = balanceRepository.findById(id);
        if (optionalBalance.isPresent()) {
            return mappingUtilsBalance.mapToBalanceDto(optionalBalance.get());
        } else {
            throw new BalanceNotFoundException("balance not found");
        }
    }

    @Override
    public BalanceDTO findByOperationIdDTO(Long id) {
        Optional<BalanceEntity> optionalBalance = balanceRepository.findByOperationId(id);
        if (optionalBalance.isPresent()) {
            return mappingUtilsBalance.mapToBalanceDto(optionalBalance.get());
        } else {
            throw new BalanceNotFoundException("balance not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<BalanceEntity> optionalBalance = balanceRepository.findById(id);
        if (optionalBalance.isPresent()) {
            balanceRepository.deleteById(id);
        } else {
            throw new BalanceNotFoundException("balance not found");
        }
    }

    @Override
    public void addNewBalance(Double debit, Double credit) {
        LocalDateTime create_date = LocalDateTime.now();
        if (debit == null) {
            throw new DebitIsEmptyException("debit is empty");
        }
        if (credit == null) {
            throw new CreditIsEmptyException("credit is empty");
        }
        if (debit < 0) {
            throw new DebitNotPositive("debit not positive");
        }
        if (credit < 0) {
            throw new CreditNotPositive("credit not positive");
        }
        Double amount = debit - credit;
        balanceRepository.addNewBalance(create_date, debit, credit, amount);

    }

    @Override
    public void updateBalance(Long id, Double debit, Double credit) {
        if (debit == null) {
            throw new DebitIsEmptyException("debit is empty");
        }
        if (credit == null) {
            throw new CreditIsEmptyException("credit is empty");
        }
        if (debit < 0) {
            throw new DebitNotPositive("debit not positive");
        }
        if (credit < 0) {
            throw new CreditNotPositive("credit not positive");
        }
        Double amount = debit - credit;
        balanceRepository.updateBalance(id, debit, credit, amount);

    }

    @Override
    public List<BalanceDTO> findByDebitCredit(Double debit, Double credit) {
        if (debit == null) {
            throw new DebitIsEmptyException("debit is empty");
        }
        if (credit == null) {
            throw new CreditIsEmptyException("credit is empty");
        }
        return StreamSupport.stream(balanceRepository.findByDebitCredit(debit, credit).spliterator(), false).map(mappingUtilsBalance::mapToBalanceDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }
}
