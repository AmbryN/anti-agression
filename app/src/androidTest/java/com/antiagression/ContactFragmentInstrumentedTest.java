package com.antiagression;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.antiagression.Fragments.ContactFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ContactFragmentInstrumentedTest {
    @Test
    public void createContactFragment() throws Exception {
        ContactFragment contactFragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString("contact", "{name = \"Nicolas\", number = \"0606060606\"}");
        contactFragment.setArguments(args);

        assertEquals("Nicolas", contactFragment.getContact().getName());
        assertEquals("0606060606", contactFragment.getContact().getNumber());
    }
}
