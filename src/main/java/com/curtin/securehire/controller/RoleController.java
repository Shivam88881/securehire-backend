package com.curtin.securehire.controller;

import com.curtin.securehire.constant.RoleName;
import com.curtin.securehire.entity.db.Role;
import com.curtin.securehire.service.db.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> findById(@PathVariable Integer roleId) {
        log.info("Entering findById method with roleId: {}", roleId);
        Role role = roleService.findById(roleId);
        log.info("Exiting findById method. Retrieved role: {}", role);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Role> findByName(@PathVariable RoleName name) {
        log.info("Entering findByName method with role name: {}", name);
        Role role = roleService.findByName(name);
        log.info("Exiting findByName method. Retrieved role: {}", role);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> findAll() {
        log.info("Entering findAll method.");
        List<Role> roles = roleService.findAll();
        log.info("Exiting findAll method. Retrieved {} roles.", roles.size());
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<Role> save(@RequestBody Role role) {
        log.info("Entering save method with role details: {}", role);
        Role savedRole = roleService.save(role);
        log.info("Exiting save method. Saved role: {}", savedRole);
        return ResponseEntity.ok(savedRole);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> update(@PathVariable Integer roleId, @RequestBody Role role) {
        log.info("Entering update method with roleId: {} and updated details: {}", roleId, role);
        Role updatedRole = roleService.update(roleId, role);
        log.info("Exiting update method. Updated role: {}", updatedRole);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> delete(@PathVariable Integer roleId) {
        log.info("Entering delete method with roleId: {}", roleId);
        roleService.delete(roleId);
        log.info("Exiting delete method. Role deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable RoleName name) {
        log.info("Entering existsByName method with role name: {}", name);
        boolean exists = roleService.existsByName(name);
        log.info("Exiting existsByName method. Role exists: {}", exists);
        return ResponseEntity.ok(exists);
    }
}
