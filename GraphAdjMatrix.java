import java.util.ArrayList;

public class GraphAdjMatrix implements Graph {

	private int[][] matrix;
	private int size;
	
	public GraphAdjMatrix(int size) {
		this.matrix = new int[size][size];
		this.size = size;
	}
	
	@Override
	public void addEdge(int v1, int v2) {

	}
	
	@Override
	public void addEdge(int v1, int v2, int weight) {
		matrix[v1][v2] = weight;
		matrix[v2][v1] = weight;

	}

	@Override
	public int getEdge(int v1, int v2) {
		return matrix[v1][v2];
	}
	
	
	@Override
	public void topologicalSort() {
		
	}


	@Override
	public int createSpanningTree() {
		
		// Initialize a new, minimum spanning tree representation adjacency matrix
		int[][] new_matrix = new int[size][size];
		
		// Keep track of the new total cost of the tree we create
		int total_cost = 0;
		
		// Creating three arrays to represent the solution graph that will be updated in stages
		boolean[] known = new boolean[size];
		int[] cost = new int[size];
		int[] path = new int[size];
		
		// Set default path to -1, since 0 is a vertex.  Set default costs to be high.
		for(int i = 0; i < path.length; i++) {
			cost[i] = 999;
			path[i] = -1;
		}
		
		
		// Starting Prim's algorithm on vertex 0
		known[0] = true;
		cost[0] = 0;
		path[0] = -1;
		
		// Display the initial solution graph
		System.out.println();
		for(int i = 0; i < cost.length; i++) {
			System.out.println(known[i] + " " + cost[i] + " " + path[i]);
		} System.out.println();
		
		// Outsource determining the next vertex to choose after 0 to a helper function
		int choice = prims(0, known, cost, path);
		int weight = getEdge(0, choice);
		
		// Add the selected vertex with the lowest cost edge to the new matrix
		new_matrix[0][choice] = weight;
		new_matrix[choice][0] = weight;
		
		total_cost += weight;
		
		// Update the arrays
		known[choice] = true;
		cost[choice] = weight;
		path[choice] = 0;
		
		// Add the new outgoing edges from choice to the arrays
		for(int i = 0; i < matrix.length; i++) {
			if(matrix[choice][i] < cost[i] && matrix[choice][i] != 0) {
				cost[i] = matrix[choice][i];
				path[i] = choice;
			}
		}
		
		// Displaying update graph
		for(int i = 0; i < cost.length; i++) {
			System.out.println(known[i] + " " + cost[i] + " " + path[i]);
		}
		
		// Loop until the all cost-minimizing edges are found
		while(notComplete(known)) {
			
			// Arbitrary 
			int next_vertex = -1;
			int next_cost = 999;
			
			// Find the next unknown, least-cost outgoing edge and the vertex it connects to 
			for(int i = 0; i < known.length; i++) {
				if(!known[i] && cost[i] < next_cost && cost[i] != 0)  {
					next_vertex = i;
					next_cost = cost[i];
				}
			}
			
			// Add this information to the new matrix
			new_matrix[path[next_vertex]][next_vertex] = next_cost;
			new_matrix[next_vertex][path[next_vertex]] = next_cost;
			total_cost += next_cost;
			
			// Mark the next vertex as known
			known[next_vertex] = true;
			
			// Add the new outgoing edges from the next vertex
			for(int i = 0; i < matrix.length; i++) {
				if(matrix[next_vertex][i] < cost[i] && matrix[next_vertex][i] != 0 && !known[i]) {
					cost[i] = matrix[next_vertex][i];
					path[i] = next_vertex;
				}
			}
			
			// Displaying update graph
			System.out.println();
			for(int i = 0; i < cost.length; i++) {
				System.out.println(known[i] + " " + cost[i] + " " + path[i]);
			} System.out.println();
		}
		
		// Replace the old adjacency matrix with the new, solved MST matrix and return the new total cost
		matrix = new_matrix;
		return total_cost;
	}
	
	private int prims(int vertex, boolean[] known, int cost[], int[] path) {
		// Get the neighbors of the vertex
		int[] neighbors = neighbors(vertex);
		
		int choice = neighbors[0];
		int weight = getEdge(vertex, choice);
		
		// Iterate through the neighbors, updating paths and cost through vertex 0
		for(int i = 0; i < neighbors.length; i++) {
			
			path[neighbors[i]] = vertex; 
			cost[neighbors[i]] = getEdge(vertex, neighbors[i]);
			
			// Update the choice of the outgoing edge if it has the smallest cost
			if(getEdge(vertex, neighbors[i]) <= weight && !known[i]) {
				choice = neighbors[i];
				weight = getEdge(vertex, neighbors[i]);
			}
		}
		
		return choice;
	}

	
	// Helper function for createSpanningTree() that signals whether the solution has been found
	private boolean notComplete(boolean[] known) {
		for(int i = 0; i < known.length; i++) {
			if(known[i] == false) 
				return true;
		}
		return false;
	}
	
	
	// Returns an int-array of neighbors
	public int[] neighbors(int vertex) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		// Iterate horizontally to find all directed edges out of vertex
		for(int i = 0; i < size; i++) {
			if(matrix[vertex][i] != 0) {
				temp.add(i);
			}
		}
		
		// Calls the helper function on the temp ArrayList
		int[] neighbors = convertIntegers(temp);
		return neighbors;
	}
	
	// Helper function to convert an ArrayList of Integer objects to a primitive int array
	private static int[] convertIntegers(ArrayList<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	} 

}
