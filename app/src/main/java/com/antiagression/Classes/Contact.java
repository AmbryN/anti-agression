package com.antiagression.Classes;

public class Contact {

    private String name;
    private String number;

    public Contact (){
        this.name = "";
        this.number = "";
    }

    public Contact (String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName(){
        return this.name;
    }

    public String getNumber(){
        return this.number;
    }
}
