package ch.fhnw.scim.scim.util;

import java.net.InetAddress;
import java.net.URI;

public class ServerUtils {

	public static URI buildLocation(String scimId, String port, String path) throws Exception {
		return new URI("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + path + "/scim/v2/" + scimId);
	}
}
