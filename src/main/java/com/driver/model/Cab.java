package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Cab{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int perKmRate;
    private boolean available;
    @OneToOne
    @JoinColumn
    private Driver driver;

    public Cab(int id, int rate, boolean status, Driver driver){
        this.id= id;
        this.perKmRate= rate;
        this.available= status;
        this.driver= driver;
    }

    public Cab(int perKmRate, boolean available, Driver driver) {
        this.perKmRate = perKmRate;
        this.available = available;
        this.driver= driver;
    }
    public int getId(){
        return id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}