package com.exercise3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {

    private String name;
    private int id;
    private List<Band> groups;

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

    public List<Band> getGroups() {
        return groups;
    }

    public void setGroups(List<Band> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", groups=" + groups +
                '}';
    }

}
