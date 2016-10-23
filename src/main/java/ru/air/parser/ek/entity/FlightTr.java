package ru.air.parser.ek.entity;

import java.io.Serializable;
import java.util.Date;

public class FlightTr implements Serializable {
    private String flightNumber; //рейс
    private String direction; //направление
    private String typeBC; //тип ВС
    private Date planeDate; //плановое время
    private Date factDate; //ожидаемое / фактическое время
    private String status; //статус
    private String description; //примечание

    public FlightTr(){

    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
}
