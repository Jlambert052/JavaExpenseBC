package com.bootcamp.capstone.practice.ExpenseDb.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Expenseline;
import com.bootcamp.capstone.practice.ExpenseDb.Repositories.ExpenselinesRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/expenselines")
public class ExpenselinesController {

	private ExpenselinesRepository explineRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Expenseline>> getExpenselines() {
		Iterable<Expenseline> explines = explineRepo.findAll();
		return new ResponseEntity<Iterable<Expenseline>>(explines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Expenseline> getExpenselineByPK(@PathVariable int id) {
		if(id == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Expenseline> newExpline = explineRepo.findById(id);
		if(newExpline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Expenseline>(newExpline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Expenseline> postExpenseline(@RequestBody Expenseline expenseline) {
		if(expenseline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newExpLine = explineRepo.save(expenseline);
		return new ResponseEntity<Expenseline>(newExpLine, HttpStatus.OK);
	}
	
	@PutMapping("{id}")
	public ResponseEntity putExpenseline(@PathVariable int id, @RequestBody Expenseline expenseline) {
		if(expenseline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Optional<Expenseline> newExpline = explineRepo.findById(id);
		if(newExpline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteExpenseline(@PathVariable int id) {
		if(id ==0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newExpline = explineRepo.findById(id);
		if(newExpline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		explineRepo.delete(newExpline.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
