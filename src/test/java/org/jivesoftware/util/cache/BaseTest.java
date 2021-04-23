package org.jivesoftware.util.cache;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.cluster.NodeID;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class BaseTest 
{
	@BeforeAll
	public static void beforeEach()
	{
		XMPPServer server = mock(XMPPServer.class);
		
		when(server.getNodeID()).thenReturn(NodeID.getInstance(new byte[] {0,0,0,0}));
		
		MockedStatic<XMPPServer> theMock = Mockito.mockStatic(XMPPServer.class);
		
		theMock.when(XMPPServer::getInstance).thenReturn(server);
	}
}
