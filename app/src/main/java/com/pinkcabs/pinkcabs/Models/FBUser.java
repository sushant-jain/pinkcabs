package com.pinkcabs.pinkcabs.Models;

import java.util.ArrayList;

/**
 * Created by aanyajindal on 15/10/16.
 */

public class FBUser {

    String name;

    String email;
    String contact;

    String trustedContact;

    public FBUser() {
    }

    public FBUser(String name, String email, String contact, String trustedContact) {
        this.name = name;

        this.email = email;
        this.contact = contact;
        this.trustedContact = trustedContact;
    }


    public String getTrustedContact() {
        return trustedContact;
    }

    public void setTrustedContact(String trustedContact) {
        this.trustedContact = trustedContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


}
