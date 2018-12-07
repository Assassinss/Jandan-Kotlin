package me.zsj.dan.utils;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zsj
 */

public class FileUtils {

    public static File ensureDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "Pictures/Jandan");
    }

    public static File mkdirsIfNotExists() {
        File file = ensureDirectory();
        if (file.exists() || file.isDirectory()) {
            return file;
        } else {
            return null;
        }
    }

    public static File copy(final File from, final File to) {
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            input = new FileInputStream(from);
            output = new FileOutputStream(to);

            FileChannel inputChannel = input.getChannel();
            FileChannel outputChannel = output.getChannel();

            inputChannel.transferTo(0, inputChannel.size(), outputChannel);

            return to;
        } catch (IOException e) {
            return null;
        } finally {
            closeQuietly(input);
            closeQuietly(output);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
