package main.service;

import java.util.List;

import main.dto.OperationDTO;
import main.entity.OperationEntity;


public interface OperationService {
    List<OperationDTO> findAllOperations();

    OperationDTO findByIdDto(Long id);

    OperationEntity findById(Long id);

    List<OperationDTO> findByArticleName(String name);

    List<OperationDTO> findByArticleId(Long id);

    List<OperationDTO> findByBalanceId(Long id);

    List<OperationDTO> findWithUpperMarginOfAmount(Double amount);

    List<OperationDTO> findWithLowerMarginOfAmount(Double amount);

    void deleteByArticleId(Long id);

    void deleteById(Long id);

    void deleteByBalanceId(Long id);

    void addNewOperation(Long article_id, Long balance_id);

    void updateOperation(Long id, Long article_id, Long balance_id);
}
