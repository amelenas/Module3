package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditOrderListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@EntityListeners(AuditOrderListener.class)
@Table(name = "users_orders", indexes = @Index(name = "users_orders_user_id_index", columnList = "user_id"))
public class Order implements Serializable {

    private static final long serialVersionUID = 333L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "certificate_id")
    private long certificateId;

    @Min(value = 0)
    @Max(value = 1000000)
    @Column(name = "cost")
    private double cost;

    @Column(name = "buy_date")
    private Instant dateOfBuy;

    @Column(name = "lock_order")
    private Integer lock;

    public Order() {
    }

    public Order(long orderId, long userId, long certificateId,
                 Double cost, Instant dateOfBuy) {
        this.orderId = orderId;
        this.userId = userId;
        this.certificateId = certificateId;
        this.cost = cost;
        this.dateOfBuy = dateOfBuy;
    }

    public Order(long userId, long certificateId,
                 double cost, Instant dateOfBuy) {
        this.userId = userId;
        this.certificateId = certificateId;
        this.cost = cost;
        this.dateOfBuy = dateOfBuy;
    }
    public Order( double cost, Instant dateOfBuy) {
        this.cost = cost;
        this.dateOfBuy = dateOfBuy;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(long certificateId) {
        this.certificateId = certificateId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getDateOfBuy() {
        return dateOfBuy;
    }

    public void setDateOfBuy(Instant dateOfBuy) {
        this.dateOfBuy = dateOfBuy;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return this.getClass() +
                " orderId = " + orderId +
                ", userId = " + userId +
                ", giftId = " + certificateId +
                ", cost = " + cost +
                ", dateOfBuy = " + dateOfBuy +
                ", lock = " + lock ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderId() == order.getOrderId() && getUserId() == order.getUserId() && getCertificateId() == order.getCertificateId() && Double.compare(order.getCost(), getCost()) == 0 && getDateOfBuy().equals(order.getDateOfBuy()) && getLock().equals(order.getLock());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getUserId(), getCertificateId(), getCost(), getDateOfBuy(), getLock());
    }
}
