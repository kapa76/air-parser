package ru.air.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.air.common.ArrivalStatus;

import java.io.Serializable;
import java.util.List;

public class FlightDetail implements Serializable {
    private String flightNumber; //номер рейса (согласно кодировки IATA)

    @JsonProperty("codeshares")
    private List<String> codeShares;

    private String scheduled;      // дата/время приземления по расписанию по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private String estimated;      //прогнозируемые дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private String actual;         //фактические дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
    private ArrivalStatus status;

    public FlightDetail() {
        this.status = ArrivalStatus.SCHEDULED;
        this.scheduled = "";
        this.estimated = "";
        this.actual = "";
    }

    public FlightDetail(String flightNumber, String scheduled, String estimated, String actual, ArrivalStatus status) {
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

    public ArrivalStatus getStatus() {
        return status;
    }

    public void setStatus(ArrivalStatus status) {
        this.status = status;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getEstimated() {
        return estimated;
    }

    public void setEstimated(String estimated) {
        this.estimated = estimated;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public List<String> getCodeShares() {
        return codeShares;
    }

    public void setCodeShares(List<String> codeShares) {
        this.codeShares = codeShares;
    }
}
