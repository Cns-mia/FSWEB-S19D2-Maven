package com.workintech.s18d4.controller;

import com.workintech.s18d4.entity.Address;
import com.workintech.s18d4.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<Address> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address find(@PathVariable long id) {
        return addressService.find(id);
    }

    @PostMapping
    public Address save(@RequestBody Address address) {
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public Address update(@PathVariable long id, @RequestBody Address address) {
        Address existing = addressService.find(id);
        if (existing == null) {
            return null;
        }
        address.setId(id);
        address.setCustomer(existing.getCustomer());
        return addressService.save(address);
    }

    @DeleteMapping("/{id}")
    public Address delete(@PathVariable long id) {
        return addressService.delete(id);
    }
}
