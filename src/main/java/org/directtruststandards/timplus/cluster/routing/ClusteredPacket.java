package org.directtruststandards.timplus.cluster.routing;

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
	 * The local part of the recipeint of the packet
	 */
	private String recipLocal;
	
	/**
	 * The domain part of the recipeint of the packet
	 */
	private String recipDomain;	
	
	/**
	 * The resource part of the recipeint of the packet
	 */
	private String recipResource;		
	
	/**
	 * The cluster Node ID that the packet is destined to hit
	 */
	private byte[] destNode;
}
