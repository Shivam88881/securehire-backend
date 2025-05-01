package com.curtin.securehire.service.impl;

import com.curtin.securehire.entity.Role;
import com.curtin.securehire.exception.BadRequestException;
import com.curtin.securehire.exception.NotFoundException;
import com.curtin.securehire.repository.RoleRepository;
import com.curtin.securehire.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Integer roleId) {
        logger.info("Finding role by ID: {}", roleId);

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isEmpty()) {
            logger.error("Role not found with ID: {}", roleId);
            throw new NotFoundException("Role not found with ID: " + roleId);
        }

        logger.info("Found role with ID: {}", roleId);
        return roleOpt.get();
    }

    @Override
    public Role findByName(String name) {
        logger.info("Finding role by name: {}", name);

        Optional<Role> roleOpt = roleRepository.findByName(name);
        if (roleOpt.isEmpty()) {
            logger.error("Role not found with name: {}", name);
            throw new NotFoundException("Role not found with name: " + name);
        }

        logger.info("Found role with name: {}", name);
        return roleOpt.get();
    }

    @Override
    public List<Role> findAll() {
        logger.info("Fetching all roles");

        try {
            List<Role> roles = roleRepository.findAll();
            logger.info("Fetched {} roles", roles.size());
            return roles;
        } catch (Exception e) {
            logger.error("Error fetching all roles: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to fetch roles: " + e.getMessage());
        }
    }

    @Override
    public Role save(Role role) {
        logger.info("Creating new role: {}", role.getName());

        if (roleRepository.existsByName(role.getName())) {
            logger.error("Role already exists with name: {}", role.getName());
            throw new BadRequestException("Role already exists with name: " + role.getName());
        }

        try {
            Role savedRole = roleRepository.save(role);
            logger.info("Role created successfully with ID: {}", savedRole.getId());
            return savedRole;
        } catch (Exception e) {
            logger.error("Error creating role: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to create role: " + e.getMessage());
        }
    }

    @Override
    public Role update(Integer roleId, Role role) {
        logger.info("Updating role with ID: {}", roleId);

        if (!roleRepository.existsById(roleId)) {
            logger.error("Cannot update role: Role not found with ID: {}", roleId);
            throw new NotFoundException("Role not found with ID: " + roleId);
        }

        // Check if new name conflicts with existing role (except itself)
        Optional<Role> existingRoleWithSameName = roleRepository.findByName(role.getName());
        if (existingRoleWithSameName.isPresent() && existingRoleWithSameName.get().getId()!=roleId) {
            logger.error("Cannot update role: Another role already exists with name: {}", role.getName());
            throw new BadRequestException("Another role already exists with name: " + role.getName());
        }

        try {
            role.setId(roleId);
            Role updatedRole = roleRepository.save(role);
            logger.info("Role updated successfully with ID: {}", roleId);
            return updatedRole;
        } catch (Exception e) {
            logger.error("Error updating role with ID {}: {}", roleId, e.getMessage(), e);
            throw new BadRequestException("Failed to update role: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer roleId) {
        logger.info("Deleting role with ID: {}", roleId);

        if (!roleRepository.existsById(roleId)) {
            logger.error("Cannot delete role: Role not found with ID: {}", roleId);
            throw new NotFoundException("Role not found with ID: " + roleId);
        }

        try {
            roleRepository.deleteById(roleId);
            logger.info("Role deleted successfully with ID: {}", roleId);
        } catch (Exception e) {
            logger.error("Error deleting role with ID {}: {}", roleId, e.getMessage(), e);
            throw new BadRequestException("Failed to delete role: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByName(String name) {
        logger.info("Checking if role exists with name: {}", name);
        return roleRepository.existsByName(name);
    }
}
