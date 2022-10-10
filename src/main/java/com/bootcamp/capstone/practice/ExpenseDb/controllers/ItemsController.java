package com.bootcamp.capstone.practice.ExpenseDb.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Item;
import com.bootcamp.capstone.practice.ExpenseDb.Repositories.ItemsRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/items")
public class ItemsController {

	
	private ItemsRepository itemRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Item>> getItems() {
		Iterable<Item> items = itemRepo.findAll();
		return new ResponseEntity<Iterable<Item>>(items, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Item> getItemByPK(@PathVariable int id) {
		if(id == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var item = itemRepo.findById(id);
		if(item.isEmpty() ) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Item> postItem(@RequestBody Item item) {
		if(item.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Item newItem = itemRepo.save(item);
		return new ResponseEntity<Item>(newItem, HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Item> putItem(@RequestBody Item item, @PathVariable int id) {
		if(id != item.getId() ) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		Optional<Item> newItem = itemRepo.findById(id);
		if(newItem.isEmpty()) {
			return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
		}
		itemRepo.save(item);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteItem(@PathVariable int id) {
		if(id == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newItem = itemRepo.findById(id);
		if(newItem.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		itemRepo.delete(newItem.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
