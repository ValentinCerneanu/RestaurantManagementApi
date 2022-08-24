package com.resturant.management.models;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Item")
public class Item {
    @Id
    @SequenceGenerator(
            name="item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "category",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String category;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "DECIMAL"
    )
    private Double price;

    @OneToMany
    @JoinColumn(
            name = "item",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "item_fk"
            )
    )
    private List<OrderItem> orderItems;

    public Item(String category, String name, Double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Item() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
