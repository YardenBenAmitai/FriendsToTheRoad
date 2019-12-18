package com.example.loginapplication;

public class NewTrip {
    public String kind;
    public String area;
    public String msg;

    public NewTrip(String k, String a){
        kind=k;
        area=a;
        msg="";
    }

    public void addMsg(String m){
        msg=m;
    }

}
