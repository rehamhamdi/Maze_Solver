package algorithm_project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Algorithm {

    // start time
    long startTime;
    //stop time
    long stopTime;
    //calculate the elapsed time
    long duration;
    //time for DFS
    double dfsTime;
    //time for BFS
    double bfsTime;
    //time for A*
    double AstarTime;
    int nodeCount = 0; // Counter for the number of nodes visited

    public void dfs(Node start) {
        startTime = System.nanoTime(); // start of the time
        Stack<Node> nodes = new Stack<>();
        nodes.push(start);
        nodeCount = 0;
        while (!nodes.empty()) {
            Node curNode = nodes.pop();
            if (!curNode.isEnd()) {
                curNode.setColor(Color.ORANGE);
                curNode.setColor(Color.BLUE); //visited node
                nodeCount++; // Increment the node count
                for (Node adjacent : curNode.getNeighbours()) {
                    nodes.push(adjacent);
                }
            } else {
                curNode.setColor(Color.MAGENTA); //end node
                break;
            }
        }
         if (!nodes.isEmpty()) {               //you exited before the satck was emptied, meaning that you found the solution and exited.
            stopTime = System.nanoTime();    //stop the timer
        }   
        duration = stopTime - startTime;    //calculate the elapsed time
        dfsTime = (double) duration / 1000000;   //convert to ms
        System.out.println(String.format("DFS Algorithm Time %1.3f ms", dfsTime));
        System.out.println("Number of nodes traversed in DFS Algorithm: " + nodeCount); // Print the number of nodes traversed

    }

    public void bfs(Node start, Node end, int graphWidth, int graphHeight) {
        // start of the time
        startTime = System.nanoTime();
        Queue<Node> queue = new LinkedList<>();
        Node[][] prev = new Node[graphWidth][graphHeight];
        queue.add(start);
        nodeCount = 0;
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if (curNode.isEnd()) {
                curNode.setColor(Color.MAGENTA);
                break;
            }
            if (!curNode.isSearched()) {
                curNode.setColor(Color.ORANGE);
                curNode.setColor(Color.BLUE);
                nodeCount++; // Increment the node count
                for (Node adjacent : curNode.getNeighbours()) {
                    queue.add(adjacent);
                    prev[adjacent.getX()][adjacent.getY()] = curNode;
                }
            }
        }
        if (!queue.isEmpty()) {               //you exited before the queue was emptied, meaning that you found the solution and exited.
            stopTime = System.nanoTime();    //stop the timer
        }   
        shortpath(prev, end);
        duration = stopTime - startTime;    //calculate the elapsed time
        bfsTime = (double) duration / 1000000;   //convert to ms
        System.out.println(String.format("BFS Algorithm Time %1.3f ms", bfsTime));
        System.out.println("Number of nodes traversed in BFS Algorithm: " + nodeCount); // Print the number of nodes traversed

    }

    public void Astar(Node start, Node targetNode, int graphWidth, int graphHeight) {
        startTime = System.nanoTime();
        PriorityQueue<Node> queue = new PriorityQueue<>((n1, n2) -> {
            double f1 = Node.distance(n1, targetNode) + Node.distance(start, n1);
            double f2 = Node.distance(n2, targetNode) + Node.distance(start, n2);
            return Double.compare(f1, f2);
        });
        Node[][] prev = new Node[graphWidth][graphHeight];
        queue.add(start);
        nodeCount = 0;
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if (curNode.isEnd()) {
                curNode.setColor(Color.MAGENTA);
                break;
            }
            curNode.setColor(Color.ORANGE);
            curNode.setColor(Color.BLUE);
            nodeCount++;
            for (Node adjacent : curNode.getNeighbours()) {
                if (adjacent.isSearched()) {
                    continue;
                }
                double f1 = Node.distance(adjacent, targetNode);
                double h1 = Node.distance(curNode, start);

                double f2 = Node.distance(adjacent, targetNode);
                double h2 = Node.distance(prev[adjacent.getX()][adjacent.getY()], start);

                if (!queue.contains(adjacent) || (f1 + h1 < f2 + h2)) {
                    prev[adjacent.getX()][adjacent.getY()] = curNode;
                    if (!queue.contains(adjacent)) {
                        queue.add(adjacent);
                    }
                }
            }
        }
        shortpath(prev, targetNode);
        if (!queue.isEmpty()) {              //you exited before the queue was emptied, meaning that you found the solution and exited.
             stopTime = System.nanoTime(); 
        }
        duration = stopTime - startTime;
        AstarTime = (double) duration / 1000000;
        System.out.println(String.format("A* Algorithm Time %1.3f ms", AstarTime));
        System.out.println("Number of nodes traversed in A* Algorithm: " + nodeCount);
    }

    private void shortpath(Node[][] prev, Node end) {
        Node pathConstructor = end;
        while (pathConstructor != null) {
            pathConstructor = prev[pathConstructor.getX()][pathConstructor.getY()];
            if (pathConstructor != null) {
                pathConstructor.setColor(Color.ORANGE);
            }
        }
    }
}
