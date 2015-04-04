//This is the to do list class a separate .java will need to be created for the to do list activity

package com.example.tripplanner;

public class ToDoList extends TripList {

	public ToDoList(Trip trip) {
		super(trip);
		super.setTable("todo");

	}

	public ToDoList() {
		super.setTable("todo");
	}
}
