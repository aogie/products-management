package com.example.demo.models;

import javax.persistence.*;
import java.util.Calendar;

//Plain Object Java Object = POJO
@Entity
@Table(name="tblProduct")
public class Product{
    //This is primaryKey
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) //auto-increment
    //you can also use "sequence"
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    //auto increment
    private Long id;
    @Column(nullable = false, unique = true, length = 300)
    private String productName;
    private int year;
    private Double price;
    private String company;

    public Product() {
    }

    @Transient
    private int age;

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }

    public Product(String productName, int year, Double price, String url) {
        this.productName = productName;
        this.company = url;
        this.year = year;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", url='" + company + '\'' +
                ", year=" + year +
                ", price=" + price +
                '}';
    }
}
