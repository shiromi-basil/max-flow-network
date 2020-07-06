import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This Main class implements an application that solves the maximum possible flow
 * problem.
 *
 * @author Shiromi Basil
 * @version 1.0
 * @since 2020-03-05
 */
public class Menu {
    // Declare and initialize Scanner object to receive keyboard inputs from the user
    public Scanner scanner = new Scanner(System.in);

    /**
     * This method is used to validate integer input.
     *
     * @param value The lower boundary of the integer.
     * @return int Returns the validated integer value.
     */
    public int userInputInt(int value) {
        try {
            int integer = Integer.parseInt(scanner.next());
            // Check if the integer entered is greater than the value
            if (integer >= value) {
                return integer;
            } else {
                System.out.println("Please enter a valid integer!");
                return userInputInt(value);
            }
        } catch (InputMismatchException | NumberFormatException e) {
            // Catches the exception if the user enter
            // a non numeric or a non integer value
            System.out.println("Please enter an integer value!");
            return userInputInt(value);
        }
    }

    /**
     * This method is used to validate source and sink values of the given graph.
     *
     * @param noOfNodes The number of nodes of the given graph.
     * @return int Returns the validated source or sink value.
     */
    public int userSourceSink(int noOfNodes) {
        try {
            int integer = Integer.parseInt(scanner.next());
            // Checks if the value entered by the user is grater or equal to zero and
            // less than the number of nodes
            if (integer >= 0 && integer < noOfNodes) {
                return integer;
            } else {
                System.out.println("Please enter a value between 0 - " + (noOfNodes - 1) +
                        "!");
                return userSourceSink(noOfNodes);
            }
        } catch (InputMismatchException | NumberFormatException e) {
            // Catches the exception if the user enter
            // a non numeric or a non integer value
            System.out.println("Please enter an integer value!");
            return userSourceSink(noOfNodes);
        }
    }

    /**
     * This method reads the user inputs and saves them into a 2D array.
     *
     * @param sourceNode The source node of the given graph.
     * @param sinkNode   The sink node of the given graph.
     * @param matrix     The empty matrix of the given graph.
     * @return int[][] Returns the matrix of the given graph with the edges.
     */
    public int[][] readUserInputs(int sourceNode, int sinkNode, int[][] matrix) {
        System.out.println("\nPlease enter the capacities of the following links: ");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // Check if the first is not source node and the last is not sink node
                if ((i != sinkNode) && (i != j) && (j != sourceNode)) {
                    System.out.print(i + " to " + j + ": ");
                    matrix[i][j] = userInputInt(0);
                }
            }
        }
        return matrix;
    }

    /**
     * This method reads the inputs from a text file.
     *
     * @param matrix The empty matrix of the given graph.
     * @return int[][] Returns the matrix of the given graph with the edges.
     * @throws FileNotFoundException On the text file.
     */
    public int[][] readFromFile(int[][] matrix) throws FileNotFoundException {
        // Read from the text file
        Scanner sc = new Scanner(new BufferedReader(new FileReader("src/Graph6.txt")));
        while (sc.hasNextLine()) {
            for (int i = 0; i < matrix.length; i++) {
                String[] line = sc.nextLine().trim().split(" "); // Separate by spaces
                for (int j = 0; j < line.length; j++) {
                    matrix[i][j] = Integer.parseInt(line[j]);
                }
            }
        }
        return matrix;
    }

    /**
     * This method makes use of the MaximumFlowNetwork class to find the maximum possible
     * flow of the given graph. The time taken to find the maximum flow is calculated.
     *
     * @param sourceNode The source node of the given graph.
     * @param sinkNode   The sink node of the given graph.
     * @param matrix     The matrix of the given graph with the edges.
     * @param noOfNodes  The number of nodes in the given graph.
     */
    public void findMaxFlow(int sourceNode, int sinkNode, int[][] matrix, int noOfNodes) {
        long startTime = System.nanoTime() / 1000; // Start recording time in milliseconds

        // Declare and initialize MaximumFlowNetwork object
        MaximumFlowNetwork network = new MaximumFlowNetwork(noOfNodes);
        // Calculate the maximum possible flow of the given graph
        int maximumFlow = network.fordFulkerson(matrix, sourceNode, sinkNode);
        System.out.println("\nThe maximum possible flow from source node " + sourceNode +
                " to sink node " + sinkNode + ": " + maximumFlow);
        // Print the time elapsed to calculate the maximum possible flow of a given graph
        System.out.println("Elapsed time: " + ((System.nanoTime() / 1000) - startTime) + " microseconds");

        // Print the residual graph of the user given graph
        int[][] residualMatrix = network.printResidual(matrix);
        boolean valid = network.validateEdges(sourceNode, sinkNode, residualMatrix);
        System.out.println("The graph shown below adheres to all the restrictions: " +
                valid);

        // Print the edges and their respective capacities of the residual graph
        System.out.println("\nYour network flow matrix: ");
        printMatrix(residualMatrix);
        System.out.println();
        for (int u = 0; u < noOfNodes; u++) {
            for (int v = 0; v < noOfNodes; v++) {
                int capacity = residualMatrix[u][v];
                if (capacity > 0) {
                    System.out.println(u + " - " + v + ": " + capacity);
                }
            }
        }
    }

    /**
     * This method prints the given 2D array in a matrix format.
     *
     * @param matrix The matrix to be printed.
     */
    public void printMatrix(int[][] matrix) {
        // Looping through all the rows of the matrix
        for (int[] row : matrix) {
            // Looping through all the columns of the current row
            for (int col : row) {
                // Print each element of the row
                System.out.print(col + " ");
            }
            // Start a new row
            System.out.println();
        }
    }

    /**
     * This method exits the program.
     */
    public void closeMenu() {
        System.out.println("\nThank you!");
        System.exit(0);
    }

    /**
     * This is the main method is used to display menu and makes use of all the
     * methods and classes of this project.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        // Declare and initialize Menu object
        Menu menu = new Menu();

        // Print program instructions
        System.out.println("Welcome!!!");
        System.out.print("Please enter the number of nodes: (Including the source and " +
                "sink nodes) ");
        int noOfNodes = menu.userInputInt(6);
        System.out.print("Enter the Source Node: (0 to " + (noOfNodes - 1) + ") ");
        int sourceNode = menu.userSourceSink(noOfNodes);
        System.out.print("Enter the Sink Node: (0 to " + (noOfNodes - 1) + ") ");
        int sinkNode = menu.userSourceSink(noOfNodes);

        // Initialize a2D array to hold the graph entered by the user
        int[][] matrix = new int[noOfNodes][noOfNodes];

        boolean menuOption1 = true;
        while (menuOption1) {
            System.out.println("\nPlease select one of the following option: ");
            System.out.println("1 - Enter inputs.\n2 - Read from a text file.");
            int option = menu.userInputInt(0);
            switch (option) {
                case 1:
                    // Read user inputs to create the graph
                    matrix = menu.readUserInputs(sourceNode, sinkNode, matrix);
                    menuOption1 = false;
                    break;
                case 2:
                    // Read from file to create the graph
                    try {
                        matrix = menu.readFromFile(matrix);
                        menuOption1 = false;
                    } catch (FileNotFoundException e) {
                        System.err.println("Error : " + e);
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option number.");
            }
        }

        // Print the graph entered by the user
        System.out.println("\nYour network flow matrix: ");
        menu.printMatrix(matrix);
        menu.findMaxFlow(sourceNode, sinkNode, matrix, noOfNodes);

        int from;
        int to;
        boolean menuOption2 = true;
        while (menuOption2) {
            System.out.println("\nPlease select one of the following option: ");
            System.out.println("1 - Add an edge.\n2 - Delete an edge.\n3 - Change the " +
                    "capacity of an edge.\n4 - Exit");
            int option = menu.userInputInt(0);
            switch (option) {
                case 1:
                    // Read inputs from user to add an edge
                    System.out.println("Please enter the edge you want to add.");
                    System.out.print("From : (0 to " + (noOfNodes - 1) + ") ");
                    from = menu.userSourceSink(noOfNodes);
                    System.out.print("To : (0 to " + (noOfNodes - 1) + ") ");
                    to = menu.userSourceSink(noOfNodes);

                    // Check if the edge doesn't exists
                    if (matrix[from][to] == 0) {
                        System.out.print("Capacity : ");
                        int capacity = menu.userInputInt(0);
                        matrix[from][to] = capacity;
                        System.out.println("\nYour network flow matrix: ");
                        menu.printMatrix(matrix);
                        menu.findMaxFlow(sourceNode, sinkNode, matrix, noOfNodes);
                    } else {
                        System.out.println("The edge you entered already exists!");
                    }
                    break;
                case 2:
                    // Read inputs from user to delete an edge
                    System.out.println("Please enter the edge you want to delete.");
                    System.out.print("From : (0 to " + (noOfNodes - 1) + ") ");
                    from = menu.userSourceSink(noOfNodes);
                    System.out.print("To : (0 to " + (noOfNodes - 1) + ") ");
                    to = menu.userSourceSink(noOfNodes);

                    // Check if the capacity of the edge is not zero
                    if (matrix[from][to] != 0) {
                        matrix[from][to] = 0;
                        System.out.println("\nYour network flow matrix: ");
                        menu.printMatrix(matrix);
                        menu.findMaxFlow(sourceNode, sinkNode, matrix, noOfNodes);

                    } else {
                        System.out.println("The edge you entered does not exist!");
                    }
                    break;
                case 3:
                    // Read inputs from the user to change the
                    // capacity of an existing edge
                    System.out.println("Please enter the edge you want to change.");
                    System.out.print("From : (0 to " + (noOfNodes - 1) + ") ");
                    from = menu.userSourceSink(noOfNodes);
                    System.out.print("To : (0 to " + (noOfNodes - 1) + ") ");
                    to = menu.userSourceSink(noOfNodes);

                    // Check if the edge already exists
                    if (matrix[from][to] != 0) {
                        System.out.print("Capacity : ");
                        int capacity = menu.userInputInt(0);
                        matrix[from][to] = capacity;
                        System.out.println("\nYour network flow matrix: ");
                        menu.printMatrix(matrix);
                        menu.findMaxFlow(sourceNode, sinkNode, matrix, noOfNodes);
                    } else {
                        System.out.println("The edge you entered does not exist!");
                    }
                    break;
                case 4:
                    // Exit program
                    menuOption2 = false;
                    break;
                default:
                    System.out.println("Please enter a valid option number.");
            }
        }
        // Exit program
        menu.closeMenu();
    }

}
