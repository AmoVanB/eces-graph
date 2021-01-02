package de.tum.ei.lkn.eces.graph;

import de.tum.ei.lkn.eces.core.Controller;
import de.tum.ei.lkn.eces.core.MapperSpace;
import de.tum.ei.lkn.eces.core.util.EventCountTestSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Test class for de.tum.ei.lkn.eces.graph.GraphSystem.java.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */
public class GraphSystemTest {
	private Controller controller;
	private GraphSystem graphSystem;
	private EventCountTestSystem eventTestSystem;


	@Before
	public void setup() {
		controller = new Controller();
		graphSystem = new GraphSystem(controller);
		eventTestSystem = new EventCountTestSystem(controller);
	}

	@Test
	public final void testCreateGraph() {
		Graph g = graphSystem.createGraph();

		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 0", 0, g.getNodes().size());

		eventTestSystem.doFullCheck(Graph.class,1,0,0);
		eventTestSystem.doFullCheck(Node.class,0,0,0);
		eventTestSystem.doFullCheck(Edge.class,0,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testDeleteGraph() {
		Node[] n = new Node[2];
		Graph g = graphSystem.createGraph();
		n[0] = graphSystem.createNode(g);
		n[1] = graphSystem.createNode(g);
		Edge e = graphSystem.createEdge(n[0],n[1]);

		eventTestSystem.reset();

		graphSystem.deleteGraph(g);

		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 0", 0, g.getNodes().size());

		assertFalse("Graph still stores the node", g.getNodes().contains(n[0]));
		assertFalse("Graph still stores the node", g.getNodes().contains(n[1]));

		assertFalse("Graph still stores the edge", g.getEdges().contains(e));

		eventTestSystem.doFullCheck(Graph.class,0,3,1);
		eventTestSystem.doFullCheck(Node.class,0,2,2);
		eventTestSystem.doFullCheck(Edge.class,0,0,1);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testCreateNodeGraph() {
		Graph g = graphSystem.createGraph();
		Node n  = graphSystem.createNode(g);

		assertEquals("The name of the created Node should be empty", 0, n.getName().compareTo(""));
		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 1", 1, g.getNodes().size());
		assertTrue("Graph does not store the right node", g.getNodes().contains(n));

		eventTestSystem.doFullCheck(Graph.class,1,1,0);
		eventTestSystem.doFullCheck(Node.class,1,0,0);
		eventTestSystem.doFullCheck(Edge.class,0,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testCreateNodeWithNameGraph() {
		Graph g = graphSystem.createGraph();
		Node n  = graphSystem.createNode(g, "PLC");

		assertEquals("The name of the created Node should be 'PLC'", 0, n.getName().compareTo("PLC"));
		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 1", 1, g.getNodes().size());
		assertTrue("Graph does not store the right node", g.getNodes().contains(n));

		eventTestSystem.doFullCheck(Graph.class,1,1,0);
		eventTestSystem.doFullCheck(Node.class,1,0,0);
		eventTestSystem.doFullCheck(Edge.class,0,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testDeleteNode() {
		Graph g = graphSystem.createGraph();
		Node n = graphSystem.createNode(g);

		Node[] nodes = new Node[10];
		for(int i = 0; i < nodes.length; i++){
			nodes[i] = graphSystem.createNode(g);
			graphSystem.createEdge(nodes[i],n);
			graphSystem.createEdge(n,nodes[i]);
		}

		eventTestSystem.reset();

		graphSystem.deleteNode(n);

		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 1", 10, g.getNodes().size());
		assertFalse(g.getNodes().contains(n));

		for (Node node : nodes) {
			assertEquals("There should be no incoming connections at this node", 0, node.getIncomingConnections().size());
			assertEquals("There should be no outgoing connections at this node", 0, node.getOutgoingConnections().size());
		}

		eventTestSystem.doFullCheck(Graph.class,0,1 + 2 * nodes.length,0);
		eventTestSystem.doFullCheck(Node.class,0,4 * nodes.length,1);
		eventTestSystem.doFullCheck(Edge.class,0,0, 2 * nodes.length);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testCreateNodeGraphMapperSpace() {
		Graph g;
		Node n;
		try(MapperSpace ms = controller.startMapperSpace()){
			g = graphSystem.createGraph();
			n = graphSystem.createNode(g);
		}

		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 1", 1, g.getNodes().size());
		assertTrue("Graph does not store the right node", g.getNodes().contains(n));

		eventTestSystem.doFullCheck(Graph.class,1,1,0);
		eventTestSystem.doFullCheck(Node.class,1,0,0);
		eventTestSystem.doFullCheck(Edge.class,0,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testDeleteEdge() {
		Node[] n = new Node[2];
		Graph g = graphSystem.createGraph();
		n[0] = graphSystem.createNode(g);
		n[1] = graphSystem.createNode(g);
		Edge e = graphSystem.createEdge(n[0],n[1]);

		eventTestSystem.reset();

		graphSystem.deleteEdge(e);

		assertEquals("Size of set of edges should be 0", 0, g.getEdges().size());
		assertEquals("Size of set of nodes should be 0", 2, g.getNodes().size());

		assertTrue("Graph still stores the node", g.getNodes().contains(n[0]));
		assertTrue("Graph still stores the node", g.getNodes().contains(n[1]));

		assertFalse("Graph still stores the edge", g.getEdges().contains(e));
		assertTrue("The edge is still connected to the nodes", !n[0].getOutgoingConnections().contains(e) && !n[1].getIncomingConnections().contains(e) );

		eventTestSystem.doFullCheck(Graph.class,0,1,0);
		eventTestSystem.doFullCheck(Node.class,0,2,0);
		eventTestSystem.doFullCheck(Edge.class,0,0,1);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testCreateEdge() {

		Node[] n = new Node[2];
		Graph g = graphSystem.createGraph();
		n[0] = graphSystem.createNode(g);
		n[1] = graphSystem.createNode(g);
		Edge e = graphSystem.createEdge(n[0],n[1], "Nice name");

		assertEquals("Size of set of edges should be 1", 1, g.getEdges().size());
		assertEquals(0, (g.getEdges().toArray()[0]).toString().compareTo("Nice name"));
		assertEquals("Size of set of nodes should be 2", 2, g.getNodes().size());

		assertTrue("Graph still stores the node", g.getNodes().contains(n[0]));
		assertTrue("Graph still stores the node", g.getNodes().contains(n[1]));

		assertTrue("Graph still stores the edge", g.getEdges().contains(e));
		assertTrue("The edge is not connected to the right nodes", e.getSource() == n[0] && e.getDestination() == n[1]);

		eventTestSystem.doFullCheck(Graph.class,1,3,0);
		eventTestSystem.doFullCheck(Node.class,2,2,0);
		eventTestSystem.doFullCheck(Edge.class,1,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void testCreateEdgeMapperSpace() {
		Node[] n = new Node[2];
		Graph g;
		Edge e;
		try(MapperSpace ms = controller.startMapperSpace()){
			g = graphSystem.createGraph();
			n[0] = graphSystem.createNode(g);
			n[1] = graphSystem.createNode(g);
			e = graphSystem.createEdge(n[0],n[1]);
		}

		assertEquals("Size of set of edges should be 0", 1, g.getEdges().size());
		assertEquals("Size of set of nodes should be 0", 2, g.getNodes().size());

		assertTrue("Graph still stores the node", g.getNodes().contains(n[0]));
		assertTrue("Graph still stores the node", g.getNodes().contains(n[1]));

		assertTrue("Graph still stores the edge", g.getEdges().contains(e));
		assertTrue("The edge is not connected to the right nodes", e.getSource() == n[0] && e.getDestination() == n[1]);

		eventTestSystem.doFullCheck(Graph.class,1,3,0);
		eventTestSystem.doFullCheck(Node.class,2,2,0);
		eventTestSystem.doFullCheck(Edge.class,1,0,0);

		eventTestSystem.checkIfEmpty();
	}

	@Test
	public final void TestGML() {
		Graph g = graphSystem.createGraph();
		Node n1 = graphSystem.createNode(g);
		Node n2 = graphSystem.createNode(g);
		graphSystem.createEdge(n1, n2);

		String case1 = "graph [\n" +
				"\tdirected 1\n" +
				"\tid 0\n" +
				"\t node [\n" +
				"\t\tid 1\n" +
				"\t]\n" +
				"\t node [\n" +
				"\t\tid 2\n" +
				"\t]\n" +
				"\tedge [\n" +
				"\t\t source 1\n" +
				"\t\t target 2\n" +
				"\t]\n" +
				"]";
		String case2 = "graph [\n" +
				"\tdirected 1\n" +
				"\tid 0\n" +
				"\t node [\n" +
				"\t\tid 2\n" +
				"\t]\n" +
				"\t node [\n" +
				"\t\tid 1\n" +
				"\t]\n" +
				"\tedge [\n" +
				"\t\t source 1\n" +
				"\t\t target 2\n" +
				"\t]\n" +
				"]";
		assertTrue("GML do not look like it should", g.toGML().trim().compareTo(case1) == 0
				|| g.toGML().trim().compareTo(case2) == 0);
	}

	@Test(timeout = 5000)
	public final void TestBigTopology() {

		int numNodes = 1000;
		int numEdges = 4000;

		try(MapperSpace ms = controller.startMapperSpace()){
			Graph g = graphSystem.createGraph();
			Vector<Node> nodes = new Vector<>();
			for(int i = 0; i < numNodes; i++)
				nodes.add(graphSystem.createNode(g));

			for(int i = 0; i < numEdges; i++) {
				int n1 =(int) (Math.random() * (double) nodes.size());
				int n2 =(int) (Math.random() * (double) nodes.size());
				graphSystem.createEdge(nodes.get(n1), nodes.get(n2));
			}
		}
		eventTestSystem.doFullCheck(Graph.class,1,numNodes + numEdges,0);
		eventTestSystem.doFullCheck(Node.class,numNodes,2 * numEdges,0);
		eventTestSystem.doFullCheck(Edge.class,numEdges,0,0);

		eventTestSystem.checkIfEmpty();
	}
}
