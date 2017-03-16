package org.bicsi.canada2014;

/**
 * Created by barry on 6/4/15.
 */
public class Planner {

    public int _id;
    public String code;
    public String title;
    public String desc;
    public String location;
    public String date;
    public String start;
    public String end;

    public Planner(int id, String code, String title, String desc, String location, String date, String start, String end){

        this._id = id;
        this.code = code;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.date = date;
        this.start = start;
        this.end = end;

    }

    public Planner(){


    }

}
