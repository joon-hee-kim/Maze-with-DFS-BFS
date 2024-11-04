package com.example.mazehyun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.util.Log;

public class SolutionActivity extends AppCompatActivity {
    private MazeView mazeView;
    private ArrayList<int[]> shortestPath; // BFS로 찾은 최단 경로 저장용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        mazeView = findViewById(R.id.MazeViewSolution);

        // Intent로 전달된 데이터를 받아서 변환
        ArrayList<ArrayList<Integer>> mazeList = (ArrayList<ArrayList<Integer>>) getIntent().getSerializableExtra("maze");
        shortestPath = (ArrayList<int[]>) getIntent().getSerializableExtra("shortestPath");

        // 2D ArrayList를 int[][] 배열로 변환
        int[][] maze = new int[mazeList.size()][mazeList.get(0).size()];
        for (int i = 0; i < mazeList.size(); i++) {
            for (int j = 0; j < mazeList.get(i).size(); j++) {
                maze[i][j] = mazeList.get(i).get(j);
            }
        }

        // 로그 추가: 데이터 확인
        Log.d("SolutionActivity", "Maze array size: " + maze.length + "x" + maze[0].length);
        Log.d("SolutionActivity", "Shortest path size: " + (shortestPath != null ? shortestPath.size() : "null"));

        // MazeView에 데이터 설정 후 invalidate로 갱신
        mazeView.setMaze(maze);
        mazeView.setPath(shortestPath);
        mazeView.invalidate();
    }
}
