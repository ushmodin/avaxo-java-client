package com.artmark.forward;

import java.net.SocketAddress;

/**
 * @author Ushmodin N.
 * @since 29.01.2016 10:52
 */

public interface AuthenticationManager {
	void authentication(SocketAddress client) throws SecurityException;
	AuthenticationManager ALLOW_ALL_AUTHENTICATION_MANAGER = new AuthenticationManager() {
		@Override
		public void authentication(SocketAddress client) throws SecurityException {
		}
	};
}
