package com.demo.ecommerce.users;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.EnumSet;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="mail")
    private String email;
    /** Password is hashed (using bcrypt) at the service layer **/
    @Column(name="password")
    private String password;
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private EnumSet<Role> role;
    @CreatedDate
    @Column(name="createdAt")
    private Instant createdAt;
    @LastModifiedDate
    @Column(name="updatedAt")
    private Instant updatedAt;
    @Version
    private Integer version;
}
