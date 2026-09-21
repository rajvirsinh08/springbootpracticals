package com.example.pr7.controller;

import com.example.pr7.entity.Account;
import com.example.pr7.exception.AccountNotZeroException;
import com.example.pr7.exception.InsufficientBalanceException;
import com.example.pr7.service.AccountServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private final AccountServiceImpl accountService;

    public AccountRestController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    // POST /api/accounts/transfer
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(
            @RequestBody Map<String, Object> request) {

        int fromId =
                ((Number) request.get("fromId")).intValue();

        int toId =
                ((Number) request.get("toId")).intValue();

        double amount =
                ((Number) request.get("amount")).doubleValue();

        accountService.transferFunds(
                fromId,
                toId,
                amount
        );

        return ResponseEntity.ok("Transfer successful");
    }

    // GET /api/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(
            @PathVariable int id) {

        return ResponseEntity.ok(
                accountService.getAccount(id)
        );
    }

    // PUT /api/accounts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {

        String accountHolder =
                request.get("accountHolder");

        Account account =
                accountService.updateAccountHolder(
                        id,
                        accountHolder
                );

        return ResponseEntity.ok(account);
    }

    // DELETE /api/accounts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable int id) {

        accountService.deleteAccount(id);

        return ResponseEntity.noContent().build();
    }

    // Insufficient balance exception
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalance(
            InsufficientBalanceException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // Non-zero balance exception
    @ExceptionHandler(AccountNotZeroException.class)
    public ResponseEntity<String> handleAccountNotZero(
            AccountNotZeroException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // Other runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(
            RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}