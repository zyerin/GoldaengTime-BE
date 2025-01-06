package com.goldang.goldangtime.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "pet_name", nullable = false)
    private String petName;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String residence;

    @Column(nullable = true)
    private String feature;

    @Column(name = "pet_photo")
    private String petPhoto;
}
