package ru.air.parser.ek.entity;

import java.io.Serializable;
import java.util.Date;

public class FlightTr implements Serializable {
    private String flightNumber; //рейс
    private String directionFrom; //направление
    private String typeBC; //тип ВС
    private Date planeDate; //плановое время
    private Date factDate; //ожидаемое / фактическое время
    private String status; //статус
    private String description; //примечание
    private String baggage;

    public FlightTr() {

    }

    public FlightTr(String flightNumber, String directionFrom, String typeBC, Date planeDate, Date factDate, String status, String description) {
        this.flightNumber = flightNumber;
        this.directionFrom = directionFrom;
        this.typeBC = typeBC;
        this.planeDate = planeDate;
        this.factDate = factDate;
        this.status = status;
        this.description = description;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDirectionFrom() {
        return directionFrom;
    }

    public void setDirectionFrom(String directionFrom) {
        this.directionFrom = directionFrom;
    }

    public String getTypeBC() {
        return typeBC;
    }

    public void setTypeBC(String typeBC) {
        this.typeBC = typeBC;
    }

    public Date getPlaneDate() {
        return planeDate;
    }

    public void setPlaneDate(Date planeDate) {
        this.planeDate = planeDate;
    }

    public Date getFactDate() {
        return factDate;
    }

    public void setFactDate(Date factDate) {
        this.factDate = factDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }
}
