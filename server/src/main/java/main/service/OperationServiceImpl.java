package main.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dto.OperationDTO;
import main.entity.ArticleEntity;
import main.entity.BalanceEntity;
import main.entity.OperationEntity;
import main.exception.AmountIsEmptyException;
import main.exception.ArticleNotFoundException;
import main.exception.BalanceNotFoundException;
import main.exception.NameIsEmptyException;
import main.exception.OperationNotFoundException;
import main.repository.ArticleRepository;
import main.repository.BalanceRepository;
import main.repository.OperationRepository;
import main.utils.MappingUtilsOperation;

@Service
public class OperationServiceImpl implements OperationService {
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private MappingUtilsOperation mappingUtilsOperation;

    @Override
    public List<OperationDTO> findAllOperations() {
        return StreamSupport.stream(operationRepository.findAll().spliterator(), false).map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public OperationDTO findByIdDto(Long id) {
        Optional<OperationEntity> operation = operationRepository.findById(id);
        if (operation.isPresent()) {
            return mappingUtilsOperation.mapToOperationDto(operation.get());
        } else {
            throw new OperationNotFoundException("operation not found");
        }
    }

    @Override
    public OperationEntity findById(Long id) {
        Optional<OperationEntity> operation = operationRepository.findById(id);
        if (operation.isPresent()) {
            return operation.get();
        } else {
            throw new OperationNotFoundException("operation not found");
        }
    }

    @Override
    public List<OperationDTO> findByArticleName(String name) {
        if (name.isEmpty()) {
            throw new NameIsEmptyException("name is empty");
        }
        return operationRepository.findByArticleName(name).stream().map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDTO> findByArticleId(Long id) {
        return operationRepository.findByArticleId(id).stream().map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDTO> findByBalanceId(Long id) {
        return operationRepository.findByBalanceId(id).stream().map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDTO> findWithUpperMarginOfAmount(Double amount) {
        if (amount == null) {
            throw new AmountIsEmptyException("amount is empty");
        }
        return StreamSupport.stream(operationRepository.findWithUpperMarginOfAmount(amount).spliterator(), false).map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDTO> findWithLowerMarginOfAmount(Double amount) {
        if (amount == null) {
            throw new AmountIsEmptyException("amount is empty");
        }
        return StreamSupport.stream(operationRepository.findWithLowerMarginOfAmount(amount).spliterator(), false).map(mappingUtilsOperation::mapToOperationDto) //оператором из streamAPI map, использовали для каждого элемента метод mapToProductDto из класса MappingUtils
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByArticleId(Long id) {
        List<OperationEntity> operation = operationRepository.findByArticleId(id);
        if (!operation.isEmpty()) {
            operationRepository.deleteByArticleId(id);
        } else {
            throw new OperationNotFoundException("Operation not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        operationRepository.deleteById(id);
    }

    @Override
    public void deleteByBalanceId(Long id) {
        List<OperationEntity> operation = operationRepository.findByBalanceId(id);
        if (!operation.isEmpty()) {
            operationRepository.deleteByBalanceId(id);
        } else {
            throw new OperationNotFoundException("Operation not found");
        }
    }

    @Override
    public void addNewOperation(Long article_id, Long balance_id) {
        Optional<ArticleEntity> article = articleRepository.findById(article_id);
        if (article.isPresent()) {
            Optional<BalanceEntity> balance = balanceRepository.findById(balance_id);
            if (balance.isPresent()) {
                LocalDateTime create_date = LocalDateTime.now();
                operationRepository.addNewOperation(article_id, balance.get().getDebit(), balance.get().getCredit(), create_date, balance_id);
            } else {
                throw new BalanceNotFoundException("balance not found");
            }
        } else {
            throw new ArticleNotFoundException("article not found");
        }

    }

    @Override
    public void updateOperation(Long id, Long article_id, Long balance_id) {
        Optional<ArticleEntity> article = articleRepository.findById(article_id);
        if (article.isPresent()) {
            Optional<BalanceEntity> balance = balanceRepository.findById(balance_id);
            if (balance.isPresent()) {
                Optional<OperationEntity> operation = operationRepository.findById(id);
                if (operation.isPresent()) {
                    operationRepository.updateOperation(id, article_id, balance.get().getDebit(), balance.get().getCredit(), balance_id);
                } else {
                    throw new OperationNotFoundException("operation not found");
                }
            } else {
                throw new BalanceNotFoundException("balance not found");
            }
        } else {
            throw new ArticleNotFoundException("article not found");
        }
    }
}
