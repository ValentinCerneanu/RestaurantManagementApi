package com.resturant.management.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.persistence.*;

@Entity(name = "order_item")
public class OrderItem {
    @JsonIgnore
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
            name = "item",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "item_fk"
            )
    )
    private Item item;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "order_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "order_fk"
            )
    )
    private Order order;

    private int quantity;
    private String comment;

    public OrderItem(Order order, Item item, int quantity) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
    }

    public OrderItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String commentary) {
        this.comment = commentary;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", item=" + item +
                ", orderId=" + order.getId() +
                ", quantity=" + quantity +
                ", comment='" + comment + '\'' +
                '}';
    }

//    public String getAsJson(){
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String json = null;
//        try{
//            json = ow.writeValueAsString(this);
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return json;
//    }
}
