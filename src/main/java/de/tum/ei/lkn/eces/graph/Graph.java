package de.tum.ei.lkn.eces.graph;

import de.tum.ei.lkn.eces.core.Component;
import de.tum.ei.lkn.eces.core.annotations.ComponentBelongsTo;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a generic Graph.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */
@ComponentBelongsTo(system = GraphSystem.class)
public class Graph extends Component {
	/**
	 * Set of all the Nodes of the Graph.
	 * */
	protected Set<Node> nodes;

	/**
	 * Set of all the Edges of the Graph.
	 */
	protected Set<Edge> edges;

	/**
	 * Creates a new empty Graph.
	 */
	protected Graph() {
		nodes = new HashSet<>();
		edges = new HashSet<>();
	}

	/**
	 * Adds a Node to the Graph.
	 * @param node Node to add.
	 */
	protected void addNode(Node node) {
		nodes.add(node);
	}

	/**
	 * Removes a Node from the Graph.
	 * @param node Node to remove.
	 */
	protected void removeNode(Node node) {
		nodes.remove(node);
	}

	/**
	 * Gets the Nodes of the Graph.
	 * @return the nodes of the Graph.
	 */
	public Set<Node> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	/**
	 * Adds an Edge to the Graph.
	 * @param edge Edge to add.
	 */
	protected void addEdge(Edge edge) {
		edges.add(edge);
	}

	/**
	 * Removes an Edge from the Graph.
	 * @param edge Edge to remove.
	 */
	protected void removeEdge(Edge edge) {
		edges.remove(edge);
	}

	/**
	 * Gets the Edges of the Graph.
	 * @return the Edges of the Graph.
	 */
	public Set<Edge> getEdges() {
		return Collections.unmodifiableSet(edges);
	}

	/**
	* Creates a GML representation of the Graph.
	* @return String.
	*/
	public String toGML() {
		StringBuilder out = new StringBuilder("graph [\n");
		out.append("\tdirected 1\n");
		out.append("\tid ").append(this.getId()).append("\n");

		// Printing each node definition.
		for(Node node : this.getNodes()) {
		out.append("\t node [\n");
		out.append("\t\tid ").append(node.getId()).append("\n");
		out.append("\t]\n");
		}

		// Printing each edge definition.
		for(Node node : this.getNodes()) {
			List<Edge> connections = node.getIncomingConnections();
			for(Edge e : connections) {
				out.append("\tedge [\n");
				out.append("\t\t source ").append(e.getSource().getId()).append("\n");
				out.append("\t\t target ").append(e.getDestination().getId()).append("\n");
				out.append("\t]\n");
			}
		}

		out.append("]\n");
		return out.toString();
	}
}
