package com.miz.authenticateservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.Date;

@Data
@Entity
@Table(schema = "login_auth", name = "product_list")
@Where(clause = "deleted is false")
public class ProductList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createAt;

    @Column(name = "product", nullable = false)
    private String product;

    @Column
    private boolean deleted = false;
}
