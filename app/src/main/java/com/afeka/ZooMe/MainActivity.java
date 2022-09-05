package com.afeka.ZooMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

public class MainActivity extends AppCompatActivity {

    EditText user;
    Button signup;

    TextView hello;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference1 = database.getReference();

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.username);
        signup = findViewById(R.id.signup);

        hello = findViewById(R.id.hello);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user.getText().toString();
                Intent i = new Intent(MainActivity.this,MainActivity2.class);

                //i.putExtra("userName",userName);
                saveData();
                startActivity(i);

            }
        });

    }
    public void saveData(){
        sharedPreferences = getSharedPreferences("saveName",MODE_PRIVATE);
        String userName = user.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName",userName);
        editor.commit();
    }
}