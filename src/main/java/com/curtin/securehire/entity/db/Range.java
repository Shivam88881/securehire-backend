package com.curtin.securehire.entity.db;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Range {
    private double min;
    private double max;
    
    public Range(double min, double max) {
    	this.min =min;
    	this.max = max;
    }
}
