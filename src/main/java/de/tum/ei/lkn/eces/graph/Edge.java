package de.tum.ei.lkn.eces.graph;

import de.tum.ei.lkn.eces.core.Component;
import de.tum.ei.lkn.eces.core.annotations.ComponentBelongsTo;
import org.json.JSONObject;

/**
 * Unidirectional Edge from a source to a destination Node.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */

@ComponentBelongsTo(system = GraphSystem.class)
public class Edge extends Component {
	/**
	 * Source node.
	 */
	private Node source;

	/**
	 * Destination Node.
	 */
	private Node destination;

	/**
	 * Edge name.
	 */
	private String name;

	/**
	 * Creates an Edge.
	 * @param source Source Node of the Edge.
	 * @param destination Destination Node of the Edge.
	 */
	protected Edge(Node source, Node destination) {
		this(source, destination, "");
	}

	/**
	 * Creates an Edge.
	 * @param source Source Node of the Edge.
	 * @param destination Destination Node of the Edge.
	 */
	protected Edge(Node source, Node destination, String name) {
		this.setSource(source);
		this.setDestination(destination);
		this.setName(name);
	}

	/**
	 * Sets the source Node of the Edge.
	 * @param node new source Node.
	 */
	protected void setSource(Node node) {
		source = node;
	}

	/**
	 * Sets the destination Node of the Edge.
	 * @param node new destination Node.
	 */
	protected void setDestination(Node node) {
		destination = node;
	}

	/**
	 * Gets the source Node of the Edge.
	 * @return source Node.
	 */
	public Node getSource() {
		return source;
	}

	/**
	 * Gets the destination Node of the Edge.
	 * @return destination Node.
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * Gets the name of the Edge.
	 * @return Edge name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Edge.
	 * @param name Name.
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Tells whether or not the given Nodes are connected by the current Edge.
	 * @return true if the given nodes are connected by the current Edge.
	 */
	public boolean connects(Node a, Node b) {
		return (a == source && b == destination) || (a == destination && b == source);
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject obj = super.toJSONObject();
		obj.put("id", this.getId());
		obj.put("name", this.getName());
		obj.put("source", this.getSource().getId());
		obj.put("destination", this.getDestination().getId());
		return obj;
	}

	public String toString() {
		if(this.getName().compareTo("") == 0)
			return super.toString();
		else
			return this.getName();
	}
}