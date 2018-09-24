package com.antiagression.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.antiagression.Activities.MainActivity;
import com.antiagression.Classes.Alert;
import com.antiagression.Classes.Constants;
import com.antiagression.Classes.Contact;
import com.antiagression.Classes.Position;
import com.antiagression.Services.FetchAddressIntentService;
import com.antiagression.R;
import com.antiagression.Services.SaveAlertToDBIntentService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlertFragment extends Fragment {

    @BindView(R.id.fragment_alert_button) ImageButton button;

    private FusedLocationProviderClient fusedLocation;
    private Location lastKnownLocation;
    private AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler());

    public AlertFragment() {
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_alert, container, false);
         ButterKnife.bind(this, view);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showAlertDialog();
             }
         });
         return view;
    }

    private void showAlertDialog(){
        final AlertDialog dialog = new AlertDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "alert_dialog_fragment");
        dialog.setCountDownToAlertDialog();
    }

    public void fetchAdress(){
        fusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            fusedLocation.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            lastKnownLocation = location;

                            if (lastKnownLocation == null) {
                                return;
                            }
                            if (!Geocoder.isPresent()){
                                Toast.makeText(getActivity(),
                                        R.string.fragment_alert_missing_geocoder,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (thereAreSavedContacts()) {
                                startFetchAddressIntentService();
                            } else {
                                Toast.makeText(getActivity(), R.string.fragment_alert_no_contact,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean thereAreSavedContacts(){
        return !MainActivity.getContacts().isEmpty();
    }

    protected void startFetchAddressIntentService(){
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastKnownLocation);
        getContext().startService(intent);
    }

    //Classe interne qui récupère une adresse demandé à FetchAddressIntentService
    class AddressResultReceiver extends ResultReceiver implements Parcelable{
        private String addressAsText;

        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            addressAsText = resultData.getString(Constants.RESULT_DATA_KEY);
            Position actualPosition = new Position(lastKnownLocation, addressAsText);

            if(resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(getActivity(), R.string.fragment_alert_address_found,
                        Toast.LENGTH_LONG).show();
                Alert alert = new Alert (actualPosition);
                sendSMS(actualPosition);

                startSaveAlertToDBService(alert);
            } else Toast.makeText(getActivity(), R.string.fragment_alert_address_notfound,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void sendSMS(Position position){
        SmsManager sms = SmsManager.getDefault();

        String messageToSend = generateMessageToSend(position.getAddressAsText());
        for (Contact contact : MainActivity.getContacts()) {
            String number = contact.getNumber();
            sms.sendTextMessage(number, null, messageToSend,
                    null, null);
        }
    }

    private String generateMessageToSend(String address){
        String mapsParameters = generateMapsParametersFromAddress(address);
        return getString(R.string.fragment_alert_alertmsg)+ " https://www.google.com/maps/search/?api=1&query="+ mapsParameters;
    }

    private String generateMapsParametersFromAddress(String address){
        String addressWithoutCommas = address.replace(",", "");
        String mapsParameters = addressWithoutCommas.replace(" ", "+");
        return mapsParameters;
    }

    private void startSaveAlertToDBService(Alert alert){
        Intent intent = new Intent(getActivity(), SaveAlertToDBIntentService.class);
        Gson gson = new Gson();
        String alertAsJson = gson.toJson(alert);
        intent.putExtra("alert", alertAsJson);
        getContext().startService(intent);
        Log.d("saveDB", "startSaveAlertToDBService: ");

    }
}
