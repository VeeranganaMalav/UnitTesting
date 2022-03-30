package com.example.springbootunittesting.repository;

import com.example.springbootunittesting.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class IEmployeeRepositoryTest {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @AfterEach
    void tearDown(){
        employeeRepository.deleteAll();
    }

    @Test
    @Rollback(value = false)
    void existsByEmailId() {
        Employee employee = Employee.builder()
                .firstName("Veerangana")
                .lastName("Malav")
                .emailId("veerangana@yahoo.in")
                .build();

        employeeRepository.save(employee);

        Boolean expected = employeeRepository.existsByEmailId("veerangana@yahoo.in");

        assertThat(expected).isTrue();
    }


    @Test
    void findByEmailId() {
        Employee employee = employeeRepository.findByEmailId("veerangana@yahoo.in");

        assertAll(
                () -> assertThat(employee.getFirstName()).isEqualTo("Veerangana"),
                () ->assertThat(employee.getLastName()).isEqualTo("Malav")
        );
    }
}