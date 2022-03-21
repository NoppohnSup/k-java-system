package com.example.kjavasystem.auth.service;

import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private EmployeeRepository employeeRepository;

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> employee = employeeRepository.findByUsername(username);
		if (employee.isPresent()) {
			return new User(employee.get().getUserName(), employee.get().getPassword(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}