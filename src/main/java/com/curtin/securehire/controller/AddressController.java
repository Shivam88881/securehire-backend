package com.curtin.securehire.controller;

import com.curtin.securehire.entity.db.Address;
import com.curtin.securehire.service.db.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> findById(@PathVariable Integer addressId) {
        log.info("Entering findById method with addressId: {}", addressId);
        Address address = addressService.findById(addressId);
        log.info("Exiting findById method. Retrieved address: {}", address);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        log.info("Entering findAll method.");
        List<Address> addresses = addressService.findAll();
        log.info("Exiting findAll method. Retrieved {} addresses.", addresses.size());
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<Address> save(@RequestBody Address address) {
        log.info("Entering save method with address details: {}", address);
        Address savedAddress = addressService.save(address);
        log.info("Exiting save method. Saved address: {}", savedAddress);
        return ResponseEntity.ok(savedAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> update(@PathVariable Integer addressId, @RequestBody Address address) {
        log.info("Entering update method with addressId: {} and updated details: {}", addressId, address);
        Address updatedAddress = addressService.update(addressId, address);
        log.info("Exiting update method. Updated address: {}", updatedAddress);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable Integer addressId) {
        log.info("Entering delete method with addressId: {}", addressId);
        addressService.delete(addressId);
        log.info("Exiting delete method. Address deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}
