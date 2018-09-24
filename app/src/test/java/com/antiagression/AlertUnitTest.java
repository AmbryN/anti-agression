package com.antiagression;

import android.content.Context;

import com.antiagression.Classes.Alert;
import com.antiagression.Classes.Position;

import org.junit.Test;


import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class AlertUnitTest {

    Position position = mock(Position.class);
    Context context = mock(Context.class);

    @Test
    public void newInstanceAlert() {
        Alert alert = Alert.newInstance(context, position);

        assertEquals(position, alert.getEventPosition());
        assertNotNull(alert.getEventDate());
    }
}
