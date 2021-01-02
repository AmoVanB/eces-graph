package de.tum.ei.lkn.eces.graph;

import de.tum.ei.lkn.eces.core.Controller;
import de.tum.ei.lkn.eces.core.Entity;
import de.tum.ei.lkn.eces.core.MapperSpace;
import de.tum.ei.lkn.eces.core.RootSystem;
import de.tum.ei.lkn.eces.graph.exceptions.GraphException;
import de.tum.ei.lkn.eces.graph.mappers.EdgeMapper;
import de.tum.ei.lkn.eces.graph.mappers.GraphMapper;
import de.tum.ei.lkn.eces.graph.mappers.NodeMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * System handling a Graph.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */
public class GraphSystem  extends RootSystem {
	/**
	 * Mapper handling the Graph Components.
	 */
	private GraphMapper graphMapper;

	/**
	 * Mapper handling the Edge Components.
	 */
	private EdgeMapper edgeMapper;

	/**
	 * Mapper handling the Node Components.
	 */
	private NodeMapper nodeMapper;

	/**
	 * Creates a new GraphSystem.
	 * @param controller Controller responsible for the GraphSystem.
	 */
	public GraphSystem(Controller controller) {
		super(controller);
		graphMapper = new GraphMapper(controller);
		edgeMapper  = new EdgeMapper(controller);
		nodeMapper  = new NodeMapper(controller);
	}

	/**
	* Creates a new Graph as a Component of a new Entity.
	* @return the new Graph instance.
	*/
	public Graph createGraph() {
		return createGraph(controller.createEntity());
	}

	/**
	 * Creates a new Graph as a Component of a given Entity.
	 * @param entity Entity to which the Graph must be attached.
	 * @return the new Graph instance.
	 */
	public Graph createGraph(Entity entity) {
		Graph graph = new Graph();
		graphMapper.attachComponent(entity, graph);
		logger.info(graph + " created.");
		return graph;
	}

	/**
	 * Deletes a Graph and all its Nodes and Edges.
	 * @param graph Graph to be deleted.
	 */
	public void deleteGraph(Graph graph) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			graphMapper.acquireReadLock(graph);
			for(Edge edge : graph.getEdges())
				deleteEdge(edge);

			for(Node node : graph.getNodes()) {
				nodeMapper.detachComponent(node);
				graphMapper.updateComponent(graph, ()->graph.removeNode(node));
			}

			graphMapper.detachComponent(graph);

			logger.info(graph + " deletion triggered.");
		}
	}

	/**
	 * Deletes a Graph and all its Nodes and Edges.
	 * @param entity Entity containing the Graph to be deleted.
	 */
	public void deleteGraph(Entity entity) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			deleteGraph(graphMapper.get(entity));
		}
	}

	/**
	* Creates a Node as a Component of a new Entity and adds it to
	* the given Graph.
	* @param graph the target Graph.
	* @return the new Node instance.
	*/
	public Node createNode(Graph graph) {
		return createNode(graph, controller.createEntity());
	}

	/**
	 * Creates a Node as a Component of a new Entity and adds it to
	 * the given Graph.
	 * @param graph the target Graph.
	 * @param name Name of the Node.
	 * @return the new Node instance.
	 */
	public Node createNode(Graph graph, String name) {
		return createNode(graph, name, controller.createEntity());
	}

	/**
	* Creates a Node as a Component of the given Entity and adds it
	* to the given Graph.
	* @param graph the target Graph.
	* @param entity Entity to which the Node must be attached.
	* @return the new Node instance.
	*/
	public Node createNode(Graph graph, Entity entity) {
		return createNode(graph, "", entity);
	}

	/**
	 * Creates a Node as a Component of the given Entity and adds it
	 * to the given Graph.
	 * @param graph the target Graph.
	 * @param name Name of the Node.
	 * @param entity Entity to which the Node must be attached.
	 * @return the new Node instance.
	 */
	public Node createNode(Graph graph, String name, Entity entity) {
		// Create the Node object and add it to the Entity.
		Node node = new Node(graph, name);

		try(MapperSpace ms = controller.startMapperSpace()) {
			// Update Graph before to avoid updating Nodes if the Graph is write-protected.
			graphMapper.updateComponent(graph, ()->graph.addNode(node));
			nodeMapper.attachComponent(entity, node);

			logger.info(node + " creation in " + graph + " triggered.");
		}

		return node;
	}

	/**
	 * Deletes a Node and all Edges connected to it.
	 * @param node Node to delete.
	 */
	public void deleteNode(Node node) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			nodeMapper.acquireReadLock(node);
			Graph graph = node.getGraph();
			// Update Graph before to avoid updating Nodes if the Graph is write-protected.
			graphMapper.updateComponent(graph, ()->graph.removeNode(node));

			List<Edge> outgoingConnections = node.getOutgoingConnections();
			for(Edge edge : outgoingConnections)
				deleteEdge(edge);

			List<Edge> incomingConnections = node.getIncomingConnections();
			for(Edge edge : incomingConnections)
				deleteEdge(edge);
			nodeMapper.detachComponent(node);

			logger.info(node + " deletion from " + node.getGraph() + " triggered.");
		}
	}

	/**
	 * Deletes a Node and all Edges connected to it.
	 * @param entity Entity containing the Node to delete.
	 */
	public void deleteNode(Entity entity) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			deleteNode(nodeMapper.get(entity));
		}
	}

	/**
	 * Creates an Edge as a Component of a new Entity.
	 * @param srcNode source of the Edge.
	 * @param dstNode destination of the Edge.
	 * @return the new Edge instance.
	 * @throws GraphException if the two Nodes do not belong to the same Graph.
	 */
	public Edge createEdge(Node srcNode, Node dstNode) {
		return createEdge(srcNode, dstNode, "", controller.createEntity());
	}

	/**
	 * Creates an Edge as a Component of a new Entity.
	 * @param srcNode source of the Edge.
	 * @param dstNode destination of the Edge.
	 * @param name Edge name.
	 * @return the new Edge instance.
	 * @throws GraphException if the two Nodes do not belong to the same Graph.
	 */
	public Edge createEdge(Node srcNode, Node dstNode, String name) {
		return createEdge(srcNode, dstNode, name, controller.createEntity());
	}

	/**
	 * Creates an Edge as a Component of the given Entity.
	 * @param srcNode source of the Edge.
	 * @param dstNode destination of the Edge.
	 * @param name Edge name.
	 * @param entity Entity object that will carry the Edge Component.
	 * @return the new Edge instance.
	 * @throws GraphException if the two Nodes do not belong to the same Graph.
  	 */
	public Edge createEdge(Node srcNode, Node dstNode, String name, Entity entity) {
		if(srcNode.getGraph() != dstNode.getGraph())
			throw new GraphException("Can only create an Edge between two Nodes of the same Graph");

		Edge edge = new Edge(srcNode, dstNode, name);

		try(MapperSpace ms = controller.startMapperSpace()) {
			edgeMapper.attachComponent(entity, edge);
			nodeMapper.acquireReadLock(srcNode);
			Graph graph = srcNode.getGraph();
			// Update Graph before to avoid updating Nodes if the Graph is write-protected.
			graphMapper.updateComponent(graph, ()->graph.addEdge(edge));
			nodeMapper.updateComponent(srcNode, ()->srcNode.addOutgoingConnection(edge));
			nodeMapper.updateComponent(dstNode, ()->dstNode.addIncomingConnection(edge));

			logger.info(edge + " creation (" + srcNode + " -> " + dstNode + ") in " + graph + " triggered.");
		}

		return edge;
	}

	/**
	 * Deletes an Edge.
	 * @param edge Edge to delete.
	 */
	public void deleteEdge(Edge edge) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			edgeMapper.acquireReadLock(edge);
			Node src = edge.getSource();
			Node dst = edge.getDestination();
			nodeMapper.acquireReadLock(src);
			Graph graph = src.getGraph();
			edgeMapper.detachComponent(edge);
			// Update Graph before to avoid updating Nodes if the Graph is write-protected.
			graphMapper.updateComponent(graph, ()->graph.removeEdge(edge));
			nodeMapper.updateComponent(src, ()->src.removeOutgoingConnection(edge));
			nodeMapper.updateComponent(dst, ()->dst.removeIncomingConnection(edge));

			logger.info(edge + " deletion from " + graph + " programmed.");
		}
	}

	/**
	 * Deletes an Edge.
	 * @param entity Entity holding the Edge to delete.
	 */
	public void deleteEdge(Entity entity) {
		try(MapperSpace ms = controller.startMapperSpace()) {
			deleteEdge(edgeMapper.get(entity));
		}
	}
}
