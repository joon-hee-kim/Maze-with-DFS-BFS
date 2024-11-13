package com.example.mazehyun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MazeView extends View {
    private int[][] maze;
    private int cellSize;
    private int exitRow;
    private int exitCol;
    private ArrayList<int[]> path;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMaze(int[][] maze) {
        this.maze = maze; // Save the maze array
        Log.d("MazeView", "Maze set with dimensions: " + maze.length + "x" + maze[0].length); // Add log
        invalidate(); // Refresh the screen
    }

    public void setCellSize(int size) {
        this.cellSize = size; // Save cell size
        invalidate(); // Refresh the screen
    }

    // Method to set exit coordinates
    public void setExitPosition(int row, int col) {
        this.exitRow = row;
        this.exitCol = col;
    }

    public void setPath(ArrayList<int[]> path) {
        this.path = path;
        Log.d("MazeView", "Path set with size: " + (path != null ? path.size() : "null")); // Add log
        invalidate(); // Refresh the screen
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("MazeView", "onDraw called");  // Check if onDraw is called
        super.onDraw(canvas);
        Paint paint = new Paint();

        if (maze != null) {
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[row].length; col++) {
                    if (maze[row][col] == 1) { // Wall = Black
                        paint.setColor(Color.BLACK);
                    } else { // Path = White
                        paint.setColor(Color.WHITE);
                    }
                    // Draw cell
                    canvas.drawRect(col * cellSize, row * cellSize,
                            (col + 1) * cellSize, (row + 1) * cellSize, paint);
                }
            }
            // Draw "Exit" text at the exit
            paint.setColor(Color.GREEN);
            paint.setTextSize(cellSize / 2);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Exit", (exitCol + 0.5f) * cellSize, (exitRow + 0.75f) * cellSize, paint);
        }

        // Display the shortest path in blue
        if (path != null) {
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5); // Set line thickness
            for (int i = 0; i < path.size() - 1; i++) {
                int[] start = path.get(i);
                int[] end = path.get(i + 1);
                // Adjust pixel coordinates based on the top-left corner of the cell to make the line pass through the center of the cell
                float startX = start[1] * cellSize + cellSize / 2; // start[1] * cellSize converts the start column position to pixel coordinates
                float startY = start[0] * cellSize + cellSize / 2;
                float endX = end[1] * cellSize + cellSize / 2;
                float endY = end[0] * cellSize + cellSize / 2;
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
        }
    }
}
