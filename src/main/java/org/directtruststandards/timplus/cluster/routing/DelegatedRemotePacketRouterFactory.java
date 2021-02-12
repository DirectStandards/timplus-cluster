package org.directtruststandards.timplus.cluster.routing;

import org.jivesoftware.openfire.RemotePacketRouter;

/**
 * Class factory used for delegating the creation of a RemotePacketRouter.
 * @author Greg Meyer
 * @since 1.0
 */
public interface DelegatedRemotePacketRouterFactory 
{
	/**
	 * Gets an instance of the RemotePacketRouter backed by a specific implementation of the RemotePacketRouter.
	 * In many cases, the implementation of DelegatedRemotePacketRouterFactory will use a singleton instance model
	 * and return the same instance.  RemotePacketRouter implementations should be thread safe.
	 * @return An instance of a RemotePacketRouter.
	 */
	public RemotePacketRouter getInstance();
}
