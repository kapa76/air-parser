package ru.air.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Admin on 19.10.2016.
 */
public class FlightAD implements Serializable {

    private String airportId; // - ID Аэропорта (согласно кодировки IATA)
    private List<FlightDetail> arrivals; // - массив с данными о прилетах
    private List<FlightDetail> departure; // - массив с данными о прилетах

    public FlightAD(){
        arrivals = new LinkedList<FlightDetail>();
        departure = new LinkedList<FlightDetail>();
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public List<FlightDetail> getArrivals() {
        return arrivals;
    }

    public void setArrivals(List<FlightDetail> arrivals) {
        this.arrivals = arrivals;
    }

    public List<FlightDetail> getDeparture() {
        return departure;
    }

    public void setDeparture(List<FlightDetail> departure) {
        this.departure = departure;
    }
}
