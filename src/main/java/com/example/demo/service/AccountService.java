package com.example.demo.service;

public interface AccountService {
    void openNewAccout(String accountType, double initialDeposit, String username);
    void depositMoney(String accountNumber, double amount);
    void withdrawMoney(String accountNumber, double amount);
    void transferMoney(String fromAccountNumber, String toAccountNumber, double amount);
}
