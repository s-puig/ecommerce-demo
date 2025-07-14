package com.demo.ecommerce.products;

import com.demo.ecommerce.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "owner_id")
    private User user;
    @Column(name = "name")
    @NotBlank
    private String name;
    @Column(name = "description")
    private String description;
    @Builder.Default
    @NotNull
    @Column(name = "active")
    private boolean active = true;
    @CreatedDate
    @Column(name = "createdAt")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updatedAt")
    private Instant updatedAt;
}
