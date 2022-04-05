package main.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import main.dto.BalanceDTO;
import main.entity.BalanceEntity;
import main.exception.BalanceNotFoundException;
import main.exception.CreditIsEmptyException;
import main.exception.CreditNotPositive;
import main.exception.DebitIsEmptyException;
import main.exception.DebitNotPositive;
import main.exception.OperationNotFoundException;
import main.service.BalanceService;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    @Autowired
    private BalanceService balanceService;

    @GetMapping()
    public ResponseEntity<List<BalanceDTO>> getBalance() {
        List<BalanceDTO> list = balanceService.findAllBalance();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BalanceEntity> getBalanceById(@PathVariable("id") Long id) {
        try {
            BalanceEntity balanceEntity = balanceService.findById(id);
            return new ResponseEntity<>(balanceEntity, HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        }
    }

    @GetMapping(value = "/dto/{id}")
    public ResponseEntity<BalanceDTO> getBalanceByIdDTO(@PathVariable("id") Long id) {
        try {
            BalanceDTO balanceDTO = balanceService.findByIdTODTO(id);
            return new ResponseEntity<>(balanceDTO, HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<BalanceEntity> editBalance(@PathVariable("id") Long id, @RequestParam LinkedMultiValueMap<String, String> map) {
        try {
            balanceService.updateBalance(id, Double.parseDouble(Objects.requireNonNull(map.getFirst("debit"))), Double.parseDouble(Objects.requireNonNull(map.getFirst("credit"))));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        } catch (CreditNotPositive exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credit not positive");
        } catch (DebitNotPositive exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "debit not positive");
        } catch (DebitIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "debit is empty");
        } catch (CreditIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credit is empty");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<BalanceEntity> addBalance(@RequestParam LinkedMultiValueMap<String, String> map) {
        try {
            balanceService.addNewBalance(Double.parseDouble(Objects.requireNonNull(map.getFirst("debit"))), Double.parseDouble(Objects.requireNonNull(map.getFirst("credit"))));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        } catch (CreditNotPositive exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credit not positive");
        } catch (DebitNotPositive exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "debit not positive");
        } catch (DebitIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "debit is empty");
        } catch (CreditIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credit is empty");
        }
    }

    @PostMapping(value = "/remove/{id}")
    public ResponseEntity<BalanceEntity> removeBalance(@PathVariable("id") Long id) {
        try {
            balanceService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        }
    }

    @PostMapping(value = "/find")
    public ResponseEntity<List<BalanceDTO>> findBalanceByDebitCreditPost(@RequestParam LinkedMultiValueMap<String, String> map) {
        try {
            List<BalanceDTO> list = balanceService.findByDebitCredit(Double.parseDouble(Objects.requireNonNull(map.getFirst("debit"))), Double.parseDouble(map.getFirst("credit")));
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (DebitIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "debit is empty");
        } catch (CreditIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credit is empty");
        }
    }

    @GetMapping(value = "/find/operation_id/{id}")
    public ResponseEntity<BalanceDTO> findBalanceByOperationId(@PathVariable("id") Long id) {
        try {
            BalanceDTO balanceDTO = balanceService.findByOperationIdDTO(id);
            return new ResponseEntity<>(balanceDTO, HttpStatus.OK);
        } catch (OperationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }
}
