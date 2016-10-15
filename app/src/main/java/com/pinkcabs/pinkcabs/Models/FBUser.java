package com.pinkcabs.pinkcabs.Models;

import java.util.ArrayList;

/**
 * Created by aanyajindal on 15/10/16.
 */

public class FBUser {

    String name;
    String photoId;
    String email;
    String contact;
    ArrayList<String> smsContact;
    ArrayList<String> trackIds;

    public FBUser() {
    }

    public FBUser(String name, String photoId, String email, String contact, ArrayList<String> smsContact, ArrayList<String> trackIds) {
        this.name = name;
        this.photoId = photoId;
        this.email = email;
        this.contact = contact;
        this.smsContact = smsContact;
        this.trackIds = trackIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
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

    public ArrayList<String> getSmsContact() {
        return smsContact;
    }

    public void setSmsContact(ArrayList<String> smsContact) {
        this.smsContact = smsContact;
    }

    public ArrayList<String> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(ArrayList<String> trackIds) {
        this.trackIds = trackIds;
    }
}
