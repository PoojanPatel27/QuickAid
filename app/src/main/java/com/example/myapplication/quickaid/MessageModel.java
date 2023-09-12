package com.example.myapplication.quickaid;

public class MessageModel {

    private String userUid,name,contact,location, problem;

    public MessageModel() {
    }

    public MessageModel(String userUid, String name, String contact, String location, String message) {
        this.userUid = userUid;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.problem = message;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
