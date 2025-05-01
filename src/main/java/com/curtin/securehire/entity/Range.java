package com.curtin.securehire.entity;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Range {
    private double min;
    private double max;
    
    public Range(double min, double max) {
    	this.min =min;
    	this.max = max;
    }
}
