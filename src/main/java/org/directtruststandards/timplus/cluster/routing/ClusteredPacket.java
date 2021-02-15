package org.directtruststandards.timplus.cluster.routing;

import org.xmpp.packet.JID;

import lombok.Data;

/**
 * Packet routing structure for a packet that is sent to cluster nodes.
 * @author Greg Meyer
 * @since 1.0
 */
@Data
public class ClusteredPacket 
{
	/**
	 * Raw XML of the stanza
	 */
	private String packet;
	
	/**
	 * The recipeint of the packet
	 */
	private JID receipient;
	
	/**
	 * The cluster Node ID that the packet is destined to hit
	 */
	private byte[] destNode;
}
