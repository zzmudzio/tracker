package com.tracker.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String login;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "reportedBy")
    @Builder.Default
    private List<Bug> reportedBugs = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "assignedTo")
    @Builder.Default
    private List<Bug> assignedBugs = new ArrayList<>();
}
