package com.example.mazehyun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewTreeObserver;
import java.util.Random;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class MazeActivity extends AppCompatActivity {
    // Maze
    private int[][] maze;
    private int rows;
    private int cols;

    private int characterRow = 1; // Starting position (1, 1)
    private int characterCol = 1;
    private int exitRow;
    private int exitCol;
    private ImageView characterImageView; // Character image
    private int buttonCount = 0; // Number of button presses
    private TextView countTextView; // Displays button press count
    private int cellSize; // Size of maze cells
    private int mazeSize = 1000; // Total size of the maze
    private MazeView mazeView;
    private ArrayList<int[]> shortestPath; // To store the shortest path


    // Allows the user to set the maze to a desired size
    // Generates a maze using DFS (randomly places walls and paths while ensuring a path to the exit)
    public void generateRandomMaze(int rows, int cols) {
        Random random = new Random();
        maze = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1; // Initialize all cells as walls
            }
        }

        exitRow = rows - 2;
        exitCol = cols - 2;

        // Generate the maze randomly using DFS
        dfsGenerate(1, 1);

        // Set entrance and exit as paths
        maze[1][1] = 0; // Entrance
        maze[exitRow][exitCol] = 0; // Exit
    }

    private void dfsGenerate(int row, int col) {
        maze[row][col] = 0; // Make the current position a path

        // Create a list of directions in a random order to move
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{1, 0});  // Down
        directions.add(new int[]{-1, 0}); // Up
        directions.add(new int[]{0, 1});  // Right
        directions.add(new int[]{0, -1}); // Left
        Collections.shuffle(directions);  // Shuffle directions randomly

        for (int[] direction : directions) {
            int newRow = row + direction[0] * 2; // Move by two cells
            int newCol = col + direction[1] * 2;

            // Move only if the new position is within maze boundaries and has not been visited
            if (isInMaze(newRow, newCol) && maze[newRow][newCol] == 1) {
                // Before moving two cells, break the wall (one cell away) to create a path
                int wallRow = row + direction[0];
                int wallCol = col + direction[1];

                // Check if the wall is within maze boundaries and break the wall
                if (isInMaze(wallRow, wallCol)) {
                    maze[wallRow][wallCol] = 0; // Break the wall
                }

                // Recursive call
                dfsGenerate(newRow, newCol);
            }
        }
    }

    // Method to check if a new position is within the maze boundaries
    private boolean isInMaze(int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length;
    }

    class Node {
        int x, y, steps;
        Node prev;

        Node(int x, int y, int steps, Node prev) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.prev = prev;
        }
    }

    // Find the shortest path using the BFS algorithm
    private ArrayList<int[]> findShortestPath() {
        int[] dx = {-1, 1, 0, 0}; // Up, down, left, right
        int[] dy = {0, 0, -1, 1};
        int mazeRows = maze.length;  // Number of rows in the maze
        int mazeCols = maze[0].length; // Number of columns in the maze
        boolean[][] visited = new boolean[mazeRows][mazeCols]; // Array to check if visited

        Queue<Node> queue = new LinkedList<>(); // Initialize queue
        queue.add(new Node(1, 1, 0, null)); // Modify here if needed
        visited[1][1] = true; //

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int x = current.x;
            int y = current.y;

            // When the destination is reached
            if (x == exitRow && y == exitCol) {
                ArrayList<int[]> path = new ArrayList<>();

                // Backtrack the path and store in ArrayList
                while (current != null) {
                    path.add(0, new int[]{current.x, current.y}); // Add in reverse order
                    current = current.prev;
                }

                return path; // Return the path as ArrayList<int[]>
            }

            // Explore in four directions
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                // Check if within maze, unvisited, and is a path
                if (nx >= 0 && nx < mazeRows && ny >= 0 && ny < mazeCols && !visited[nx][ny] && maze[nx][ny] == 0) {
                    visited[nx][ny] = true;
                    Node nextNode = new Node(nx, ny, current.steps + 1, current);
                    queue.add(nextNode);
                }
            }
        }
        // If no path is found
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // Retrieve row and column values passed via Intent
        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 10); // Default value is 10
        cols = intent.getIntExtra("cols", 10); // Default value is 10

        // Generate maze based on row and column values
        generateRandomMaze(rows, cols);

        cellSize = mazeSize / Math.max(rows, cols); // Cell size = map size / max(row, col)

        MazeView mazeView = findViewById(R.id.MazeView);
        mazeView.setMaze(maze); // Generate maze
        mazeView.setCellSize(cellSize);  // Set maze cell size to accept cellSize as parameter
        mazeView.setExitPosition(exitRow, exitCol); // Set exit position

        characterImageView = findViewById(R.id.character);
        countTextView = findViewById(R.id.button_count);

        // Adjust character size to match cell size
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) characterImageView.getLayoutParams();
        layoutParams.width = cellSize;
        layoutParams.height = cellSize;
        characterImageView.setLayoutParams(layoutParams);

        Button upButton = findViewById(R.id.btn_up);
        Button downButton = findViewById(R.id.btn_down);
        Button leftButton = findViewById(R.id.btn_left);
        Button rightButton = findViewById(R.id.btn_right);

        // Set the same listener for all buttons
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCount++; // Increase button press count
                countTextView.setText(String.valueOf(buttonCount)); // Update TextView

                // Check which button was pressed and move character
                if (v == upButton) {
                    moveCharacter(characterRow - 1, characterCol);
                } else if (v == downButton) {
                    moveCharacter(characterRow + 1, characterCol);
                } else if (v == leftButton) {
                    moveCharacter(characterRow, characterCol - 1);
                } else if (v == rightButton) {
                    moveCharacter(characterRow, characterCol + 1);
                }
            }
        };

        // Link buttons and listener
        upButton.setOnClickListener(buttonClickListener);
        downButton.setOnClickListener(buttonClickListener);
        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);

        Button btn_solution = findViewById(R.id.btn_solution);
        btn_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<int[]> shortestPath = findShortestPath();
                // Log coordinates of shortest path in Logcat
                if (shortestPath != null) {
                    for (int[] coordinates : shortestPath) {
                        Log.d("MazeActivity", "Path Coordinate: (" + coordinates[0] + ", " + coordinates[1] + ")");
                    }
                } else {
                    Log.d("MazeActivity", "No path found.");
                }
                if (shortestPath != null) {
                    // Convert maze to ArrayList format
                    ArrayList<ArrayList<Integer>> mazeList = new ArrayList<>();
                    // Traverse each row in maze. Here row refers to a specific row in maze
                    for (int[] row : maze) {
                        ArrayList<Integer> rowList = new ArrayList<>();
                        // cell refers to each int value in the row array.
                        for (int cell : row) {
                            rowList.add(cell);
                        }
                        mazeList.add(rowList);
                    }

                    cellSize = mazeSize / Math.max(rows, cols);
                    Intent intent = new Intent(MazeActivity.this, SolutionActivity.class);
                    intent.putExtra("maze", mazeList); // Pass maze data
                    intent.putExtra("shortestPath", shortestPath); // Pass shortest path
                    intent.putExtra("mazeSize", mazeSize);
                    intent.putExtra("rows", rows);
                    intent.putExtra("cols", cols);
                    intent.putExtra("exitRow", exitRow);
                    intent.putExtra("exitCol", exitCol);
                    startActivity(intent);
                } else {
                    Toast.makeText(MazeActivity.this, "No path to the exit.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adjust initial character position (using updateCharacterPosition(); resolves initial slight misalignment of the character image at the starting position)
        characterImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Set initial position so character is centered in the cell
                updateCharacterPosition();

                // Remove listener (execute only once)
                characterImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void moveCharacter(int newRow, int newCol) {
        // Check if the new position is valid (within maze boundaries & is a path (0))
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] == 0) {
            characterRow = newRow; // Update position
            characterCol = newCol;
            updateCharacterPosition(); // Update character position

            // Check if the exit is reached
            if (characterRow == exitRow && characterCol == exitCol) {
                Toast.makeText(this, "Congratulations! You've reached the exit!", Toast.LENGTH_LONG).show();
                finish(); // End game
            }
        } else {
            Toast.makeText(this, "It's a wall!", Toast.LENGTH_SHORT).show(); // Show message if it's a wall
        }
    }

    // x-coordinate increases as you move right, and y-coordinate increases as you move down
    private void updateCharacterPosition() {
        // Update the position of the character image
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) characterImageView.getLayoutParams();
        params.leftMargin = characterCol * cellSize + (cellSize / 2) - (characterImageView.getWidth() / 2); // Center character on x-axis in the cell
        params.topMargin = characterRow * cellSize + (cellSize / 2) - (characterImageView.getHeight() / 2); // Center character on y-axis in the cell
        characterImageView.setLayoutParams(params);
    }
}
