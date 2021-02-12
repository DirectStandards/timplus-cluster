package org.directtruststandards.timplus.cluster.routing;

import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import lombok.Data;

/**
 * Packet routing structure for a packet that is sent to cluster nodes.
 * @author Greg Meyer
 * @since 1.0
 */
@Data
public class ClusteredPacket 
{
	private Packet packet;
	
	private JID receipient;
	
	private byte[] destNode;
}
