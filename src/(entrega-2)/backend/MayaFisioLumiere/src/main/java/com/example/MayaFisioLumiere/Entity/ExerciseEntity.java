package com.example.MayaFisioLumiere.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "exercises")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exercise_ID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String midiaURL;

    @Column(nullable = false)
    private String tags;

    @Column(nullable = false)
    private String description;

}
