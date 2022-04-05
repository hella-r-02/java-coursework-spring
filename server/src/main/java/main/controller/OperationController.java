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

import main.dto.OperationDTO;
import main.entity.BalanceEntity;
import main.entity.OperationEntity;
import main.exception.AmountIsEmptyException;
import main.exception.ArticleNotFoundException;
import main.exception.BalanceNotFoundException;
import main.exception.OperationNotFoundException;
import main.service.OperationService;

@RestController
@RequestMapping("/operation")
public class OperationController {
    @Autowired
    private OperationService operationService;

    @GetMapping()
    public ResponseEntity<List<OperationDTO>> getOperations() {
        List<OperationDTO> list = operationService.findAllOperations();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OperationEntity> getOperationById(@PathVariable("id") Long id) {
        try {
            OperationEntity operationEntity = operationService.findById(id);
            return new ResponseEntity<>(operationEntity, HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }

    @GetMapping(value = "/dto/{id}")
    public ResponseEntity<OperationDTO> getOperationByIdDTO(@PathVariable("id") Long id) {
        try {
            OperationDTO operationDTO = operationService.findByIdDto(id);
            return new ResponseEntity<>(operationDTO, HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<BalanceEntity> editOperation(@PathVariable("id") Long id, @RequestParam LinkedMultiValueMap<String, String> map) {
        try {
            operationService.updateOperation(id, Long.parseLong(Objects.requireNonNull(map.getFirst("article_id"))), Long.parseLong(Objects.requireNonNull(map.getFirst("balance_id"))));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BalanceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        } catch (ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }

    @PostMapping(value = "/remove/{id}")
    public ResponseEntity<BalanceEntity> removeOperation(@PathVariable("id") Long id) {
        try {
            operationService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OperationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }

    @PostMapping(value = "/remove/article_id/{id}")
    public ResponseEntity<BalanceEntity> removeOperationByArticleId(@PathVariable("id") Long id) {
        try {
            operationService.deleteByArticleId(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OperationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "article not found");
        }
    }

    @PostMapping(value = "/remove/balance_id/{id}")
    public ResponseEntity<BalanceEntity> removeOperationByBalanceId(@PathVariable("id") Long id) {
        try {
            operationService.deleteByBalanceId(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OperationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "balance not found");
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<OperationEntity> addOperation(@RequestParam LinkedMultiValueMap<String, String> map) {
        try {
            operationService.addNewOperation(Long.parseLong(Objects.requireNonNull(map.getFirst("article_id"))), Long.parseLong(Objects.requireNonNull(map.getFirst("balance_id"))));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BalanceNotFoundException | ArticleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "operation not found");
        }
    }

    @PostMapping("/find/lower")
    public ResponseEntity<List<OperationDTO>> findOperationByLowerMarginPost(@RequestParam Double amount) {
        try {
            List<OperationDTO> list = operationService.findWithLowerMarginOfAmount(amount);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (AmountIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is empty");
        }

    }

    @PostMapping("/find/name")
    public ResponseEntity<List<OperationDTO>> findOperationByArticleName(@RequestParam String name) {
        try {
            List<OperationDTO> list = operationService.findByArticleName(name);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (AmountIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is empty");
        }

    }

    @PostMapping("/find/upper")
    public ResponseEntity<List<OperationDTO>> findOperationByUpperMarginPost(@RequestParam Double amount) {
        try {
            List<OperationDTO> list = operationService.findWithUpperMarginOfAmount(amount);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (AmountIsEmptyException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is empty");
        }
    }

    @GetMapping("/find/article_id/{id}")
    public ResponseEntity<List<OperationDTO>> findOperationByArticleId(@PathVariable Long id) {
        List<OperationDTO> list = operationService.findByArticleId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @GetMapping("/find/balance_id/{id}")
    public ResponseEntity<List<OperationDTO>> findOperationByBalanceId(@PathVariable Long id) {
        List<OperationDTO> list = operationService.findByBalanceId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
