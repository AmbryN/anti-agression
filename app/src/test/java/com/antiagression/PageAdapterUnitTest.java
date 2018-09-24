package com.antiagression;


import android.support.v4.app.FragmentManager;

import com.antiagression.Adapters.PageAdapter;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PageAdapterUnitTest
{
    @Test
    public void onCreatePageAdapter(){
        FragmentManager manager = mock(FragmentManager.class);
        String [] tabsNames = {"Bonjour", "comment", "ça", "va"};
        PageAdapter adapter = new PageAdapter(manager, tabsNames);

        assertEquals(tabsNames[0], adapter.getPageTitle(0));
        assertEquals(tabsNames[1], adapter.getPageTitle(1));
        assertEquals(tabsNames[2], adapter.getPageTitle(2));
        assertEquals(tabsNames[3], adapter.getPageTitle(3));
    }
}
