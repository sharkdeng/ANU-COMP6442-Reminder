package com.anu.dolist;

public enum Constants {

    TAG("Shark"),
    LAT("latitude"),
    LON("longititude");


    private String name;

    Constants(String name){
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
