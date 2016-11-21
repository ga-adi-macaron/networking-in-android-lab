package com.justinwells.networkinglab;

/**
 * Created by justinwells on 11/17/16.
 */

public class CustomObject {
    private String name;

    public CustomObject(String name) {
        this.name = name;
    }

    public String getText() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
