package com.example.kjavasystem;

import com.example.kjavasystem.transaction.entity.BankMoney;
import com.example.kjavasystem.transaction.entity.Branch;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.repository.BankMoneyRepository;
import com.example.kjavasystem.transaction.repository.BranchRepository;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.utils.DateUtils;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class KJavaSystemApplication {
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	BranchRepository branchRepository;

	@Autowired
	BankMoneyRepository bankMoneyRepository;

	@PostConstruct
	public void initData() {
		Employee employee1 = new Employee();
		employee1.setId(1);
		employee1.setUserName("test");
		employee1.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
		employee1.setFirstName("name1");
		employee1.setLastName("last1");
		employee1.setBranchId(2);
		employee1.setRoleId(1);

		Employee employee2 = new Employee();
		employee2.setId(2);
		employee2.setUserName("test1");
		employee2.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
		employee2.setFirstName("name2");
		employee2.setLastName("last2");
		employee2.setBranchId(1);
		employee2.setRoleId(2);

		Employee employee3 = new Employee();
		employee3.setId(3);
		employee3.setUserName("test2");
		employee3.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
		employee3.setFirstName("name3");
		employee3.setLastName("last3");
		employee3.setBranchId(1);
		employee3.setRoleId(3);
		employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

		Branch branch1 = new Branch();
		branch1.setId(1);
		branch1.setName("cash center");

		Branch branch2 = new Branch();
		branch2.setId(2);
		branch2.setName("branch 1");
		branchRepository.saveAll(Arrays.asList(branch1, branch2));

		DateUtils dateUtils = new DateUtils();
		BankMoney bankMoney = new BankMoney();
		bankMoney.setId(1);
		bankMoney.setTotalMoney(0);
		bankMoney.setBranchId(1);
		bankMoney.setBranchId(1);
		bankMoney.setCreatedAt(dateUtils.getCurrentDate());
		bankMoney.setUpdatedAt(dateUtils.getCurrentDate());
		bankMoneyRepository.save(bankMoney);
	}

	public static void main(String[] args) {
		SpringApplication.run(KJavaSystemApplication.class, args);
	}

}
