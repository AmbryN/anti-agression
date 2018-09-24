package com.antiagression;

import android.location.Location;

import com.antiagression.Classes.Position;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PositionUnitTest {

    @Test
    public void createPosition() throws Exception{

        Location location = mock(Location.class);
        Position position = new Position(location, "addressastext");

        assertEquals(location, position.getLocation());
        assertEquals("addressastext", position.getAddressAsText());
    }
}
