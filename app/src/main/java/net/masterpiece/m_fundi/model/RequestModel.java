package net.masterpiece.m_fundi.model;

public class RequestModel {

    String service = null;
    String cost = null;

    public RequestModel(String service, String cost) {
        super();
        this.service = service;
        this.cost = cost;
    }

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }

}