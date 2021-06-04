package com.GOF.cairn;

import java.util.Date;

public class UserI {

    public String email;
    public String favLandmark;  //enum
    public String homeAddr;
    public boolean metric;

    public boolean getMetric() {
        return metric;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }

    public UserI(String email, String favLandmark, String homeAddr, boolean metric) {
        this.email = email;
        this.favLandmark = favLandmark;
        this.homeAddr = homeAddr;
        this.metric = metric;
    }
    public UserI(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFavLandmark() {
        return favLandmark;
    }

    public void setFavLandmark(String favLandmark) {
        this.favLandmark = favLandmark;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }
}
