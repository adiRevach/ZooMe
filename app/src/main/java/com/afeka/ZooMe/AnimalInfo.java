package com.afeka.ZooMe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.afeka.ZooMe.databinding.ActivityAnimalInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class AnimalInfo extends AppCompatActivity {

    private StorageReference imStorageRef;


    TextView animalTitle;
    TextView animalDescription;
    TextView animalDidYouKnow;
    ImageView animalPhoto;
    String animal;

    //path to the database
    //realtime
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    //storage
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_info);

        animalTitle = findViewById(R.id.animalTitle);
        animalDescription = findViewById(R.id.animalDescription);
        animalDidYouKnow = findViewById(R.id.animalDidYouKnow);
        animalPhoto = findViewById(R.id.animalPhoto);

        retrieveData();
        //for the storage
        imStorageRef = FirebaseStorage.getInstance().getReference("Images/"+animal+".jpg");
        try {
            final File localFile = File.createTempFile("fox","jpg");
            imStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    //העלאה של התמונה
                    animalPhoto.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();
                    int x = 0;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //realtime data base extraction
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = (String) snapshot.child("Dictionary/"+animal).getValue();
                String desc = (String) snapshot.child("animal/"+animal).getValue();
                String didYouKnow = (String) snapshot.child("did_you_know/"+animal).getValue();

                animalDidYouKnow.setText("הידעת? "+didYouKnow);
                animalDescription.setText(desc);
                animalTitle.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String er = error.getMessage();
                int x = 0;
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
                Intent home = new Intent(AnimalInfo.this, MainActivity2.class);
                startActivity(home);
                return true;

            case R.id.menu2:
                Intent apo = new Intent(AnimalInfo.this, appolonia.class);
                startActivity(apo);

                return true;
            case R.id.menu3:
                Intent help = new Intent(AnimalInfo.this, help.class);
                startActivity(help);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveData(){
        sharedPreferences = getSharedPreferences("saveAnimal",MODE_PRIVATE);
        animal = sharedPreferences.getString("animal","קיפוד");

    }



}