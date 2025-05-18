package com.curtin.securehire.repository.db;

import com.curtin.securehire.entity.db.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // Find addresses by city
    List<Address> findByCity(String city);

    // Find addresses by state
    List<Address> findByState(String state);

    // Find addresses by zip code
    List<Address> findByZip(String zip);

    // Find addresses by street containing a certain text
    List<Address> findByStreetContaining(String streetText);

    // Find addresses by city and state
    List<Address> findByCityAndState(String city, String state);

    // Find addresses by state with zip code starting with
    List<Address> findByStateAndZipStartingWith(String state, String zipPrefix);

    // Custom query to find addresses with complex criteria
    @Query("SELECT a FROM Address a WHERE " +
            "(:street IS NULL OR a.street LIKE %:street%) AND " +
            "(:city IS NULL OR a.city = :city) AND " +
            "(:state IS NULL OR a.state = :state) AND " +
            "(:zip IS NULL OR a.zip = :zip)")
    List<Address> findAddressesByFilters(
            @Param("street") String street,
            @Param("city") String city,
            @Param("state") String state,
            @Param("zip") String zip);
}