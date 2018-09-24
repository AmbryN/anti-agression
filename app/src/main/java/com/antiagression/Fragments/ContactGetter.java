package com.antiagression.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import com.antiagression.Classes.Constants;
import com.antiagression.Classes.Contact;

import static android.app.Activity.RESULT_OK;

public class ContactGetter extends Fragment {

    public interface OnContactSelectListener {
        void onContactSelected(ContactFragment contactFragment, Contact contact);
        void onBackPressed(ContactFragment contactFragment);
    }

    private Contact contact;
    private OnContactSelectListener selectedContactCallback;

    public ContactGetter() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            selectedContactCallback = (OnContactSelectListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getTargetFragment().toString() + " must implement OnSelectedContactListener");
        }
        selectContactInRepertoire();
    }

    public void selectContactInRepertoire(){
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        //Montre les contacts qui ont un numéro de téléphone
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, Constants.PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PICK_CONTACT_REQUEST){ //On vérifie que c'est l'appli contact qui répond
            if (resultCode == RESULT_OK) { //On vérifie que la requete s'est bien passée
                String contactName;
                String number;

                //On récupère l'Uri qui pointe vers le contact sélectionné
                Uri contactUri = data.getData();
                //On ne veut que la colonne "Numéro" de notre requete
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                //On fait la requete du numéro sur l'Uri obtenue
                Cursor cursor = getActivity().getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                //On récupère le numéro de téléphone de la colonne "Numero"
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                number = cursor.getString(column);

                //On récupère le nom du contact dans la colonne "Nom"
                column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                contactName = cursor.getString(column);

                contact = new Contact(contactName, number);
                selectedContactCallback.onContactSelected((ContactFragment) getTargetFragment(), contact);
                getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            } else {
                selectedContactCallback.onBackPressed((ContactFragment)getTargetFragment());
                getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        selectedContactCallback = null;
    }
}
