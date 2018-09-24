package com.antiagression.Classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.util.Log;

import com.antiagression.Activities.MainActivity;
import com.antiagression.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Alert {

    private Position eventPosition;
    private Date eventDate;

    public Alert (Position position){
        this.eventPosition = position;
        this.eventDate = new Date(System.currentTimeMillis());
    }

    public ArrayList<String> getRelevantInfoForDB(){
        ArrayList<String> relevantInfo = new ArrayList<>();
        Position position = getEventPosition();
        Date date = getEventDate();

        String year, month, day, hours, minutes, seconds;
        year = Integer.toString(date.getYear()+1900);
        month = Integer.toString(date.getMonth()+1);
        day = Integer.toString(date.getDate());
        hours = Integer.toString(date.getHours());
        minutes = Integer.toString(date.getMinutes());
        seconds = Integer.toString(date.getSeconds());
        String dateAsString = year +'-'+month+'-'+day+' '+hours+':'+minutes+':'+seconds;

        String longitude = Double.toString(position.getLocation().getLongitude());
        String latitude = Double.toString(position.getLocation().getLatitude());
        String address = position.getAddressAsText();

        relevantInfo.add(dateAsString);
        relevantInfo.add(longitude);
        relevantInfo.add(latitude);
        relevantInfo.add(address);

        return relevantInfo;
    }

    public Position getEventPosition(){
        return eventPosition;
    }

    public Date getEventDate(){
        return eventDate;
    }

}
