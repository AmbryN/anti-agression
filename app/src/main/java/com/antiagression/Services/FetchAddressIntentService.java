package com.antiagression.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.antiagression.Classes.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver receiver;

    public FetchAddressIntentService(){
    super("addressfetcher");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String errorMessage = "";
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        //On crée une instance de géocoder permettant de récupérer une adresse
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //On récupère la localisation passé par l'intermédiaire d'un Extra de l'intent
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        //On crée une liste d'adresse pour récupérer l'adresse de la localisation
        List<Address> addresses = null;

        try { //On récupère une seule adresse correspondante
            addresses = geocoder
                    .getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException){ //On catch les erreurs réseau ou d'entrée/sortie
            errorMessage = "Impossible d'accéder au service";
            Log.e("IOException", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException){ //On catch les latitudes/latitude incorrectes
            errorMessage = "Latitude / Longitude incorrectes";
            Log.e("IllegalArgument", errorMessage+" : "+
                    "Latitude = "+location.getLatitude()+" , "+
                    "Longitude = "+location.getLongitude(), illegalArgumentException);
        }

        //On gère les cas où on ne trouve pas d'adresse
        if (addresses == null || addresses.size() == 0){
            if(errorMessage.isEmpty()){
                errorMessage = "Pas d'adresse trouvée";
                Log.e("noadresse", errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            //On récupère les lignes de l'adresse avec getAdressLine
            //On les concatène et on les envoie
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i("adressfound", "Une adresse a été trouvée");
            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }

    public void deliverResultToReceiver(int resultCode, String message){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }
}
