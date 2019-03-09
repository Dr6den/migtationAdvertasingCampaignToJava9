package com.campaigns.domain;

/**
 *
 * @author andrew
 */
public class Summary {
    private int id;
    private String name;
    private Status status;
    private int numberOfAds;

    public Summary(int id, String name, Status status, int numberOfAds) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.numberOfAds = numberOfAds;
    }

    public Summary() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getNumberOfAds() {
        return numberOfAds;
    }

    public void setNumberOfAds(int numberOfAds) {
        this.numberOfAds = numberOfAds;
    }
    
    
}
