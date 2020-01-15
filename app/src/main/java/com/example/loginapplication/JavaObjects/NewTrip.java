package com.example.loginapplication.JavaObjects;

public class NewTrip {
    //use UserUid() as string to identify users
    public String creator;
    public String participants= "";
    public String kind;
    public String area;
    public String msg;
    public boolean open;

    public NewTrip(String c, String k, String a, String m){
        creator=c;
        kind=k;
        area=a;
        msg=m;
        open=true;
    }



}
