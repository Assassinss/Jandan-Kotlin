package me.zsj.dan;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author zsj
 */

public class UnitTest {

    @Test
    public void testDateRelativeTimeSpan() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        Date date = sdf.parse("2017-07-02 20:40:05");
        Date currentTime = Calendar.getInstance().getTime();
        long diff = date.getTime() - currentTime.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        assertEquals(2, Math.abs(days));
    }
}
