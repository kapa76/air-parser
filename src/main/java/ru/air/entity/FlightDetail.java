package ru.air.entity;

import ru.air.common.ArrivalStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 19.10.2016.
 */
public class FlightDetail implements Serializable {
    private String flightNumber; //номер рейса (согласно кодировки IATA)
    private Date scheduled;      // дата/время приземления по расписанию по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private Date estimated;      //прогнозируемые дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private Date actual;         //фактические дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private ArrivalStatus status;

    public FlightDetail() {
    }

    public FlightDetail(String flightNumber, Date scheduled, Date estimated, Date actual, ArrivalStatus status) {
        this.flightNumber = flightNumber;
        this.scheduled = scheduled;
        this.estimated = estimated;
        this.actual = actual;
        this.status = status;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date scheduled) {
        this.scheduled = scheduled;
    }

    public Date getEstimated() {
        return estimated;
    }

    public void setEstimated(Date estimated) {
        this.estimated = estimated;
    }

    public Date getActual() {
        return actual;
    }

    public void setActual(Date actual) {
        this.actual = actual;
    }

    public ArrivalStatus getStatus() {
        return status;
    }

    public void setStatus(ArrivalStatus status) {
        this.status = status;
    }
}
