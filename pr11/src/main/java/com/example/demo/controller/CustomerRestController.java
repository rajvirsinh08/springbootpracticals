package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
	
	private final CustomerRepository customerRepository;

	public CustomerRestController(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}
	
	//add Customer
	@PostMapping
	public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDTO customerDTO)
	{
		Customer customer = new Customer();
		customer.setName(customerDTO.getName());
		customer.setCity(customerDTO.getCity());
		customer.setAge(customerDTO.getAge());
		Customer savedCustomer = customerRepository.save(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
	}
	
	//Get Customer by Id
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCusomer(@PathVariable Integer id)
	{
		Customer customer = customerRepository.findById(id).get();
		return ResponseEntity.ok(customer);
	}
	
	//Search
	@GetMapping("/search")
	public ResponseEntity<List<Customer>> search(@RequestParam String city,@RequestParam(defaultValue = "0") Integer minAge)
	{
		List<Customer> customers = customerRepository.findByCityAndAgeGreaterThanEqual(city, minAge);
		return ResponseEntity.ok(customers);
	}
	
	//update customer
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id,@RequestBody CustomerDTO customerDTO)
	{
		Customer customer = customerRepository.findById(id).get();
		customer.setName(customerDTO.getName());
		customer.setCity(customerDTO.getCity());
		customer.setAge(customerDTO.getAge());
		Customer updatedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(updatedCustomer);
	}
	
	//delete customer
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer id)
	{
		Customer customer = customerRepository.findById(id).get();
		customerRepository.delete(customer);
		return ResponseEntity.ok("Customer Deleted Successfully");
		
	}

}
