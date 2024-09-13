package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/account")
    public String getOpenAccount() {
        return "openAccount";
    }

    @PostMapping("/account")
    public String openAccount(
            @RequestParam String accountType,
            @RequestParam double initialDeposit,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        accountService.openNewAccout(accountType, initialDeposit, userDetails.getUsername());
        model.addAttribute("message", "Account created successfully!");
        return "redirect:/account/success";
    }

    @GetMapping("/account/success")
    public String getAccountSuccess() {
        return "accountSuccess";
    }

    @GetMapping("/transactions/deposit")
    public String getDeposit() {
        return "deposit";
    }

    @PostMapping("/transactions/deposit")
    public String deposit(
            @RequestParam String accountNumber,
            @RequestParam double amount
    ) {
        accountService.depositMoney(accountNumber, amount);
        return "redirect:/";
    }

    @GetMapping("/transactions/withdraw")
    public String getWithdraw() {
        return "withdraw";
    }

    @PostMapping("/transactions/withdraw")
    public String withdraw(
            @RequestParam String accountNumber,
            @RequestParam double amount
    ) {
        accountService.withdrawMoney(accountNumber, amount);
        return "redirect:/";
    }

    @GetMapping("/transactions/transfer")
    public String getTransfer() {
        return "transfer";
    }

    @PostMapping("/transactions/transfer")
    public String transfer(
            @RequestParam(name = "fromAccount") String fromAccountNumber,
            @RequestParam(name = "toAccount") String toAccountNumber,
            @RequestParam double amount
    ) {
        accountService.transferMoney(fromAccountNumber, toAccountNumber, amount);
        return "redirect:/";
    }
}
