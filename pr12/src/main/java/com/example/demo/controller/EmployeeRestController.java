package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {
	
	private final EmployeeRepository employeeRepository;

	public EmployeeRestController(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}
	
	//add employee
	@PostMapping
	public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO)
	{
		Employee employee = EmployeeMapper.toEntity(employeeDTO);
		Employee savedEmployee = employeeRepository.save(employee);
		EmployeeDTO response = EmployeeMapper.toDto(savedEmployee);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	//Get All Employee
	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees()
	{
		List<Employee> employees = employeeRepository.findAll();
		
		//stream - A Stream allows us to process the objects in the list one by one.
		//.map(EmployeeMapper::toDto) - Take each Employee and convert it into an EmployeeDTO using the toDto() method.
		// :: - is called a method reference. It is a shorter way of writing a lambda expression.
		//.collect(Collectors.toList()) - Collect all those EmployeeDTO objects and put them into a List.
		List<EmployeeDTO> employeeDTOs = employees.stream().map(EmployeeMapper::toDto).collect(Collectors.toList());
		
		return ResponseEntity.ok(employeeDTOs);
	}
	
	//update employee
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO)
	{
		Optional<Employee> optionalEmployee = employeeRepository.findById(id);
		
		if(optionalEmployee.isPresent())
		{
			Employee employee = optionalEmployee.get();
			employee.setEmpName(employeeDTO.getEmpName());
			employee.setDepartment(employeeDTO.getDepartment());
			
			Employee updatedEmployee = employeeRepository.save(employee);
			EmployeeDTO response = EmployeeMapper.toDto(updatedEmployee);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.noContent().build();
	}
	
	//delete employee
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id)
	{
		if(employeeRepository.existsById(id))
		{
			employeeRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}

}
