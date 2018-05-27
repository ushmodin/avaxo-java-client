package com.artmark.forward;

/**
 * @author Ushmodin N.
 * @since 04.01.2016
 */
public interface StateChangeListener {
    void stateChanged(Exchanging exchanging, int state, Object subject);
}
