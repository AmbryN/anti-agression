package com.antiagression.Activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import com.antiagression.Adapters.PageAdapter;
import com.antiagression.Classes.Contact;
import com.antiagression.Fragments.ContactFragment;
import com.antiagression.Fragments.ContactGetter;
import com.antiagression.Fragments.ParamsFragment;
import com.antiagression.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ContactFragment.OnContactRemovedListener, ContactGetter.OnContactSelectListener {

    @BindView(R.id.activity_main_viewpager) ViewPager pager;
    @BindView(R.id.activity_main_tablayout) TabLayout tabs;
    private static ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getPermissionsAtRuntime();
        this.configureViewPagerAndTabs();
        this.setShowWhenLocked();
        getSavedContacts();
    }

    private void configureViewPagerAndTabs(){

        ButterKnife.bind(this, this);

        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.titlePagesViewPager)));

        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    private void getPermissionsAtRuntime(){
        String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        String sendSms = Manifest.permission.SEND_SMS;

        String[] permissions = {fineLocation,
                                sendSms};

        while (ContextCompat.checkSelfPermission(this, fineLocation)== PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, sendSms) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void setShowWhenLocked(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void getSavedContacts() {
        contacts = new ArrayList<>();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        if (preferences.contains("contacts")) {
            Gson gson = new Gson();
            String savedContacts = preferences.getString("contacts", "");
            contacts = gson.fromJson(savedContacts, new TypeToken<ArrayList<Contact>>() {
            }.getType());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveContactsToPreferences(contacts);
    }

    private void saveContactsToPreferences(ArrayList<Contact> contacts){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String contactsToSave = gson.toJson(contacts);
        preferences.edit().putString("contacts", contactsToSave).apply();
    }

    @Override
    public void onRemoveContactClicked(ContactFragment contactFragment) {
        getSupportFragmentManager().beginTransaction().remove(contactFragment).commitAllowingStateLoss();
        ParamsFragment params = (ParamsFragment) getSupportFragmentManager().getFragments().get(1);

        contacts.remove(contactFragment.getContact());
        params.deleteContactFragment(contactFragment);
    }

    public void onContactSelected(ContactFragment contactFragment, Contact contact){
        contactFragment.setContact(contact);
        contacts.add(contact);
    }

    public void onBackPressed(ContactFragment contactFragment){
        getSupportFragmentManager().beginTransaction().remove(contactFragment).commit();
    }

    public static ArrayList<Contact> getContacts(){
        return contacts;
    }
}
