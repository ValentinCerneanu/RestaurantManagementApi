package com.resturant.management.models;

public class OrderItemExtension {
    private OrderItem orderItem;
    private Long orderId;
    private String table;
    private Waiter waiter;
    private String status;

    public OrderItemExtension(OrderItem orderItem, Long orderId, String table, Waiter waiter, String status) {
        this.orderItem = orderItem;
        this.orderId = orderId;
        this.table = table;
        this.waiter = waiter;
        this.status = status;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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

    @Override
    public String toString() {
        return "OrderItemExtension{" +
                "orderItem=" + orderItem +
                ", orderId=" + orderId +
                ", table='" + table + '\'' +
                ", waiter=" + waiter +
                ", status='" + status + '\'' +
                '}';
    }
}
