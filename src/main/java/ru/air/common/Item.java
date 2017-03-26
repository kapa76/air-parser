package ru.air.common;

import java.util.List;

public class Item {

    private String group_name;
    private List<GroupItem> group_items;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public List<GroupItem> getGroup_items() {
        return group_items;
    }

    public void setGroup_items(List<GroupItem> group_items) {
        this.group_items = group_items;
    }
}
