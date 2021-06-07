package com.GOF.cairn;

import java.util.List;

public class UserI {

    public String email;
    public String fLandType;  //enum
    public String homeAddr;
    public List<SavedPOI> lsFavLandmarks;
    public boolean metric;

    public boolean getMetric() {
        return metric;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }

    public UserI(String email, String fLandType, String homeAddr, List<SavedPOI> lsFavLandmarks, boolean metric) {
        this.email = email;
        this.fLandType = fLandType;
        this.homeAddr = homeAddr;
        this.lsFavLandmarks = lsFavLandmarks;
        this.metric = metric;
    }

    public UserI(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfLandType() {
        return fLandType;
    }

    public void setfLandType(String fLandType) {
        this.fLandType = fLandType;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }
}
