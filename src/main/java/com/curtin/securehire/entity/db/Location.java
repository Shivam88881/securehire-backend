// Location.java
package com.curtin.securehire.entity.db;

import com.curtin.securehire.constant.LocationType;
import jakarta.persistence.*;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LocationType type;

    @ManyToOne
    @JoinColumn(name = "parent")
    @JsonBackReference
    private Location parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<Location> children = new ArrayList<>();


}

