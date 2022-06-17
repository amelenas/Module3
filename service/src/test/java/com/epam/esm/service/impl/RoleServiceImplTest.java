package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.dao.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    private final Role.RoleType ROLE = Role.RoleType.USER;

    @Test
    void findRoleByName_Positive_Test() {
        Role role = new Role();
        Long ID = 1L;
        role.setId(ID);
        role.setRoleType(ROLE);
        when(roleRepository.findByRoleType(ROLE)).thenReturn(role);
        assertEquals(role, roleRepository.findByRoleType(ROLE));
    }
}