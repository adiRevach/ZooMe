package com.afeka.ZooMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
                Intent home = new Intent(help.this, MainActivity2.class);
                startActivity(home);
                return true;

            case R.id.menu2:
                Intent apo = new Intent(help.this, appolonia.class);
                startActivity(apo);

                return true;
            case R.id.menu3:
                Intent help = new Intent(help.this, help.class);
                startActivity(help);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}