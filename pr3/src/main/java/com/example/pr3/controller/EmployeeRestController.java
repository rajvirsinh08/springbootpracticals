package com.example.pr3.controller;

import com.example.pr3.entity.Employee;
import com.example.pr3.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeRepository employeeRepository;

    public EmployeeRestController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        Employee savedEmployee =
                employeeRepository.save(employee);

        return new ResponseEntity<>(
                savedEmployee,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getByDepartment(
            @PathVariable String department) {

        List<Employee> employees =
                employeeRepository.findByDepartment(department);

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/count/{department}")
    public ResponseEntity<Long> countByDepartment(
            @PathVariable String department) {

        long count =
                employeeRepository.countByDepartment(department);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/salary-above")
    public ResponseEntity<List<Employee>> getSalaryAbove(
            @RequestParam double salary) {

        List<Employee> employees =
                employeeRepository.findBySalaryGreaterThan(salary);

        return ResponseEntity.ok(employees);
    }


    @GetMapping("/salary-range")
    public ResponseEntity<List<Employee>> getSalaryRange(
            @RequestParam double min,
            @RequestParam double max) {

        List<Employee> employees =
                employeeRepository.findBySalaryRange(min, max);

        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int id,
            @RequestBody Employee employeeDetails) {

        Optional<Employee> existingEmployee =
                employeeRepository.findById(id);

        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = existingEmployee.get();

        employee.setSalary(employeeDetails.getSalary());

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return ResponseEntity.ok(updatedEmployee);
    }

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