package tmall.bean;

import java.util.List;

public class Category {
	private int id;
	private String name;
	List<Product> products;
	List<List<Product>> productsByRow;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<List<Product>> getProductsByRow() {
		return productsByRow;
	}
	public void setProductsByRow(List<List<Product>> productsByRow) {
		this.productsByRow = productsByRow;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Category [name="+name+"]";
	}
}
