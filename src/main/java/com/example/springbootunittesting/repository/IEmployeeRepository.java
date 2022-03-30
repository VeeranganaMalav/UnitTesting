package com.example.springbootunittesting.repository;

import com.example.springbootunittesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

    //all crud database methods
    Boolean existsByEmailId(String emailId);

    Employee findByEmailId(String emailId);
}
