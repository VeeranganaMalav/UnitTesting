package com.example.springbootunittesting.controller;

import com.example.springbootunittesting.model.Employee;
import com.example.springbootunittesting.repository.IEmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.example.springbootunittesting")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {EmployeeController.class})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IEmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;


    Employee employee;
    List<Employee> employees;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    @Order(1)
    void getAllEmployees() throws Exception{
        employees = new ArrayList<>();
        employees.add(new Employee(1L, "Veerangana", "Malav", "veerangana@yahoo.in"));
        employees.add(new Employee(2L, "Rajit", "Malav", "rajit@yahoo.in"));


        when(employeeRepository.findAll()).thenReturn(employees);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void createEmployee() throws Exception {
        employee = new Employee(3L, "Vanshika", "Malav", "vanshika@yahoo.in");

        when(employeeRepository.save(employee)).thenReturn(employee);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(employee);

        this.mockMvc.perform(post("/api/v1/employees")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    void getEmployeeById() throws Exception{
        employee = new Employee(2L, "Rajit", "Malav", "rajit@yahoo.in");
        when(employeeRepository.getById(2L)).thenReturn(employee);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rajit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Malav"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.emailId").value("rajit@yahoo.in"))
                .andDo(print());

    }

    @Test
    @Order(4)
    void updateEmployee() throws Exception {
        employee = new Employee(3L, "Vanshika", "Malav", "vanshika@yahoo.com");

        when(employeeRepository.getById(3L)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonBody = objectMapper.writeValueAsString(employee);

        this.mockMvc.perform(put("/api/v1/employees/{id}",3L)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Vanshika"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Malav"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.emailId").value("vanshika@yahoo.com"))
                        .andDo(print());
    }

    @Test
    @Order(5)
    void deleteEmployee() throws Exception {
        employee = new Employee(3L, "Vanshika", "Malav", "vanshika@yahoo.com");

        when(employeeRepository.getById(3L)).thenReturn(employee);

        this.mockMvc.perform(delete("/api/v1/employees/{id}", 3L))
                .andExpect(status().isNotFound());
    }
}