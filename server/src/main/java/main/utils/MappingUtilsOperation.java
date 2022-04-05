package main.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dto.OperationDTO;
import main.entity.OperationEntity;

@Service
public class MappingUtilsOperation {
    @Autowired
    private MappingUtilsBalance mappingUtilsBalance;
    public OperationDTO mapToOperationDto(OperationEntity operation){
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setArticle(operation.getArticle());
        dto.setBalance(mappingUtilsBalance.mapToBalanceDto(operation.getBalance()));
        dto.setDebit(operation.getDebit());
        dto.setCredit(operation.getCredit());
        dto.setCreate_date(operation.getCreate_date().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }
    public OperationEntity mapToOperation(OperationDTO dto){
        OperationEntity operation = new OperationEntity();
        operation.setId(dto.getId());
        operation.setDebit(dto.getDebit());
        operation.setCredit(dto.getCredit());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        operation.setCreate_date(LocalDateTime.parse(dto.getCreate_date(), formatter));
        return operation;
    }
}
