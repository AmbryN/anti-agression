package com.antiagression.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.antiagression.Classes.Contact;
import com.antiagression.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactFragment extends Fragment {

    public interface OnContactRemovedListener {
        void onRemoveContactClicked(ContactFragment contactFragment);
    }

    @BindView(R.id.fragment_contact_name) TextView contactName;
    @BindView(R.id.fragment_contact_number) TextView number;
    @BindView(R.id.fragment_contact_remove_button) Button removeButton;

    private Contact contact;
    private OnContactRemovedListener contactRemovedCallbackToMainActivity;
    private ContactFragment contactFragment = this;

    public ContactFragment() {
    }

    public static ContactFragment newInstance(Contact contact){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.contact = contact;
        return contactFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactRemovedCallbackToMainActivity.onRemoveContactClicked(contactFragment);
            }
        });

        if (contact == null) {
            getContactInfo();
        } else  {
            setContactViews(contact);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            contactRemovedCallbackToMainActivity = (OnContactRemovedListener) context;

        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnContactRemovedListener / OnContactAddedListener");
        }
    }

    private void getContactInfo(){
        ContactGetter contactGetter = new ContactGetter();
        contactGetter.setTargetFragment(this, 1);
        getFragmentManager().beginTransaction()
                .add(contactGetter, "contactgetter")
                .commit();
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        setContactViews(contact);
    }

    private void setContactViews(Contact contact){
        this.contactName.setText(contact.getName());
        this.number.setText(contact.getNumber());
    }

    public Contact getContact(){
       return this.contact;
    }
}
