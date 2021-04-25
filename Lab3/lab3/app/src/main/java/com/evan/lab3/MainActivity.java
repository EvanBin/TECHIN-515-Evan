package com.evan.lab3;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.content.Intent;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    private Button btnSignUp, btnSignIn;
    private EditText editText, editText1;
    private CheckBox checkBox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);

        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        checkBox1 = findViewById(R.id.checkBox);
        editText1.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btnSignUp = findViewById(R.id.btnAdd);
        btnSignIn = findViewById(R.id.btnView);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = editText.getText().toString();
                String pass = editText1.getText().toString();
                if ((editText.length() != 0)&&(editText1.length() != 0)) {
                    SignUp(user,pass);
                    editText.setText("");
                    editText1.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

        checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) editText1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else editText1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    public void SignUp(String user, String pass) {
        boolean insertData = mDatabaseHelper.insertData(user,pass);
        if (insertData) {
            toastMessage("User Successfully Signed Up!");
        } else {
            toastMessage("Something went wrong...");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}