package com.antiagression;

import com.antiagression.Classes.Contact;

import org.junit.Test;

import static org.junit.Assert.*;


public class ContactUnitTest {
    @Test
    public void createContact() throws Exception {
        Contact contact = new Contact("Nicolas", "0606060606");

        assertEquals("Nicolas", contact.getName());
        assertEquals("0606060606", contact.getNumber());
    }
}