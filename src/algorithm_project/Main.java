package algorithm_project;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Main extends Canvas implements Runnable, MouseListener {

    private static Node start = null;
    private static Node target = null;
    private static JFrame frame;

    private Node[][] nodeList;
    private static Main runTimeMain;
    private static Algorithm algorithm;

    private final static int WIDTH = 1000;
    private final static int HEIGHT = 750;

    private final static int NODES_WIDTH = 28;
    private final static int NODES_HEIGHT = 19;

    public static void main(String[] args) {

        frame = new JFrame("Maze Solver");
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLayout(null);
        Main m = new Main();
        algorithm = new Algorithm();
        m.setBounds(0, 25, WIDTH, HEIGHT);
        SetupMenu(frame);
        runTimeMain = m;
        frame.add(m);
        frame.setVisible(true);
        m.start();
    }

    public static void SetupMenu(JFrame frame) {
        JMenuBar bar = new JMenuBar();
        bar.setBounds(0, 0, WIDTH, 25);
        frame.add(bar);
        JMenu fileMenu = new JMenu("File");
        bar.add(fileMenu);
        JMenu boardMenu = new JMenu("Board");
        bar.add(boardMenu);
        JMenu algorithmsMenu = new JMenu("Algorithms");
        bar.add(algorithmsMenu);
        JMenuItem openMaze = new JMenuItem("Open Maze");
        JMenuItem clearSearch = new JMenuItem("Clear Search Results");
        JMenuItem bfsItem = new JMenuItem("Breadth-First Search");
        JMenuItem dfsItem = new JMenuItem("Depth-First Search");
        JMenuItem AstarItem = new JMenuItem("A-star Search");
        
        openMaze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                try {
                    runTimeMain.openMaze();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        clearSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                runTimeMain.clearSearchResults();
            }
        });

        bfsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                algorithm.bfs(runTimeMain.start, runTimeMain.target, runTimeMain.NODES_WIDTH,
                        runTimeMain.NODES_HEIGHT);
            }
        });
        dfsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                algorithm.dfs(runTimeMain.getStart());

            }
        });
        AstarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                algorithm.Astar(runTimeMain.start, runTimeMain.target, runTimeMain.NODES_WIDTH,
                        runTimeMain.NODES_HEIGHT);
            }
        });
        fileMenu.add(openMaze);
        boardMenu.add(clearSearch);
        algorithmsMenu.add(dfsItem);
        algorithmsMenu.add(bfsItem);
        algorithmsMenu.add(AstarItem);
    }

    //Initializes the program and enters the game loop.
    public void run() {
        init();
        while (true) {
            BufferStrategy bs = getBufferStrategy(); // check
            if (bs == null) {
                createBufferStrategy(2);
                continue;
            }
            Graphics2D grap = (Graphics2D) bs.getDrawGraphics(); // check
            render(grap);
            bs.show();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Initializes the program by setting focus to the canvas, 
    //adding a mouse listener, creating a grid of nodes, and setting directions for each node.
    public void init() {
        requestFocus();
        addMouseListener(this);
        nodeList = new Node[NODES_WIDTH][NODES_HEIGHT];
        createNodes(false);
        setMazeDirections();
    }

    //Sets directions for each node in the grid based on its neighboring nodes.
    public void setMazeDirections() {
        for (int i = 0; i < nodeList.length; i++) {
            for (int j = 0; j < nodeList[i].length; j++) {
                Node up = null, down = null, left = null, right = null;
                int u = j - 1;
                int d = j + 1;
                int l = i - 1;
                int r = i + 1;

                if (u >= 0) {
                    up = nodeList[i][u];
                }
                if (d < NODES_HEIGHT) {
                    down = nodeList[i][d];
                }
                if (l >= 0) {
                    left = nodeList[l][j];
                }
                if (r < NODES_WIDTH) {
                    right = nodeList[r][j];
                }

                nodeList[i][j].setDirections(left, right, up, down);
            }
        }
    }

    public void createNodes(boolean ref) {
        for (int i = 0; i < nodeList.length; i++) {
            for (int j = 0; j < nodeList[i].length; j++) {
                if (!ref) {
                    nodeList[i][j] = new Node(i, j).setX(15 + i * 35).setY(15 + j * 35);
                }
                nodeList[i][j].clearNode();
            }
        }
    }

    public void openMaze() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line = null;
            for (int i = 0; i < NODES_WIDTH; i++) {
                line = reader.readLine();
                for (int j = 0; j < NODES_HEIGHT; j++) {

                    //nodeList[i][j].setColor(Color.BLACK);
                    int nodeType = Character.getNumericValue(line.charAt(j));
                    switch (nodeType) {
                        case 0:
                            nodeList[i][j].setColor(Color.LIGHT_GRAY);
                            break;
                        case 1:
                            nodeList[i][j].setColor(Color.BLACK);
                            break;

                        case 2:
                            nodeList[i][j].setColor(Color.GREEN);
                            start = nodeList[i][j];
                            break;
                        case 3:
                            nodeList[i][j].setColor(Color.RED);
                            target = nodeList[i][j];
                            break;
                    }
                }

            }
            reader.close();
        }
    }
    // Clears the search results by resetting the color of nodes that were marked as searched.

    public void clearSearchResults() {
        for (int i = 0; i < nodeList.length; i++) {
            for (int j = 0; j < nodeList[i].length; j++) {
                if (nodeList[i][j].isSearched()) {
                    nodeList[i][j].clearNode();
                }
            }
        }
        target.setColor(Color.RED);
        start.setColor(Color.GREEN);
    }

    //Renders graphics onto the canvas.
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < nodeList.length; i++) {
            for (int j = 0; j < nodeList[i].length; j++) {
                nodeList[i][j].render(g);
            }
        }
    }

    //Starts the program by creating and starting a new thread.
    public void start() {
        new Thread(this).start();
    }

    public void mousePressed(MouseEvent e) {
        Node clickedNode = getNodeAt(e.getX(), e.getY());
        if (clickedNode == null) {
            return;
        }

        if (clickedNode.isWall()) {
            clickedNode.clearNode();
            return;
        }

        clickedNode.Clicked(e.getButton());

        if (clickedNode.isEnd()) {
            if (target != null) {
                target.clearNode();
            }
            target = clickedNode;
        } else if (clickedNode.isStart()) {

            if (start != null) {
                start.clearNode();
            }
            start = clickedNode;
        }

    }

    //Returns the start node from the grid of nodes.
    private Node getStart() {
        for (int i = 0; i < nodeList.length; i++) {
            for (int j = 0; j < nodeList[i].length; j++) {
                if (nodeList[i][j].isStart()) {
                    return nodeList[i][j];
                }
            }
        }
        return null;
    }

    //Returns the node at the specified coordinates on the canvas.
    public Node getNodeAt(int x, int y) {
        x -= 15;
        x /= 35;
        y -= 15;
        y /= 35;
        if (x >= 0 && y >= 0 && x < nodeList.length && y < nodeList[x].length) {
            return nodeList[x][y];
        }
        return null;
    }

    //These methods are required by the MouseListener interface 
    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}