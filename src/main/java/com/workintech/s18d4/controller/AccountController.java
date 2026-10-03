package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<AccountResponse> findAll() {
        return accountService.findAll().stream().map(AccountController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public AccountResponse find(@PathVariable long id) {
        return toResponse(accountService.find(id));
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@PathVariable long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
        account.setCustomer(customer);
        customer.addAccount(account);
        return toResponse(accountService.save(account));
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@PathVariable long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
        Account toUpdate = null;
        if (customer.getAccounts() != null) {
            for (Account existing : customer.getAccounts()) {
                if (existing.getId() == account.getId()) {
                    toUpdate = existing;
                    break;
                }
            }
        }
        if (toUpdate == null) {
            throw new RuntimeException("Account " + account.getId() + " not found for customer " + customerId);
        }
        int index = customer.getAccounts().indexOf(toUpdate);
        account.setCustomer(customer);
        customer.getAccounts().set(index, account);
        return toResponse(accountService.save(account));
    }

    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable long id) {
        Account account = accountService.find(id);
        if (account == null) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        return toResponse(accountService.delete(id));
    }

    private static AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(),
                CustomerController.toResponse(account.getCustomer()));
    }
}
