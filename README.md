# Graph

This project implements a graph library on top of the [ECES](https://github.com/AmoVanB/eces-core) framework.
The module defines a system (`GraphSystem.java`) in the context of that framework.

The system is able to create, modify, and delete graphs (`Graph.java`) consisting of edges (`Edge.java`) and nodes (`Node.java`). 

Graphs, edges, and nodes are attached to their own entities.

## Usage

The project can be downloaded from maven central using:
```xml
<dependency>
  <groupId>de.tum.ei.lkn.eces</groupId>
  <artifactId>graph</artifactId>
  <version>X.Y.Z</version>
</dependency>
```

### Simple Example 

The creation and deletion of a graph, a node or an edge has to be done with the `createGraph()`, `deleteGraph()`, `createNode()`, `deleteNode()`, `createEdge()` and `deleteEdge()` methods of the graph system. These methods allow to create any graph structure. The `create*()` methods will return the created component (graph, node or edge) and their respective public methods can then be used to get information on the corresponding component (source node of an edge, graph of a node, edges connected to a node, list of all the nodes in a graph, etc.).

```java
Node[] n = new Node[2];
Graph g = graphSystem.createGraph();
n[0] = graphSystem.createNode(g);
n[1] = graphSystem.createNode(g);
Edge e = graphSystem.createEdge(n[0], n[1]);
```

See [tests](src/test) for other simple examples.

### Advanced Examples

See other ECES repositories using this graph library (e.g., [network](https://github.com/AmoVanB/eces-network) and [routing](https://github.com/AmoVanB/eces-routing)) for more detailed/advanced examples.
