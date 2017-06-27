package com.schoolke.bean;

/**
 * Created by Bragg Troy on 2017/3/20.
 */
public class User {
    private int id;
    private String userName;
    private String password;
    private String contact;
    private String school;
    private String personality;
    private String registrationDate;
    private String header;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    public User(int id, String userName, String password, String contact, String school, String personality, String registrationDate, String header) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.contact = contact;
        this.school = school;
        this.personality = personality;
        this.registrationDate = registrationDate;
        this.header = header;
    }
    public User(){}
}
