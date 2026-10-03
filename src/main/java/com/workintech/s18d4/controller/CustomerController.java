package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll().stream().map(CustomerController::toResponse).toList();
    }

    @GetMapping("/{id}")
    public CustomerResponse find(@PathVariable long id) {
        return toResponse(customerService.find(id));
    }

    @PostMapping
    public CustomerResponse save(@RequestBody Customer customer) {
        return toResponse(customerService.save(customer));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable long id, @RequestBody Customer customer) {
        Customer existing = customerService.find(id);
        if (existing == null) {
            return null;
        }
        customer.setId(id);
        customer.setAccounts(existing.getAccounts());
        if (customer.getAddress() == null) {
            customer.setAddress(existing.getAddress());
        }
        return toResponse(customerService.save(customer));
    }

    @DeleteMapping("/{id}")
    public CustomerResponse delete(@PathVariable long id) {
        return toResponse(customerService.delete(id));
    }

    static CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary());
    }
}
