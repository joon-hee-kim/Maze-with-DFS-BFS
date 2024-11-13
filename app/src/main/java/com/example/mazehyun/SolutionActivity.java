package com.example.mazehyun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.util.Log;

public class SolutionActivity extends AppCompatActivity {
    private MazeView mazeView;  // Changed to MazeView
    private ArrayList<int[]> shortestPath; // To store the shortest path found by BFS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        Log.d("SolutionActivity", "check complete");

        mazeView = findViewById(R.id.MazeViewSolution);  // Changed to MazeView
        Log.d("SolutionActivity", "check complete1");

        // Receive and convert data passed via Intent
        ArrayList<ArrayList<Integer>> mazeList = (ArrayList<ArrayList<Integer>>) getIntent().getSerializableExtra("maze");
        shortestPath = (ArrayList<int[]>) getIntent().getSerializableExtra("shortestPath");
        Log.d("SolutionActivity", "Shortest path size: " + (shortestPath != null ? shortestPath.size() : "null"));

        // Convert 2D ArrayList to int[][] array
        int[][] maze = new int[mazeList.size()][mazeList.get(0).size()];
        for (int i = 0; i < mazeList.size(); i++) {
            for (int j = 0; j < mazeList.get(i).size(); j++) {
                maze[i][j] = mazeList.get(i).get(j);
            }
        }
        Log.d("SolutionActivity", "check complete2");

        int mazeSize = getIntent().getIntExtra("mazeSize", 1000);
        int rows = getIntent().getIntExtra("rows", 10);
        int cols = getIntent().getIntExtra("cols", 10);
        int exitRow = getIntent().getIntExtra("exitRow", 0);
        int exitCol = getIntent().getIntExtra("exitCol", 0);
        int cellSize = mazeSize / Math.max(rows, cols); // Cell size = map size / max(rows, cols)

        // Set up MazeView
        mazeView.setMaze(maze); // Generate maze
        mazeView.setCellSize(cellSize); // Set maze cell size
        mazeView.setExitPosition(exitRow, exitCol); // Set exit position
        mazeView.setPath(shortestPath); // Set shortest path
        mazeView.invalidate(); // Refresh the screen

        // Add log: check data
        Log.d("SolutionActivity", "Maze array size: " + maze.length + "x" + maze[0].length);
        Log.d("SolutionActivity", "Shortest path size: " + (shortestPath != null ? shortestPath.size() : "null"));
    }
}
