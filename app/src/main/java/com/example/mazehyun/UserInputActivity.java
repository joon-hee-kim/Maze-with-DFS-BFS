package com.example.mazehyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserInputActivity extends AppCompatActivity {

    private EditText rowInput;
    private EditText colInput;
    private Button inputButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        rowInput = findViewById(R.id.row_input);
        colInput = findViewById(R.id.col_input);
        inputButton = findViewById(R.id.btn_input);

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered rows and columns
                String rowText = rowInput.getText().toString();
                String colText = colInput.getText().toString();

                // Check if input values are empty
                if (rowText.isEmpty() || colText.isEmpty()) {
                    Toast.makeText(UserInputActivity.this, "Please enter both rows and columns.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rows = Integer.parseInt(rowText);
                int cols = Integer.parseInt(colText);

                // Move to MazeActivity and pass input values
                Intent intent = new Intent(UserInputActivity.this, MazeActivity.class);
                intent.putExtra("rows", rows);
                intent.putExtra("cols", cols);
                startActivity(intent);
            }
        });
    }
}
