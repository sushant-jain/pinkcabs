package com.pinkcabs.pinkcabs.Models;

/**
 * Created by aanyajindal on 15/10/16.
 */

public class FBDriver {

    String name;
    String photoLink;
    String email;
    String contact;
    String cabNo;
    String fcmId;

    public FBDriver() {
    }

    public FBDriver(String name, String photoLink, String email, String contact, String cabNo, String fcmId) {
        this.name = name;
        this.photoLink = photoLink;
        this.email = email;
        this.contact = contact;
        this.cabNo = cabNo;
        this.fcmId = fcmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
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

    public String getCabNo() {
        return cabNo;
    }

    public void setCabNo(String cabNo) {
        this.cabNo = cabNo;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }
}
