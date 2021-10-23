package com.example.nextone.model;


import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    @Column(name="name")
    private String name;
    @Column(name="price")
    private int price;
    @Column(name="image")
    private String image;

    public Product() {

    }
    public Product(String name, int price, String image){
    	super();
    	this.name = name;
    	this.price= price;
    	this.image= image;
        
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	
	
   
    
    
}
