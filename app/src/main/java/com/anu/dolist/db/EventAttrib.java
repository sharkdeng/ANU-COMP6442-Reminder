package com.anu.dolist.db;



public enum EventAttrib {

    ID("_id"),
    TITLE("title"),
    LOCATION("location"),
    DATE("date"),
    TIME("time"),
    ALERT("alert"),
    URL("url"),
    NOTES("notes"),
    COMPLETED("completed");



    private String name;

    EventAttrib(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
