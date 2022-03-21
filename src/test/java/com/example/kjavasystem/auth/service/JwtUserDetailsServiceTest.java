package com.example.kjavasystem.auth.service;

import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("test case loadUserByUsername success")
    public void test_loadUserByUsername_success() {
        JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService();
        jwtUserDetailsService.setEmployeeRepository(employeeRepository);
        Employee employee = new Employee();
        employee.setUserName("test");
        employee.setPassword("1234");

        when(employeeRepository.findByUsername(anyString())).thenReturn(Optional.of(employee));
        UserDetails actual = jwtUserDetailsService.loadUserByUsername("test");

        assertEquals(actual.getUsername(), "test");
        assertEquals(actual.getPassword(), "1234");
    }

    @Test
    @DisplayName("test case loadUserByUsername exception")
    public void test_loadUserByUsername_exception() {
        JwtUserDetailsService jwtUserDetailsService = new JwtUserDetailsService();
        jwtUserDetailsService.setEmployeeRepository(employeeRepository);

        when(employeeRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername("test"));

        String expectedMessage = "User not found with username: test";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}