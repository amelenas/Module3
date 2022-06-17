package com.epam.esm.dao.entity;

import com.epam.esm.dao.entity.audit.AuditOrderListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditOrderListener.class)
@Table(name = "users_orders", indexes = @Index(name = "users_orders_user_id_index", columnList = "user_id"))
public class Order implements Serializable {

    private static final long serialVersionUID = 333L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "certificate_id")
    private Integer certificateId;

    @Min(value = 0)
    @Max(value = 1000000)
    @Column(name = "cost")
    private double cost;

    @Column(name = "buy_date")
    private Instant dateOfBuy;

    public Order() {
    }

    public Order(Integer orderId, Integer userId, Integer certificateId,
                 Double cost, Instant dateOfBuy) {
        this.orderId = orderId;
        this.userId = userId;
        this.certificateId = certificateId;
        this.cost = cost;
        this.dateOfBuy = dateOfBuy;
    }

    public Order(Integer userId, Integer certificateId,
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderId().equals(order.getOrderId()) && getUserId().equals(order.getUserId())
                && getCertificateId().equals(order.getCertificateId())
                && Double.compare(order.getCost(), getCost()) == 0
                && getDateOfBuy().equals(order.getDateOfBuy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getUserId(), getCertificateId(), getCost(), getDateOfBuy());
    }
}

