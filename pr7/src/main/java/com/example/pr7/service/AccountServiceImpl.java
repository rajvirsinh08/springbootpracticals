package com.example.pr7.service;

import com.example.pr7.entity.Account;
import com.example.pr7.exception.AccountNotZeroException;
import com.example.pr7.exception.InsufficientBalanceException;
import com.example.pr7.repository.AccountRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Transfer money from one account to another
    @Transactional
    public void transferFunds(int fromId, int toId, double amount) {

        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() ->
                        new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() ->
                        new RuntimeException("Destination account not found"));

        // Check amount
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }

        // Check balance
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance in account " + fromId);
        }

        // Debit source account
        fromAccount.setBalance(
                fromAccount.getBalance() - amount
        );

        // Credit destination account
        toAccount.setBalance(
                toAccount.getBalance() + amount
        );

        // Save both accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    // Get account
    public Account getAccount(int id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));
    }

    // Update account holder
    public Account updateAccountHolder(
            int id,
            String accountHolder) {

        Account account = getAccount(id);

        account.setAccountHolder(accountHolder);

        return accountRepository.save(account);
    }

    // Delete account only when balance is zero
    public void deleteAccount(int id) {

        Account account = getAccount(id);

        if (account.getBalance() != 0) {
            throw new AccountNotZeroException(
                    "Account cannot be deleted. Balance is "
                            + account.getBalance());
        }

        accountRepository.delete(account);
    }
}