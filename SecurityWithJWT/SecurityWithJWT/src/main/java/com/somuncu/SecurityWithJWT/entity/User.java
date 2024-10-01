package com.somuncu.SecurityWithJWT.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeBinderType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password ;

    @Column(name = "role")
    private String role ;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

}
