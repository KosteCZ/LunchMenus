package cz.koscak.jan.model;

public class MenuItem {

	private String name;
	private String price;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public String getNameAndPrice() {
		return name + (((price != null) && (price.trim() != "")) ? (", " + price) : "");
	}
	
	@Override
	public String toString() {
		return "MenuItem [name=\"" + name + "\", price=\"" + price + "\"]";
	}
	
}
