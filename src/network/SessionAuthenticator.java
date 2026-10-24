/* Name: SessionAuthenticator
 * Description: This class validates the session ID of network requests.
 */

package network;

/**
 * The {@code SessionAuthenticator} class checks if a request was sent by the
 * remote client that is connected to a session.
 */
public class SessionAuthenticator {

	/**
	 * Checks if the request has the session ID of the specified session.
	 *
	 * @param request	the request to check.
	 * @param session	the session the request should belong to.
	 * @return true if the session IDs match.
	 */
	public boolean isAuthenticated(NetworkRequest request, Session session) {
		return request.getSid().equals(session.getSid());
	}
}
