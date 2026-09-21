package com.example.pr4.controller;

import com.example.pr4.entity.Employee;
import com.example.pr4.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees-passport")
public class EmployeePassportRestController {

    private final EmployeeRepository employeeRepository;

    public EmployeePassportRestController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        if (employee.getPassport() != null) {
            employee.getPassport().setEmployee(employee);
        }

        Employee savedEmployee =
                employeeRepository.save(employee);

        return new ResponseEntity<>(
                savedEmployee,
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeRepository.findAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(
            @PathVariable int id) {

        Optional<Employee> employee =
                employeeRepository.findById(id);

        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int id) {

        Optional<Employee> employee =
                employeeRepository.findById(id);

        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employeeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}