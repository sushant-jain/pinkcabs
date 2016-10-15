package com.pinkcabs.pinkcabs.Models;

/**
 * Created by jatin on 15/10/16.
 */
public class PanicContact {
    String name;
    String contact;

    public PanicContact(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public PanicContact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
