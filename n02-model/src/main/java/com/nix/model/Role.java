package com.nix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PERSON_ROLE", schema = "PUBLIC")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private long id;

    @Column(name = "NAME")
//    @NaturalId
//    @NotEmpty
    private String name;

}
