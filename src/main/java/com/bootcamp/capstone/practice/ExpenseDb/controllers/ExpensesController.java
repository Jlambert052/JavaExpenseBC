package com.bootcamp.capstone.practice.ExpenseDb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.practice.ExpenseDb.Models.Expense;
import com.bootcamp.capstone.practice.ExpenseDb.Repositories.EmployeesRepository;
import com.bootcamp.capstone.practice.ExpenseDb.Repositories.ExpensesRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/expenses")
public class ExpensesController {

	@Autowired
	private ExpensesRepository expRepo;
	@Autowired
	private EmployeesRepository empRepo;
	
	private String PAID = "PAID";
	private String REVIEW = "REVIEW";
	private String REJECTED = "REJECTED";
	private String APPROVED = "APPROVED";
	private String NEW = "NEW";
	
	@SuppressWarnings({ "rawtypes" })
	private ResponseEntity updateEmpExpensesDueAndPaid(int employeeid) {
		var thisEmp = empRepo.findById(employeeid);
		if(thisEmp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var emp = thisEmp.get();
		var expDue = 0;
		var expPaid = 0;
		
		Iterable<Expense> expenses = expRepo.findByEmployeeId(emp.getId());
		for(var expense : expenses) {
			//must use .equals for strings comparison in java
			if(expense.getStatus().equals(PAID)) {
				expPaid += expense.getTotal();
			}if(expense.getStatus().equals(APPROVED)) {
				expDue += expense.getTotal();
			}
		}
		emp.setExpensesDue(expDue);
		emp.setExpensesPaid(expPaid);
		empRepo.save(emp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private ResponseEntity revokeApprovedStatus(int expenseid) throws Exception{
		var thisExp = expRepo.findById(expenseid).get();
		if(!thisExp.getStatus().equals(APPROVED)) {
			throw new Exception("Cannot remove status as it is not currently approved");
		}
		thisExp.setStatus(REVIEW);
		var emp = empRepo.findById(thisExp.getEmployee().getId()).get();
		var expDue = emp.getExpensesDue();
		var updatedExpDue = expDue - thisExp.getTotal();
		emp.setExpensesDue(updatedExpDue);
		empRepo.save(emp);
		expRepo.save(thisExp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity<Iterable<Expense>> getExpenses() {
		Iterable<Expense> expenses = expRepo.findAll(); 
		return new ResponseEntity<Iterable<Expense>>(expenses, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Expense> getExpenseByPK(@PathVariable int id) {
		var exp = expRepo.findById(id);
		if(exp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if(id == 0) {
			return new ResponseEntity<Expense>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Expense>(exp.get(), HttpStatus.OK);
	}
	
	@GetMapping("/approved")
	public ResponseEntity<Iterable<Expense>> getApproved() {
		Iterable<Expense> approvedExp = expRepo.findByStatus(APPROVED);
		return new ResponseEntity<Iterable<Expense>>(approvedExp, HttpStatus.OK);
	}
	
	@GetMapping("/review")
	public ResponseEntity<Iterable<Expense>> getReview() {
		Iterable<Expense> reviewExp = expRepo.findByStatus(REVIEW);
		return new ResponseEntity<Iterable<Expense>>(reviewExp, HttpStatus.OK);
		
	}
	
	@PostMapping
	public ResponseEntity<Expense> postExpense(@RequestBody Expense expense) throws Exception{
		if(expense.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Expense emp = expRepo.save(expense);
		var respEnt = updateEmpExpensesDueAndPaid(expense.getEmployee().getId());
			if(!respEnt.getStatusCode().equals(HttpStatus.OK)) {
				throw new Exception("ExpensesPaid/Due update failed.");
			}
		return new ResponseEntity<Expense>(emp, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putExpense(@PathVariable int id, @RequestBody Expense expense) throws Exception {
		if(id != expense.getId()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var emp = expRepo.findById(id);
		if(emp.isEmpty() ) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		expRepo.save(expense);
		var respEnt = updateEmpExpensesDueAndPaid(expense.getEmployee().getId());
		if(!respEnt.getStatusCode().equals(HttpStatus.OK))
			throw new Exception("ExpensesPaid/Due update failed.");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/review/{id}")
	public ResponseEntity<Expense> reviewExpense(@PathVariable int id, @RequestBody Expense expense) throws Exception {
		if(expense.getStatus().equals(PAID)) {
			throw new Exception("This expense has been paid out already!");
		}
		if(expense.getStatus().equals(APPROVED)) {
			this.revokeApprovedStatus(id);
		}
		if(expense.getTotal() <= 75) {
			approveExpense(id, expense);	
		} else {
			expense.setStatus(REVIEW);
		}
			expRepo.save(expense);
			var respEnt = updateEmpExpensesDueAndPaid(expense.getEmployee().getId());
			if(!respEnt.getStatusCode().equals(HttpStatus.OK))
				throw new Exception("ExpensesPaid/Due update failed.");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/pay/{id}")
	public ResponseEntity<Expense> payExpense(@PathVariable int id, @RequestBody Expense expense) throws Exception {
		var exp = expRepo.findById(id);
		if(exp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		expense.setStatus(PAID);
		var expenseTotal = expense.getTotal();
		var expensePaid = expense.getEmployee().getExpensesPaid();
		var expenseDue = expense.getEmployee().getExpensesDue();
		expense.getEmployee().setExpensesPaid(expensePaid+= expenseTotal);
		expense.getEmployee().setExpensesDue(expenseDue -= expenseTotal);
		expRepo.save(expense);
		var respEnt = updateEmpExpensesDueAndPaid(expense.getEmployee().getId());
		if(!respEnt.getStatusCode().equals(HttpStatus.OK))
			throw new Exception("ExpensesPaid/Due update failed.");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/approved/{id}")
	public ResponseEntity approveExpense(@PathVariable int id, @RequestBody Expense expense) throws Exception{
		if(expense.getStatus() == APPROVED) {
			throw new Exception("This is already approved.");
		}
		if(!expense.getStatus().equals(REVIEW)) {
			throw new Exception("This expense must be reviewed first");
		}
		expense.setStatus(APPROVED);
		var expTotal = expense.getTotal();
		var expDue = expense.getEmployee().getExpensesDue();
		expense.getEmployee().setExpensesDue(expDue+=expTotal);
		expRepo.save(expense);
		var respEnt = updateEmpExpensesDueAndPaid(expense.getEmployee().getId());
		if(!respEnt.getStatusCode().equals(HttpStatus.OK))
			throw new Exception("ExpensesPaid/Due update failed.");
		return putExpense(id, expense);
		
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/rejected/{id}")
	public ResponseEntity rejectExpense(@PathVariable int id, @RequestBody Expense expense)throws Exception {
		if(!expense.getStatus().equals(REVIEW)) {
			throw new Exception("This Expense must be reviewed first");
		}
		expense.setStatus(REJECTED);
		return putExpense(id, expense);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteExpense(@PathVariable int id)throws Exception {
		var emp = expRepo.findById(id); 
		if(emp.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		expRepo.delete(emp.get());
		var respEnt = updateEmpExpensesDueAndPaid(emp.get().getId());
		if(respEnt.getStatusCode() != HttpStatus.OK)
			throw new Exception("ExpensesPaid/Due update failed.");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
