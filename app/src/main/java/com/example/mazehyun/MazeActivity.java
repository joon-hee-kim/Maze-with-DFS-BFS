package com.example.mazehyun;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
    //미로
    private int[][] maze;
    private int rows;
    private int cols;

    private int characterRow = 1; // 시작 위치 (1, 1)
    private int characterCol = 1;
    private int exitRow;
    private int exitCol;
    private ImageView characterImageView; //캐릭터 이미지
    private int buttonCount = 0; //버튼 누른 횟수
    private TextView countTextView; //버튼 누른 횟수 표기
    private int cellSize; //미로 셀의 크기
    private int mazeSize = 1000; //미로 전체 크기
    private MazeView mazeView;


    // 사용자가 원하는 크기로 미로 설정 가능하고
    // DFS 기반(미로를 생성하면서 무작위로 벽과 길을 배치하지만, 캐릭터가 출구까지 도달할 수 있는 경로가 보장)으로
    // 미로를 생성하는 메서드
    public void generateRandomMaze(int rows, int cols) {
        Random random = new Random();
        maze = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1; // 모든 셀을 벽으로 초기화
            }
        }

        exitRow = rows - 2;
        exitCol = cols - 2;

        // DFS를 사용하여 랜덤하게 미로를 생성
        dfsGenerate(1, 1);

        // 입구와 출구를 길로 설정
        maze[1][1] = 0; // 입구
        maze[exitRow][exitCol] = 0; // 출구
    }

    private void dfsGenerate(int row, int col) {
        maze[row][col] = 0; // 현재 위치를 길로 만듦

        // 랜덤한 방향 순서로 이동하기 위해 방향 리스트를 만듦
        // List<int[]>는 int[] 타입 배열들을 담을 수 있는 리스트
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{1, 0});  // 아래쪽 (행 증가, 열 그대로)
        directions.add(new int[]{-1, 0}); // 위쪽 (행 감소, 열 그대로)
        directions.add(new int[]{0, 1});  // 오른쪽 (행 그대로, 열 증가)
        directions.add(new int[]{0, -1}); // 왼쪽 (행 그대로, 열 감소)
        Collections.shuffle(directions);  // 방향을 랜덤하게 섞음

        for (int[] direction : directions) {
            // direction[0]과 direction[1]은 해당 방향의 행, 열 이동을 나타냄
            int newRow = row + direction[0] * 2; // 두 칸씩 이동
            int newCol = col + direction[1] * 2;

            // 새로운 위치가 미로 범위 내에 있고, 아직 방문하지 않은 경우에만 이동
            if (isInMaze(newRow, newCol) && maze[newRow][newCol] == 1) {
                // 두 칸을 이동한 위치로 가기 전, 중간에 있는 벽(한 칸 떨어진 위치)을 허물어 길을 만듦
                maze[row + direction[0]][col + direction[1]] = 0;
                dfsGenerate(newRow, newCol); // 재귀적으로 DFS 호출
            }
        }
    }

    // 미로의 범위를 체크하는 메서드
    // row, col > 0 조건으로 하는 이유는 미로의 외곽 부분을 벽으로 유지하기 위함 (모든 길은 미로 내부에서만)
    private boolean isInMaze(int row, int col) {
        return row > 0 && row < maze.length && col > 0 && col < maze[0].length;
    }

    // BFS 알고리즘을 이용해 최단 경로 찾기
    private ArrayList<int[]> findShortestPath() {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        boolean[][] visited = new boolean[rows][cols]; // 미로의 각 위치에 대한 방문 여부
        Queue<int[]> queue = new LinkedList<>(); // 각 셀의 위치를 큐에 넣어 순차적으로 탐색. FIFO
        ArrayList<int[]> path = new ArrayList<>(); // 최단 경로를 저장하는 리스트

        // 경로를 역추적하기 위한 부모 정보 저장
        // (각 위치 (row, col)에 도달하기 직전 위치를 기록하여, 출구에 도달했을 때 역추적하여 최단 경로를 구성할 수 있게 함)
        int[][] parent = new int[rows * cols][2];

        queue.add(new int[]{characterRow, characterCol}); // 시작 위치 (characterRow, characterCol)
        visited[characterRow][characterCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll(); // 큐의 맨 앞에 있는 위치를 꺼내 현재 위치로 설정
            int row = current[0];
            int col = current[1];

            // 출구에 도달했을 경우
            if (row == exitRow && col == exitCol) {
                // 경로를 추적하여 저장
                while (row != characterRow || col != characterCol) {
                    path.add(0, new int[]{row, col}); // path 리스트 맨 앞에 현재 위치를 추가하여 최단 경로를 구성
                    int index = row * cols + col; // 현재 위치 (row, col)을 1차원 배열 인덱스로 변환하기 위한 값. (cols: 미로의 크기)
                    row = parent[index][0];
                    col = parent[index][1];
                }
                path.add(0, new int[]{characterRow, characterCol});
                return path;
            }

            // 네 방향으로 이동
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && maze[newRow][newCol] == 0 && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                    // parent 배열의 인덱스 newRow * cols + newCol에 현재 위치 (row, col)을 저장하여,
                    // 이동할 다음 위치의 부모가 현재 위치임을 기록
                    parent[newRow * cols + newCol] = new int[]{row, col};
                }
            }
        }
        return null; // 출구까지 경로가 없는 경우
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // Intent로 전달된 행과 열 값 가져오기
        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 10); // 기본값 10
        cols = intent.getIntExtra("cols", 10); // 기본값 10

        // 행과 열 값에 따라 미로 생성
        generateRandomMaze(rows, cols);

        cellSize = mazeSize / Math.max(rows, cols); //셀 크기 = 맵 크기 / max(행,열)

        MazeView mazeView = findViewById(R.id.MazeView);
        mazeView.setMaze(maze); //미로 생성
        mazeView.setCellSize(cellSize);  //미로 셀 크기 설정 (cellSize)를 인수로 받을 수 있게 수정
        mazeView.setExitPosition(exitRow, exitCol); // 출구 위치 설정

        characterImageView = findViewById(R.id.character);
        countTextView = findViewById(R.id.button_count);

        // 캐릭터 크기를 셀 크기에 맞추기
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) characterImageView.getLayoutParams();
        layoutParams.width = cellSize;
        layoutParams.height = cellSize;
        characterImageView.setLayoutParams(layoutParams);

        Button upButton = findViewById(R.id.btn_up);
        Button downButton = findViewById(R.id.btn_down);
        Button leftButton = findViewById(R.id.btn_left);
        Button rightButton = findViewById(R.id.btn_right);

        // 모든 버튼에 동일한 리스너 설정
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCount++; // 버튼 누른 횟수 증가
                countTextView.setText(String.valueOf(buttonCount)); // TextView 업데이트

                //어떤 버튼 눌렸는지 확인 후 캐릭터 위치 이동
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

        //버튼과 리스너 연결
        upButton.setOnClickListener(buttonClickListener);
        downButton.setOnClickListener(buttonClickListener);
        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);

        Button btn_solution = findViewById(R.id.btn_solution);
        btn_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<int[]> shortestPath = findShortestPath();

                if (shortestPath != null) {
                    // 미로를 ArrayList 형태로 변환
                    ArrayList<ArrayList<Integer>> mazeList = new ArrayList<>();
                    // maze의 각 행을 순회. 여기서 row는 maze의 특정 행을 가리킴
                    for (int[] row : maze) {
                        ArrayList<Integer> rowList = new ArrayList<>();
                        // cell은 row 배열에 있는 각각의 int 값입니다.
                        for (int cell : row) {
                            rowList.add(cell);
                        }
                        mazeList.add(rowList);
                    }

                    Intent intent = new Intent(MazeActivity.this, SolutionActivity.class);
                    intent.putExtra("maze", mazeList); // 미로 데이터 전달
                    intent.putExtra("shortestPath", shortestPath); // 최단 경로 전달
                    startActivity(intent);
                } else {
                    Toast.makeText(MazeActivity.this, "출구까지의 경로가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //캐릭터 초기 위치 조정(updateCharacterPosition();만 쓰면, 한 번 이동한 다음부터는 괜찮은데 초기 위치에서의 캐릭터 이미지 위치가 살짝 어긋남)
        characterImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 캐릭터가 셀 중앙에 위치하도록 초기 위치 설정
                updateCharacterPosition();

                // 리스너 제거 (한 번만 실행되도록)
                characterImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void moveCharacter(int newRow, int newCol) {
        // 새로운 위치가 유효한지 체크 (미로 범위 안에 있는지 & 길(0)이 맞는지)
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] == 0) {
            characterRow = newRow; // 위치 업데이트
            characterCol = newCol;
            updateCharacterPosition(); // 캐릭터 위치 업데이트

            // 출구에 도달했는지 확인
            if (characterRow == exitRow && characterCol == exitCol) {
                Toast.makeText(this, "축하합니다! 출구에 도달했습니다!", Toast.LENGTH_LONG).show();
                finish(); // 게임 종료
            }
        } else {
            Toast.makeText(this, "벽입니다!", Toast.LENGTH_SHORT).show(); // 벽일 경우 메시지 출력
        }
    }

    // x좌표는 오른쪽으로 갈수록 증가하며 y좌표는 아래쪽으로 갈수록 증가함
    private void updateCharacterPosition() {
        // 캐릭터 이미지의 위치를 업데이트
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) characterImageView.getLayoutParams();
        params.leftMargin = characterCol * cellSize + (cellSize / 2) - (characterImageView.getWidth() / 2); //캐릭터 x좌표를 셀 중앙으로
        params.topMargin = characterRow * cellSize + (cellSize / 2) - (characterImageView.getHeight() / 2);; //캐릭터 y좌표를 셀 중앙으로
        characterImageView.setLayoutParams(params);
    }
}