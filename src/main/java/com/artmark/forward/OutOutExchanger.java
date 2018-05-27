package com.artmark.forward;

import com.artmark.util.IOUtils;
import com.artmark.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Ushmodin N.
 * @since 05.01.2016
 */
public class OutOutExchanger implements Exchanging {
    public static final int ERROR = -1;
    public static final int CLOSED = 0;
    public static final int TARGET_CONNECTED = 1;
    public static final int MANAGER_CONNECTED = 2;
    public static final int STARTED = 3;

    private static final Logger log = LoggerFactory.getLogger(OutOutExchanger.class);
    private StateChangeListener listener;
    private final ExecutorService executor;
    private final ConnectionParam target;
    private final ConnectionParam manager;
    private final int readTimeout;
    private Socket targetSocket;
    private Socket managerSocket;

    public OutOutExchanger(ExecutorService executor, ConnectionParam target, ConnectionParam manager, int readTimeout) {
        this.executor = executor;
        this.target = target;
        this.manager = manager;
        this.readTimeout = readTimeout;
    }


    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void close() throws IOException {
        log.trace("Closing sockets {} - {}", targetSocket, managerSocket);
        IOUtils.close(targetSocket);
        IOUtils.close(managerSocket);
    }

    @Override
    public void run() {
        emitListener(STARTED, null);
        try {
            targetSocket = new Socket();
            targetSocket.connect(new InetSocketAddress(target.getHost(), target.getPort()), Utils.coalesce(target.getConnectTimeout(), 0));
            log.debug("Connected to target {}", target);
            emitListener(TARGET_CONNECTED, null);

            managerSocket = new Socket();
            managerSocket.connect(new InetSocketAddress(manager.getHost(), manager.getPort()), Utils.coalesce(manager.getConnectTimeout(), 0));
            log.debug("Connected to manager {}", manager);
            emitListener(MANAGER_CONNECTED, null);

            Future<?> future = executor.submit(new SocketExchanger(targetSocket, managerSocket));
            new SocketExchanger(managerSocket, targetSocket).run();
            future.get();
        } catch (Exception e) {
            log.error(null, e);
            emitListener(ERROR, e);
        } finally {
            IOUtils.close(this);
        }
        emitListener(CLOSED, null);
    }

    private void emitListener(int state, Object subject) {
        if (listener != null) {
            listener.stateChanged(this, state, subject);
        }
    }

    public ConnectionParam getTarget() {
        return target;
    }

    public ConnectionParam getManager() {
        return manager;
    }

    public int getReadTimeout() {
        return readTimeout;
    }
}
