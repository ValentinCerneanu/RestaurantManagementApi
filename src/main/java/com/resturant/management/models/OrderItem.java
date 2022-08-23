package com.resturant.management.models;

import javax.persistence.*;

@Entity(name = "order_item")
public class OrderItem {
    @Id
    @SequenceGenerator(
            name="order_item_sequence",
            sequenceName = "order_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_item_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "menu_item",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "menu_item_fk"
            )
    )
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(
            name = "order_item",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "order_item_fk"
            )
    )
    private MenuOrder menuOrder;
    private int quantity;

    public OrderItem(MenuOrder menuOrder, MenuItem menuItem, int quantity) {
        this.menuOrder = menuOrder;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuOrder getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(MenuOrder menuOrder) {
        this.menuOrder = menuOrder;
    }
}
