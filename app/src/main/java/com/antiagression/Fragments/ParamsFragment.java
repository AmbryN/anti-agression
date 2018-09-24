package com.antiagression.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.antiagression.Activities.MainActivity;
import com.antiagression.Classes.Contact;
import com.antiagression.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ParamsFragment extends Fragment {

    @BindView(R.id.fragment_params_add_button) Button addContactButton;

    private int numberOfContacts = 0;

    public ParamsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_params, container, false);
        ButterKnife.bind(this, view);

        generateContactFragmentsFromContacts();
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewContact();
            }
        });

        return view;
    }

    private void generateContactFragmentsFromContacts() {
        for (Contact contact : MainActivity.getContacts()) {
            if (contact != null) {
                ContactFragment contactFragment = ContactFragment.newInstance(contact);
                showContact(contactFragment);
            }
        }
    }
    
    private void showContact(ContactFragment contactFragment) {
        if (!paramsFragmentContains(contactFragment)) {
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_params, contactFragment)
                    .commit();
        }
    }

    private boolean paramsFragmentContains(ContactFragment contactFragment){
            return getFragmentManager().getFragments().contains(contactFragment);
    }

    private void addNewContact(){
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setTargetFragment(this, 1);
        showContact(contactFragment);
        numberOfContacts++;
    }

    public void deleteContactFragment(ContactFragment contactFragment){
        getFragmentManager().beginTransaction().remove(contactFragment).commitAllowingStateLoss();
        numberOfContacts--;

        if (numberOfContacts < 4){
            addContactButton.setVisibility(View.VISIBLE);
        }
    }
}
