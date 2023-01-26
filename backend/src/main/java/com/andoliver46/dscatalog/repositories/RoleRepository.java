package com.andoliver46.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andoliver46.dscatalog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
