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
        this.maze = maze; // 미로 배열 저장
        Log.d("MazeView", "Maze set with dimensions: " + maze.length + "x" + maze[0].length); // 로그 추가
        invalidate(); // 화면 갱신
    }

    public void setCellSize(int size) {
        this.cellSize = size; //셀 크기 저장
        invalidate(); // 화면 갱신
    }

    // 출구 좌표 설정 메서드
    public void setExitPosition(int row, int col) {
        this.exitRow = row;
        this.exitCol = col;
    }

    public void setPath(ArrayList<int[]> path) {
        this.path = path;
        Log.d("MazeView", "Path set with size: " + (path != null ? path.size() : "null")); // 로그 추가
        invalidate(); // 화면 갱신
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("MazeView", "onDraw called");  // onDraw가 호출되는지 확인
        super.onDraw(canvas);
        Paint paint = new Paint();

        if (maze != null) {
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[row].length; col++) {
                    if (maze[row][col] == 1) { // 벽 = 검은색
                        paint.setColor(Color.BLACK);
                    } else { // 길 = 하얀색
                        paint.setColor(Color.WHITE);
                    } //셀 그리기
                    canvas.drawRect(col * cellSize, row * cellSize,
                            (col + 1) * cellSize, (row + 1) * cellSize, paint);
                }
            }
            // 출구에 "Exit" 텍스트 그리기
            paint.setColor(Color.GREEN);
            paint.setTextSize(cellSize / 2);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Exit", (exitCol + 0.5f) * cellSize, (exitRow + 0.75f) * cellSize, paint);
        }

        // 최단 경로를 파란색으로 표시
        if (path != null) {
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5); // 선의 두께 설정
            for (int i = 0; i < path.size() - 1; i++) {
                int[] start = path.get(i);
                int[] end = path.get(i + 1);
                // 셀의 왼쪽 상단 모서리를 기준으로 하는 픽셀 좌표를 셀의 중심으로 맞추어 선이 셀의 중앙을 지나도록 만듦
                float startX = start[1] * cellSize + cellSize / 2; // start[1] * cellSize는 start의 열 위치를 픽셀 좌표로 변환
                float startY = start[0] * cellSize + cellSize / 2;
                float endX = end[1] * cellSize + cellSize / 2;
                float endY = end[0] * cellSize + cellSize / 2;
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
        }
    }
}
