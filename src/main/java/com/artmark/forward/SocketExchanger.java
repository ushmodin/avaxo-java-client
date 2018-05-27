package com.artmark.forward;

import com.artmark.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ushmodin N.
 * @since 04.01.2016
 */
public class SocketExchanger implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(SocketExchanger.class);
    public static final int ONCLOSE_TIMEOUT = 5000;

    private final Socket inputSocket;
    private final Socket outputSocket;

    public SocketExchanger(Socket inputSocket, Socket outputSocket) {
        this.inputSocket = inputSocket;
        this.outputSocket = outputSocket;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.getClass().getSimpleName()
                + ": from " + inputSocket.getInetAddress() + ":" + inputSocket.getPort()
                + " to: " + outputSocket.getInetAddress() + ":" + outputSocket.getPort());

        try {
            log.debug("Start copy data from {}:{} to {}:{}", inputSocket.getInetAddress(), inputSocket.getPort(), outputSocket.getInetAddress(), outputSocket.getPort());

            long total = IOUtils.copy(inputSocket.getInputStream(), outputSocket.getOutputStream());

            log.debug("End copy data from {}:{} to {}:{} transfered {} bytes", inputSocket.getInetAddress(), inputSocket.getPort(), outputSocket.getInetAddress(), outputSocket.getPort(), total);
        } catch (IOException e) {
            log.error("Socket data copy error", e);
        } finally {
            try {
                log.debug("Shutdown output");
                outputSocket.shutdownOutput();
                outputSocket.setSoTimeout(ONCLOSE_TIMEOUT);
            } catch (IOException e) {
            }
        }
    }
}
