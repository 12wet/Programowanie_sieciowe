package com.exercise3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Band {

    private String name;
    private int id;
    private List<DTOMember> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DTOMember> getMembers() {
        return members;
    }

    public void setMembers(List<DTOMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Band{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", members=" + members +
                '}';
    }
}
