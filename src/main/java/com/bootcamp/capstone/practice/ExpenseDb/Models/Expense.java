package com.bootcamp.capstone.practice.ExpenseDb.Models;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="Expenses")
public class Expense {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(length=50, nullable=false)
	private String description;
	
	@Column(length=20, nullable=false)
	private String status = "NEW";
	
	@Column(columnDefinition="decimal(9,2) NOT NULL DEFAULT 0")
	private double total;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="employeeid", columnDefinition="int")
	private Employee employee;
	
	@OneToMany(mappedBy="expense")
	private List<Expenseline> expenselines;
	
	public Expense() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Expenseline> getExpenselines() {
		return expenselines;
	}

	public void setExpenselines(List<Expenseline> expenselines) {
		this.expenselines = expenselines;
	}
	
	
}
