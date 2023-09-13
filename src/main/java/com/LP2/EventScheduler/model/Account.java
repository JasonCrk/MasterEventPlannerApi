package com.LP2.EventScheduler.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String about;

    private String picture;
    private String banner;
    private String location;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private User user;
}
