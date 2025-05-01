package com.curtin.securehire.service;

import com.curtin.securehire.entity.Role;
import java.util.List;

public interface RoleService {
    // Basic CRUD operations
    Role findById(Integer roleId);
    Role findByName(String name);
    List<Role> findAll();
    Role save(Role role);
    Role update(Integer roleId, Role role);
    void delete(Integer roleId);

    // Additional operations
    boolean existsByName(String name);
}
