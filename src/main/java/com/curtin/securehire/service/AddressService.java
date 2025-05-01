// src/main/java/com/curtin/securehire/service/AddressService.java
package com.curtin.securehire.service;

import com.curtin.securehire.entity.Address;

import java.util.List;

public interface AddressService {
    Address findById(Integer addressId);
    List<Address> findAll();
    Address save(Address address);
    Address update(Integer addressId, Address address);
    void delete(Integer addressId);
}
