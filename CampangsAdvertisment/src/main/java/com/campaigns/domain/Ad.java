package com.campaigns.domain;

import java.util.List;

/**
 *
 * @author andrew
 */
public class Ad {
    private int id;
    private String name;
    private List<Platform> platforms;
    private String asserUrl;
    private Status status;

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

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public String getAsserUrl() {
        return asserUrl;
    }

    public void setAsserUrl(String asserUrl) {
        this.asserUrl = asserUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Ad(int id, String name, List<Platform> platforms, String asserUrl, Status status) {
        this.id = id;
        this.name = name;
        this.platforms = platforms;
        this.asserUrl = asserUrl;
        this.status = status;
    }

    public Ad() {
    }
    
    
}
