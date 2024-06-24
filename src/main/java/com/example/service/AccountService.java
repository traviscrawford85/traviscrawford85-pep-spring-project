package com.example.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> registerAccount(Account account) {
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(accountRepository.save(account));

    }

    public Optional<Account> loginAccount(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(acc -> acc.getPassword().equals(password));

    }

    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

}
