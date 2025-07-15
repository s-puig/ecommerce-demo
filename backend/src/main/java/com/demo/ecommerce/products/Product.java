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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;
    @Column(name = "deletedAt")
    private Instant deletedAt;
}
