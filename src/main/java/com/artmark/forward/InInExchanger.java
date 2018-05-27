package com.artmark.forward;

import com.artmark.util.IOUtils;
import com.artmark.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Ushmodin N.
 * @since 04.01.2016
 */
public class InInExchanger implements Exchanging {
    private static final Logger log = LoggerFactory.getLogger(InInExchanger.class);
    public static final int ERROR = -1;
    public static final int STARTED = 0;
    public static final int CLIENT_LISTEN = 1;
    public static final int AGENT_LISTEN = 2;
    public static final int CLIENT_CONNECTED = 3;
    public static final int AGENT_CONNECTED = 4;
    public static final int CLOSED = 5;

    private StateChangeListener listener;
    private ServerSocket clientServerSocket;
    private ServerSocket agentServerSocket;
    private final ExecutorService executor;
    private final ConnectionParam clientParam;
    private final ConnectionParam agentParam;
    private final int readTimeout;
    private final AuthenticationManager authenticationManager;

    public InInExchanger(ExecutorService executor, ConnectionParam clientParam, ConnectionParam agentParam, int readTimeout, AuthenticationManager authenticationManager) {
        this.executor = executor;
        this.clientParam = clientParam;
        this.agentParam = agentParam;
        this.readTimeout = readTimeout;
        this.authenticationManager = authenticationManager;
    }

    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        emitListener(STARTED, null);
        Thread.currentThread().setName(this.getClass().getSimpleName()
                + ": agent = " + agentParam
                + " client = " + clientParam
                + " timeout = " + readTimeout);

        try {
            log.debug("Starting client server socket {}", clientParam);
            clientServerSocket = getServerSocket(clientParam);
        } catch (IOException e) {
            log.error("Start client server error", e);
            emitListener(ERROR, null);
            IOUtils.close(this);
            return;
        }
        try {
            log.debug("Starting agent server socket {}", agentParam);
            agentServerSocket = getServerSocket(agentParam);
        } catch (IOException e) {
            log.error("Start agent server error", e);
            emitListener(ERROR, e);
            IOUtils.close(this);
            return;
        }

        while (!Thread.interrupted()) {
            try {
                emitListener(CLIENT_LISTEN, null);
                final Socket clientSocket = clientServerSocket.accept();
                log.debug("Client connected from {}", clientSocket.getRemoteSocketAddress());

                checkAuthentication(clientSocket);

                emitListener(CLIENT_CONNECTED, clientSocket.getRemoteSocketAddress());

                if (!clientSocket.isClosed()) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Socket agentSocket = null;
                            try {
                                emitListener(AGENT_LISTEN, null);
                                agentSocket = agentServerSocket.accept();
                                log.debug("Agent connected from {}", agentSocket.getRemoteSocketAddress());
                                emitListener(AGENT_CONNECTED, agentSocket.getRemoteSocketAddress());

                                Future<?> submit = executor.submit(new SocketExchanger(agentSocket, clientSocket));
                                new SocketExchanger(clientSocket, agentSocket).run();
                                submit.get();
                                log.debug("End of client-agent communication {} - {}", clientParam, agentParam);
                            } catch (Exception e) {
                                log.error(null, e);
                                emitListener(ERROR, e);
                            } finally {
                                log.trace("Closing sockets {} - {}", clientSocket, agentSocket);
                                IOUtils.close(clientSocket);
                                IOUtils.close(agentSocket);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                log.error(null, e);
                IOUtils.close(this);
                Thread.currentThread().interrupt();
            }
        }
        emitListener(CLOSED, null);
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(clientServerSocket);
        IOUtils.close(agentServerSocket);
    }


    private void emitListener(int state, Object subject) {
        if (listener != null) {
            listener.stateChanged(this, state, subject);
        }
    }

    private ServerSocket getServerSocket(ConnectionParam param) throws IOException {
        ServerSocket socket = new ServerSocket(param.getPort(), 0, InetAddress.getByName(param.getHost() != null ? param.getHost() : "0.0.0.0"));
        socket.setSoTimeout(Utils.coalesce(param.getConnectTimeout(), 0));
        return socket;
    }

    private void checkAuthentication(Socket clientSocket) {
        if (authenticationManager != null) {
            try {
                authenticationManager.authentication(clientSocket.getRemoteSocketAddress());
            } catch (SecurityException e) {
                IOUtils.close(clientSocket);
            }
        }
    }
}
