package com.mariano.itunestopfreeapplications.models.events;

/**
 * Created by mariano on 11/01/17.
 */

public class onFailEvent {
    String message;
    public onFailEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
