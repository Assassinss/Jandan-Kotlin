package me.zsj.dan;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.zsj.dan.data.DataManager;
import me.zsj.dan.data.DownloadExecutors;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("me.zsj.dan", appContext.getPackageName());
    }

    @Test
    public void test() throws Exception {
        Context context = InstrumentationRegistry.getContext();

        DataManager manager1 = DataManager.Companion.get(context);
        DataManager manager2 = DataManager.Companion.get(context);

        assertEquals(manager1, manager2);

        DownloadExecutors executors1 = DownloadExecutors.Companion.get();
        DownloadExecutors executors2 = DownloadExecutors.Companion.get();

        assertEquals(executors1, executors2);
    }
}
