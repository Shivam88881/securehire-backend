// Location.java
package com.curtin.securehire.entity;

import com.curtin.securehire.constant.LocationType;
import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne // Assuming a hierarchical location structure
    @JoinColumn(name = "parent_id") // Foreign key to the parent location
    private Location parent;
}
