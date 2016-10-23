package ru.air.parser.ek.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 23.10.2016.
 */
public class Routing implements Serializable {
    private String airportDestination;

    private Date delayTime;
    private Date delayLocaltime;

    public Routing(){

    }

    public Routing(String airportName, Date currDateTime, Date localDateTime) {
        this.airportDestination = airportName;
        this.delayTime = currDateTime;
        this.delayLocaltime = localDateTime;
    }

    public Date getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Date delayTime) {
        this.delayTime = delayTime;
    }

    public Date getDelayLocaltime() {
        return delayLocaltime;
    }

    public void setDelayLocaltime(Date delayLocaltime) {
        this.delayLocaltime = delayLocaltime;
    }

    public String getAirportDestination() {
        return airportDestination;
    }

    public void setAirportDestination(String airportDestination) {
        this.airportDestination = airportDestination;
    }
}
