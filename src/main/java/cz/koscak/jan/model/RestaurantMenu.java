package cz.koscak.jan.model;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenu {

	private String name;
	private List<MenuItem> listOfSoups = new ArrayList<>();
	private List<MenuItem> listOfMeals = new ArrayList<>();
	
	public RestaurantMenu(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<MenuItem> getListOfSoups() {
		return listOfSoups;
	}

	public void setListOfSoups(List<MenuItem> listOfSoups) {
		this.listOfSoups = listOfSoups;
	}
	
	public void addSoup(MenuItem menuItem) {
		this.listOfSoups.add(menuItem);
	}

	public List<MenuItem> getListOfMeals() {
		return listOfMeals;
	}

	public void setListOfMeals(List<MenuItem> listOfMeals) {
		this.listOfMeals = listOfMeals;
	}
	
	public void addMeal(MenuItem menuItem) {
		this.listOfMeals.add(menuItem);
	}

	@Override
	public String toString() {
		return "RestaurantMenu [name=\"" + name + "\", listOfSoups=" + listOfSoups.toString() + ", listOfMeals=" + listOfMeals.toString() + "]";
	}

}
