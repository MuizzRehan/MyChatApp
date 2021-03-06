package com.example.admin.mychatapp;

public class Users {
    String name;
    String image;
    String status;
    String thumb_image;

    public Users() {
    }

    public Users(String name, String image, String status, String thumb_image) {

        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbImage() {
        return thumb_image;
    }

    public void setThumbImage(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
