package com.example.demo.repository;

import com.example.demo.model.Account;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
    List<Account> findAllByUser(User user);
}
