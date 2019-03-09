package com.campaigns.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Campaign {
    private int id;
    private String name;
    private Status status;
    private Timestamp startDate;
    private Timestamp endDate;
    private List<Ad> ads;

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

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public Campaign() {
    }

    public Campaign(String name, Status status, Timestamp startDate, Timestamp endDate, List<Ad> ads) {
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ads = ads;
    }
    
    
}
