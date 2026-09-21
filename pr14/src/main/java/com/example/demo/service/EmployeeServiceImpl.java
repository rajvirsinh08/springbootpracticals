package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // create employee
    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // get all employee
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // get by Id employee
    @Override
    public Employee getEmployeeById(Integer id) {

        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee == null) {
            throw new ResourceNotFoundException(
                    "Employee not found with id: " + id);
        }

        return employee;
    }

    // update employee
    @Override
    public Employee updateEmployee(Integer id, Employee employeeDetails) {

        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee == null) {
            throw new ResourceNotFoundException(
                    "Employee not found with id: " + id);
        }

        employee.setEmpName(employeeDetails.getEmpName());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setSalary(employeeDetails.getSalary());

        return employeeRepository.save(employee);
    }

    // delete employee
    @Override
    public void deleteEmployee(Integer id) {

        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee == null) {
            throw new ResourceNotFoundException(
                    "Employee not found with id: " + id);
        }

        employeeRepository.delete(employee);
    }
}