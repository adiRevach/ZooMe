package com.afeka.ZooMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class appoloniaMap extends AppCompatActivity {

    Button buttonMap,buttonDesription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appolonia_map);

        buttonMap = findViewById(R.id.buttonMap1);
        buttonDesription = findViewById(R.id.buttonDesription1);

        buttonDesription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(appoloniaMap.this, appolonia.class);
                startActivity(i);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Intent home = new Intent(appoloniaMap.this, MainActivity2.class);
                startActivity(home);
                return true;

            case R.id.menu2:
                Intent apo = new Intent(appoloniaMap.this, appolonia.class);
                startActivity(apo);

                return true;
            case R.id.menu3:
                Intent help = new Intent(appoloniaMap.this, help.class);
                startActivity(help);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}