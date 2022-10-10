package com.bootcamp.capstone.practice.ExpenseDb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Employee;
import com.bootcamp.capstone.practice.ExpenseDb.Repositories.EmployeesRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

	@Autowired
	private EmployeesRepository empRepo;
	
	@GetMapping()
	public ResponseEntity<Iterable<Employee>> getEmployees() {
		Iterable<Employee> emps = empRepo.findAll();
		return new ResponseEntity<Iterable<Employee>>(emps, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Employee> getEmployeeByPK(@PathVariable int id) {
		var emp = empRepo.findById(id);
		if(emp.isEmpty() ) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if(id == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
	}
	
	@GetMapping("{email}/{password}")
	public ResponseEntity<Employee> getLogin(@PathVariable String email, @PathVariable String password) {
		var emp = empRepo.findByEmailAndPassword(email, password);
		if(emp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Employee> postEmployee(@RequestBody Employee employee) {
		if(employee.getId() != 0 ) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Employee emp = empRepo.save(employee);
		return new ResponseEntity<Employee>(emp, HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity putEmployee(@PathVariable int id, @RequestBody Employee employee) {
		if(id != employee.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Employee> emp = empRepo.findById(id);
		if(emp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		empRepo.save(employee);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteEmployee(@PathVariable int id) {
		Optional<Employee> emp = empRepo.findById(id);
		if(emp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		empRepo.delete(emp.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
