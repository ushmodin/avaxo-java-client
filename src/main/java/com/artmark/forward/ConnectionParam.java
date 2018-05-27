package com.artmark.forward;

/**
 * @author Ushmodin N.
 * @since 04.01.2016
 */
public class ConnectionParam {
    public static final int DEFAULT_TIMEOUT = 30000;
    private int port;
    private String host;
    private Integer connectTimeout = DEFAULT_TIMEOUT;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public String toString() {
        return "ConnectionParam{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", connectTimeout=" + connectTimeout +
                '}';
    }
}
