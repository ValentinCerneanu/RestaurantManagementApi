package com.resturant.management.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.persistence.*;
import java.util.List;

@Entity(name="order_t")
public class Order {
    @Id
    @SequenceGenerator(
            name="order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "total_price",
            columnDefinition = "DECIMAL"
    )
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(
            name = "waiter_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "waiter_order_fk"
            )
    )
    private Waiter waiter;
    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String status;

    @Column(
            name = "table_id",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String tableId;

    @OneToMany
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "order_fk"
            )
    )
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(Waiter waiter, String status, String tableId) {
        this.waiter = waiter;
        this.status = status;
        this.tableId = tableId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTable() {
        return tableId;
    }

    public void setTable(String table) {
        this.tableId = table;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", waiter=" + waiter +
                ", status='" + status + '\'' +
                ", tableId='" + tableId + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
//
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
