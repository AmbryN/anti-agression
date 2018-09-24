package com.antiagression.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.antiagression.Fragments.AlertFragment;
import com.antiagression.Fragments.ParamsFragment;


public class PageAdapter extends FragmentPagerAdapter {

    private String[] tabNames;

    public PageAdapter(FragmentManager mgr, String[] tabNames){
        super(mgr);
        this.tabNames = tabNames;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AlertFragment(); //Pas possible avec newInstance car pas de méthode static !
            case 1:
                return new ParamsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
