package ru.air.parser.ek.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 23.10.2016.
 */
public class Routing implements Serializable {
    private String fromAirport;
    private String toAirport;

    private Date departureTime;
    private Date departureLocalTime;
    private Date arrivalTime;

    public Routing(){

    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getDepartureLocalTime() {
        return departureLocalTime;
    }

    public void setDepartureLocalTime(Date departureLocalTime) {
        this.departureLocalTime = departureLocalTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
