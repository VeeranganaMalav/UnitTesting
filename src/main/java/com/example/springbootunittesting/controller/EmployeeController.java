package com.example.springbootunittesting.controller;

import com.example.springbootunittesting.exception.ResourceNotFoundException;
import com.example.springbootunittesting.model.Employee;
import com.example.springbootunittesting.repository.IEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeRepository employeeRepository;

//    private final IEmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //build create Employee REST API
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    //build get employee by id REST API
    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee by id " + id + " doesn't exist"));
        return ResponseEntity.ok(employee);
    }

    //update employee REST API
    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@RequestBody Employee employeeDetails){
        Employee updateEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee by id " + id + " doesn't exist"));
        updateEmployee.setFirstName(employeeDetails.getFirstName());
        updateEmployee.setLastName(employeeDetails.getLastName());
        updateEmployee.setEmailId(employeeDetails.getEmailId());

        employeeRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }

    //build delete employee REST API
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee by id " + id + " doesn't exist"));

        employeeRepository.delete(employee);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
