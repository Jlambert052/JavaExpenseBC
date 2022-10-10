package com.bootcamp.capstone.practice.ExpenseDb.Models;

import javax.persistence.*;

@Entity
@Table(name="Expenselines")
public class Expenseline {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="expenseid")
	private Expense expense;
	
	@ManyToOne
	@JoinColumn(name="itemid")
	private Item item;
	
	public Expenseline() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
}
