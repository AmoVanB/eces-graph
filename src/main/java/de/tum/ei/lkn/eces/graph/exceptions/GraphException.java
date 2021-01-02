package de.tum.ei.lkn.eces.graph.exceptions;

/**
 * Exception thrown when wrong Graph operations are performed.
 *
 * @author Jochen Guck
 * @author Amaury Van Bemten
 */
public class GraphException extends RuntimeException {
	private static final long serialVersionUID = 3913533029682219608L;

	public GraphException(String string) {
		super(string);
	}
}