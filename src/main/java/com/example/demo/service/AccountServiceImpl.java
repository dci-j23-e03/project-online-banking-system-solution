package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.AccountNumberGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void openNewAccout(String accountType, double initialDeposit, String username) {
        User user = userRepository.findByUsername(username);
        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(initialDeposit);
        account.setUser(user);
        accountRepository.save(account);
    }

    @Override
    public void depositMoney(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !validateAccountOwnership(List.of(accountNumber))) {
            throw new RuntimeException("Account not found or not owned by this user");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void withdrawMoney(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || account.getBalance() < amount || !validateAccountOwnership(List.of(accountNumber))) {
            throw new RuntimeException("Account not found, not owned by this user or insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        if (!validateAccountOwnership(List.of(fromAccountNumber, toAccountNumber))) {
            throw new RuntimeException("One of the accounts not owned by this user");
        }
        withdrawMoney(fromAccountNumber, amount);
        depositMoney(toAccountNumber, amount);
    }

    private boolean validateAccountOwnership(List<String> accountNumbers) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User userInDb = userRepository.findByUsername(user.getUsername());
        List<Account> userAccounts = userInDb.getAccounts();
        List<String> userAccountNumbers = userAccounts
                .stream()
                .flatMap(account -> Stream.of(account.getAccountNumber()))
                .toList();
        for (String accountNumber : accountNumbers) {
            if (!userAccountNumbers.contains(accountNumber)) {
                return false;
            }
        }
        return true;
    }
}
