import java.util.LinkedList;
import java.util.Queue;

/**
 * This MaximumFlowNetwork class implements Ford Fulkerson and Breadth First Search
 * algorithms to find the maximum possible flow graph of a given graph
 *
 * Source   : https://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/
 * License  : Open License
 * The source code is tailored according to the coursework specifications.
 *
 * @author Shiromi Basil
 * @version 1.0
 * @since 2020-03-05
 */
public class MaximumFlowNetwork {
    private int[] parent; // Holds parent of a node when a path is found (filled by BFS)
    private Queue<Integer> queue; // Queue of nodes to explore (BFS to FIFO queue)
    private int noOfNodes; // The number of nodes of the given array
    private boolean[] visited; // Keeps track of the nodes that has been visited

    // residualGraph[i][j] tells you if there's an edge between vertex i & j.
    // 0 = no edge, positive number = capacity of that edge
    public int[][] residualGraph;

    /**
     * This is the constructor of MaximumFlowNetwork class.
     *
     * @param noOfNodes The number of nodes of the given graph.
     */
    public MaximumFlowNetwork(int noOfNodes) {
        this.noOfNodes = noOfNodes;
        this.queue = new LinkedList<>();
        parent = new int[noOfNodes];
        visited = new boolean[noOfNodes];
        residualGraph = new int[noOfNodes][noOfNodes];
    }

    /**
     * Searches whether there exists an augmenting path from the source to the sink
     * and the set of vertices that exists in this path are stored in parent[].
     *
     * @param source The source node of the given graph.
     * @param sink   The sink node of the given graph.
     * @param graph  The empty matrix of the given graph.
     * @return boolean Returns true if an augmenting path exists.
     */
    public boolean bfs(int source, int sink, int[][] graph) {
        // Mark all nodes as not visited
        for (int vertex = 1; vertex < noOfNodes; vertex++) {
            visited[vertex] = false;
        }

        // Add the source node and marks it visited
        queue.add(source);
        visited[source] = true;
        parent[source] = -1; // Source has no parent

        // Standard Breadth First Search (BFS) loop
        while (!queue.isEmpty()) {
            // Return and remove the vertex from the front of the queue
            int element = queue.remove();

            // Visit all the adjacent nodes
            for (int destination = 0; destination < noOfNodes; destination++) {
                // Check if the u-v edge capacity > 0 and if a node is not already visited
                if (graph[element][destination] > 0 && !visited[destination]) {
                    parent[destination] = element;
                    queue.add(destination);
                    visited[destination] = true;
                }
            }
        }
        return (visited[sink]); // Return true if the sink node has been reached
    }

    /**
     * Runs the algorithm and calculates the maximum possible flow of the
     * given graph from source to the sink.
     *
     * @param graph  The given flow graph graph.
     * @param source The source from which the algorithm searches.
     * @param sink   The sink to which the algorithm searches to.
     * @return int Returns the maximum possible flow of the given graph.
     */
    public int fordFulkerson(int[][] graph, int source, int sink) {
        int u, v;
        int maximumFlow = 0; // Initialize the maximum possible flow to zero 

        // Initialize residual graph to be same as the original graph
        for (u = 0; u < noOfNodes; u++) {
            for (v = 0; v < noOfNodes; v++) {
                residualGraph[u][v] = graph[u][v];
            }
        }

        // Augment the flow while there is path from source to sink
        while (bfs(source, sink, residualGraph)) {

            // Find bottleneck (minimum) by looping over path from BFS using parent[]
            // array, so initially set it to the largest number possible.
            int pathFlow = Integer.MAX_VALUE;

            // Find the maximum flow through the path found
            // Loop backward through the path using parent[] array
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v]; // Holds the previous node in the path
                // Minimum out of previous bottleneck & the capacity of the new edge
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Update the residual graph capacities & reverse edges along the path
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                residualGraph[u][v] -= pathFlow; // Back edge
                residualGraph[v][u] += pathFlow; // Forward edge
            }
            // Add path flow to overall maximum flow
            maximumFlow += pathFlow;
        }
        // Return the overall maximum flow
        return maximumFlow;
    }

    /**
     * This method validates the residual graph generated.
     *
     * @param sourceNode The source node of the given graph.
     * @param sinkNode   The sink node of the given graph.
     * @param matrix     The matrix of the given graph with the edges.
     * @return boolean Returns true if the generated residual graph is valid.
     */
    public boolean validateEdges(int sourceNode, int sinkNode, int[][] matrix) {
        int noOfCols = matrix[0].length; // Number of columns of the graph
        int sumFromSource = 0;
        int sumToSink = 0;
        boolean valid = true; // Returns true if the given graph is valid

        for (int i = 0; i < noOfCols; i++) {
            sumFromSource += matrix[sourceNode][i]; // Sum of the edges from the source
        }
        for (int[] row : matrix) {
            sumToSink += row[sinkNode]; // Sum of the edges to the sink
        }

        // If the sum of the edges from the source and to the sink are equal,
        // then check if the edges from and to the other nodes are equal
        if (sumFromSource == sumToSink) {
            // Iterating through the number of nodes
            for (int node = 0; node < noOfCols; node++) {
                int sumFromNode = 0;
                int sumToNode = 0;

                // If the node is source or sink,
                // then don't check if the edges to and from the node is equal
                if (node != sourceNode && node != sinkNode) {
                    for (int i = 0; i < noOfCols; i++) {
                        sumFromNode += matrix[node][i];
                    }
                    for (int[] row : matrix) {
                        sumToNode += row[node];
                    }

                    // If the edges from and to a given node is not equal,
                    // then the graph is not valid
                    if (!(sumFromNode == sumToNode)) {
                        valid = false;
                    }
                }
            }
        } else {
            valid = false;
        }
        return valid; // Returns true if the given graph is valid
    }

    /**
     * This method converts the flow network into residual graph and prints the nodes
     * and their respective capacities of the flow network.
     *
     * @param graph The flow network of the user given graph.
     * @return int[][] Returns the residual graph of the user given graph.
     */
    public int[][] printResidual(int[][] graph) {
        // Initialize a graph to hold the converted residual graph
        int[][] finalResidual = new int[noOfNodes][noOfNodes];

        // Iterating through the rows and columns of the flow network
        for (int u = 0; u < noOfNodes; u++) {
            for (int v = 0; v < noOfNodes; v++) {
                if (graph[u][v] > residualGraph[u][v]) {
                    // Capacities of the residual graph is the difference between the
                    // capacities of the user given graph and the flow network
                    finalResidual[u][v] = graph[u][v] - residualGraph[u][v];
                } else
                    finalResidual[u][v] = 0;
            }
        }
        return finalResidual;
    }

}
