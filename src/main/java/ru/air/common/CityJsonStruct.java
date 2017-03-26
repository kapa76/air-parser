package ru.air.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CityJsonStruct {

    @JsonProperty("isGrouped")
    private boolean isGrouped;

    private List<Item> items;

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setGrouped(boolean grouped) {
        isGrouped = grouped;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
