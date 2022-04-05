package main.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import main.dto.BalanceDTO;
import main.entity.BalanceEntity;

@Service
public class MappingUtilsBalance {
    public BalanceDTO mapToBalanceDto(BalanceEntity balance){
        BalanceDTO dto = new BalanceDTO();
        dto.setId(balance.getId());
        dto.setDebit(balance.getDebit());
        dto.setCredit(balance.getCredit());
        dto.setAmount(balance.getAmount());
        dto.setCreate_date(balance.get_create_date().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }
    public BalanceEntity mapToBalance(BalanceDTO dto){
        BalanceEntity balance = new BalanceEntity();
        balance.setId(dto.getId());
        balance.setDebit(dto.getDebit());
        balance.setCredit(dto.getCredit());
        balance.setAmount(dto.getAmount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        balance.set_create_date(LocalDateTime.parse(dto.getCreate_date(), formatter));
        return balance;
    }
}
