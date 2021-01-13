package com.example.exlog;

public class UserProfile {

    String userName, phone,  userEmail,name;

    public UserProfile(){
    }

    public UserProfile(String userName,String phone, String userEmail, String  name) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userName = userName;
        this.phone=phone;
    }

    public String getname() {
        return name;
    }

    public void setName(String userAge) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return userName;
    }

    public void setPhone(String userName) {
        this.userName = userName;
    }

}
