/**
 * This file is a part of Angry IP Scanner source code,
 * see http://www.azib.net/ for more information.
 * Licensed under GPLv2.
 */

package net.azib.ipscan.core.net;

import java.io.IOException;
import java.net.InetAddress;

/**
 * CombinedUnprivilegedPinger - uses both UDP and TCP for pinging.
 * A better default alternative for unprivileged users.
 *
 * @author Anton Keks
 */
public class CombinedUnprivilegedPinger implements Pinger {

	private TCPPinger tcpPinger;
	private UDPPinger udpPinger;
	
	public CombinedUnprivilegedPinger(int timeout) {
		udpPinger = new UDPPinger(timeout);
		tcpPinger = new TCPPinger(timeout);
	}

	public PingResult ping(InetAddress address, int count) throws IOException {
		// try UDP first - it should be more reliable in general
		PingResult result = udpPinger.ping(address, count);
		if (!result.isAlive()) {
			// fallback to TCP - it may detect some hosts UDP cannot
			result = tcpPinger.ping(address, count);
		}
		return result;
	}

	public void close() throws IOException {
		udpPinger.close();
		tcpPinger.close();
	}
}
