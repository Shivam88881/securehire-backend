// src/main/java/com/curtin/securehire/service/impl/AddressServiceImpl.java
package com.curtin.securehire.service.db.impl;

import com.curtin.securehire.entity.db.Address;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.db.AddressRepository;
import com.curtin.securehire.service.db.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address findById(Integer addressId) {
        logger.info("Finding address by ID: {}", addressId);

        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            logger.error("Address not found with ID: {}", addressId);
            throw new NotFoundException("Address not found with ID: " + addressId);
        }

        logger.info("Found address with ID: {}", addressId);
        return addressOpt.get();
    }

    @Override
    public List<Address> findAll() {
        logger.info("Fetching all addresses");

        try {
            List<Address> addresses = addressRepository.findAll();
            logger.info("Fetched {} addresses", addresses.size());
            return addresses;
        } catch (Exception e) {
            logger.error("Error fetching all addresses: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch addresses: " + e.getMessage());
        }
    }

    @Override
    public Address save(Address address) {
        logger.info("Creating new address");

        try {
            Address savedAddress = addressRepository.save(address);
            logger.info("Address created successfully with ID: {}", savedAddress.getId());
            return savedAddress;
        } catch (Exception e) {
            logger.error("Error creating address: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create address: " + e.getMessage());
        }
    }

    @Override
    public Address update(Integer addressId, Address address) {
        logger.info("Updating address with ID: {}", addressId);

        Optional<Address> existingAddressOpt = addressRepository.findById(addressId);
        if (existingAddressOpt.isEmpty()) {
            logger.error("Cannot update address: Address not found with ID: {}", addressId);
            throw new NotFoundException("Address not found with ID: " + addressId);
        }

        try {
            Address existingAddress = existingAddressOpt.get();

            // Only update fields that are not null
            if (address.getStreet() != null) existingAddress.setStreet(address.getStreet());
            if (address.getCity() != null) existingAddress.setCity(address.getCity());
            if (address.getState() != null) existingAddress.setState(address.getState());
            if (address.getCountry() != null) existingAddress.setCountry(address.getCountry());
            if (address.getZip() != null) existingAddress.setZip(address.getZip());

            Address updatedAddress = addressRepository.save(existingAddress);
            logger.info("Address updated successfully with ID: {}", addressId);
            return updatedAddress;
        } catch (Exception e) {
            logger.error("Error updating address with ID {}: {}", addressId, e.getMessage(), e);
            throw new BadRequestException("Failed to update address: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer addressId) {
        logger.info("Deleting address with ID: {}", addressId);

        if (!addressRepository.existsById(addressId)) {
            logger.error("Cannot delete address: Address not found with ID: {}", addressId);
            throw new NotFoundException("Address not found with ID: " + addressId);
        }

        try {
            addressRepository.deleteById(addressId);
            logger.info("Address deleted successfully with ID: {}", addressId);
        } catch (Exception e) {
            logger.error("Error deleting address with ID {}: {}", addressId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete address: " + e.getMessage());
        }
    }
}
