package com.shreebrahmanitravels.app;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class user_helper {

    String name,username,phoneNo,password,bus_no;
ChipNavigationBar chipNavigationBar;
    public user_helper() {
    }

    public user_helper(String name, String username, String phoneNo, String password, String bus_no) {
        this.name = name;
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
        this.bus_no = bus_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }


}
