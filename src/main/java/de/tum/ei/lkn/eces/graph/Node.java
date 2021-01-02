package de.tum.ei.lkn.eces.graph;

import de.tum.ei.lkn.eces.core.Component;
import de.tum.ei.lkn.eces.core.annotations.ComponentBelongsTo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Class representing a Node in a Graph.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */
@ComponentBelongsTo(system = GraphSystem.class)
public class Node extends Component {
	/**
	 * List of all the Edges for which the node is the source.
	 */
	protected LinkedList<Edge> outgoingConnections;

	/**
	 * List of all Edge for which the Node is the destination.
	 */
	protected LinkedList<Edge> incomingConnections;

	/**
	 * Graph to which the Node belongs.
	 */
	private Graph graph;

	/**
	 * Name of the Graph Node.
	 */
	private String name;

	/**
	 * Creates a Node in a Graph.
	 * @param graph Graph containing the node.
	 * @param name Name of the Node.
	 */
	protected Node(Graph graph, String name) {
		this.name = name;
		this.graph = graph;
		this.outgoingConnections = new LinkedList<>();
		this.incomingConnections = new LinkedList<>();
	}

	/**
	 * Creates a Node in a Graph.
	 * @param graph Graph containing the node.
	 */
	protected Node(Graph graph) {
		this(graph, "");
	}

	/**
	 * Creates a Node.
	 * @param name Name of the Node.
	 */
	protected Node(String name) {
		this(null, name);
	}

	/**
	 * Creates a Node.
	 */
	protected Node() {
		this(null, "");
	}

	/**
	 * Sets the Graph of a Node.
	 * @param graph Graph containing the Node.
	 */
	protected void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Gets the Graph in which the Node is.
	 * @return The Graph containing the Node.
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Gets the name of the Node.
	 * @return Name of the Node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds an incoming Edge to the Node.
	 * @param edge outgoing Edge.
	 */
	protected void addOutgoingConnection(Edge edge) {
		outgoingConnections.add(edge);
	}

	/**
	 * Removes an outgoing Edge from the Node.
	 * @param edge The Edge to remove.
	 */
	protected void removeOutgoingConnection(Edge edge) {
		outgoingConnections.remove(edge);
	}

	/**
	 * Removes all the outgoing Edges of the Node.
	 */
	protected void clearOutgoingConnections() {
		outgoingConnections.clear();
	}

	/**
	 * Gets a list of the outgoing Edges.
	 * @return List of the Edges.
	 */
	public List<Edge> getOutgoingConnections() {
		return Collections.unmodifiableList(outgoingConnections);
	}

	/**
	 * Adds an incoming Edge to the Node.
	 * @param edge incoming Edge.
	 */
	protected void addIncomingConnection(Edge edge) {
		incomingConnections.add(edge);
	}

	/**
	 * Removes an incoming Edge from the Node.
	 * @param edge The Edge to remove.
	 */
	protected void removeIncomingConnection(Edge edge) {
		incomingConnections.remove(edge);
	}

	/**
	 * Gets a list of the incoming Edges.
	 * @return List of the Edges.
	 */
	public List<Edge> getIncomingConnections() {
		return Collections.unmodifiableList(incomingConnections);
	}

	/**
	 * Removes all the incoming Edges of the Node.
	 */
	public void clearIncomingConnections() {
		incomingConnections.clear();
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject obj = super.toJSONObject();

		obj.put("id", this.getId());
		obj.put("name", this.getName());
		obj.put("graphId", this.getGraph().getId());

		// Building array for the outgoing Edges.
		JSONArray outEdgeArray = new JSONArray();

		List<Edge> outEdgeList = this.getOutgoingConnections();

		// Maps a Node ID to the number of out connections to it.
		Map<Long, Integer> outEdgeCounter = new HashMap<>();

		// Filling outEdgeCounter.
		for(Edge outEdge : outEdgeList) {
			long dstNode = outEdge.getDestination().getId();
			if(!outEdgeCounter.containsKey(dstNode))
				outEdgeCounter.put(dstNode, 1);
			else
				outEdgeCounter.put(dstNode, outEdgeCounter.get(dstNode) + 1);
		}

		// One entry in the array per destination Node.
		for(Map.Entry<Long, Integer> entry : outEdgeCounter.entrySet()) {
			Long dstNode = entry.getKey();
			Integer edgeCount = entry.getValue();
			if(edgeCount == 1)
				outEdgeArray.put("To Node " + dstNode + ": " + edgeCount + " connection");
			else
				outEdgeArray.put("To Node " + dstNode + ": " + edgeCount + " connections");
		}

		obj.put("outgoingConnections", outEdgeArray);

		// Same procedure for incoming Edges.
		JSONArray inEdgeArray = new JSONArray();
		List<Edge> inEdgeList = this.getIncomingConnections();
		Map<Long, Integer> inEdgeCounter = new HashMap<>();

		for(Edge inEdge : inEdgeList) {
			long srcNode = inEdge.getSource().getId();
			if(!inEdgeCounter.containsKey(srcNode))
				inEdgeCounter.put(srcNode, 1);
			else
				inEdgeCounter.put(srcNode, inEdgeCounter.get(srcNode) + 1);
		}

		for(Map.Entry<Long, Integer> entry : inEdgeCounter.entrySet()) {
			Long srcNode = entry.getKey();
			Integer edgeCount = entry.getValue();
			if(edgeCount == 1)
				inEdgeArray.put("From Node " + srcNode + ": " + edgeCount + " connection");
			else
				inEdgeArray.put("From Node " + srcNode + ": " + edgeCount + " connections");
		}

		obj.put("incomingConnections", inEdgeArray);

		return obj;
	}

	public String toString() {
		if(this.name.compareTo("") == 0)
			return super.toString();
		else
			return this.name;
	}
}
