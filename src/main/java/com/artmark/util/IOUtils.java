package com.artmark.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Ushmodin N.
 * @since 05.01.2016
 */
public class IOUtils {

    public static final int EOF = -1;
    public static final int BUFFER_SIZE = 1024 * 4;

    public static long copy(InputStream input, OutputStream output) throws IOException {
        byte []buffer = new byte[BUFFER_SIZE];
        int n;
        long count = 0;
        while ((n = input.read(buffer)) != EOF) {
            output.write(buffer, 0, n);
            output.flush();
            count += n;
        }
        return count;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(Socket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
