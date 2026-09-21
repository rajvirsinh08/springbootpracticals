package com.example.pr5.controller;

import com.example.pr5.entity.Department;
import com.example.pr5.entity.Employee;
import com.example.pr5.repository.DepartmentRepository;
import com.example.pr5.repository.EmployeeRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentRestController {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentRestController(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {

        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    // ==========================================
    // CREATE DEPARTMENT WITH EMPLOYEES
    // ==========================================

    @PostMapping
    public ResponseEntity<Department> createDepartment(
            @RequestBody Department department) {

        if (department.getEmployees() != null) {

            for (Employee employee : department.getEmployees()) {
                employee.setDepartment(department);
            }
        }

        Department savedDepartment =
                departmentRepository.save(department);

        return new ResponseEntity<>(
                savedDepartment,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET DEPARTMENT WITH EMPLOYEES
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(
            @PathVariable int id) {

        Optional<Department> department =
                departmentRepository.findById(id);

        if (department.isPresent()) {
            return ResponseEntity.ok(department.get());
        }

        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // GET ONLY EMPLOYEES OF DEPARTMENT
    // ==========================================

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getDepartmentEmployees(
            @PathVariable int id) {

        Optional<Department> department =
                departmentRepository.findById(id);

        if (department.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                department.get().getEmployees()
        );
    }

    // ==========================================
    // RENAME DEPARTMENT
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable int id,
            @RequestBody Department departmentDetails) {

        Optional<Department> existingDepartment =
                departmentRepository.findById(id);

        if (existingDepartment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Department department =
                existingDepartment.get();

        department.setDeptName(
                departmentDetails.getDeptName()
        );

        Department updatedDepartment =
                departmentRepository.save(department);

        return ResponseEntity.ok(updatedDepartment);
    }

    // ==========================================
    // UPDATE EMPLOYEE WITHIN DEPARTMENT
    // ==========================================

    @PutMapping("/{deptId}/employees/{empId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int deptId,
            @PathVariable int empId,
            @RequestBody Employee employeeDetails) {

        Optional<Department> department =
                departmentRepository.findById(deptId);

        if (department.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Employee> existingEmployee =
                employeeRepository.findById(empId);

        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee =
                existingEmployee.get();

        if (employee.getDepartment() == null ||
                employee.getDepartment().getDeptId() != deptId) {

            return ResponseEntity.notFound().build();
        }

        if (employeeDetails.getEmpName() != null) {
            employee.setEmpName(
                    employeeDetails.getEmpName()
            );
        }

        if (employeeDetails.getSalary() != 0) {
            employee.setSalary(
                    employeeDetails.getSalary()
            );
        }

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return ResponseEntity.ok(updatedEmployee);
    }

    // ==========================================
    // DELETE SINGLE EMPLOYEE
    // ==========================================

    @DeleteMapping("/{deptId}/employees/{empId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int deptId,
            @PathVariable int empId) {

        Optional<Department> department =
                departmentRepository.findById(deptId);

        if (department.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Employee> existingEmployee =
                employeeRepository.findById(empId);

        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee =
                existingEmployee.get();

        if (employee.getDepartment() == null ||
                employee.getDepartment().getDeptId() != deptId) {

            return ResponseEntity.notFound().build();
        }

        employeeRepository.delete(employee);

        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // DELETE DEPARTMENT WITH ALL EMPLOYEES
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable int id) {

        Optional<Department> department =
                departmentRepository.findById(id);

        if (department.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        departmentRepository.delete(department.get());

        return ResponseEntity.noContent().build();
    }
}