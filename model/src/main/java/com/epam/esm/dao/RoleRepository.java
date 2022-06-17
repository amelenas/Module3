package com.epam.esm.dao;

import com.epam.esm.dao.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {
    Role findByRoleType(Role.RoleType roleType);
}
