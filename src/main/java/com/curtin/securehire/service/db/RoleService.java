package com.curtin.securehire.service.db;

import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.entity.db.Role;
import java.util.List;

public interface RoleService {
    // Basic CRUD operations
    Role findById(Integer roleId);
    Role findByName(RoleName name);
    List<Role> findAll();
    Role save(Role role);
    Role update(Integer roleId, Role role);
    void delete(Integer roleId);

    // Additional operations
    boolean existsByName(RoleName name);
}
