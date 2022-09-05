package com.afeka.ZooMe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afeka.ZooMe.ml.ConvertedModel;//1
import com.afeka.ZooMe.ml.ConvertedModelOpt; //4
import com.afeka.ZooMe.ml.Model;//3
import com.google.firebase.database.core.Context;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity2 extends AppCompatActivity {

    ImageView imageView;
    Button openCamera;
    TextView name;
    ActivityResultLauncher<Intent> activityResultLauncher;

    String animal;
    String username;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        imageView = findViewById(R.id.image_view);
        openCamera = findViewById(R.id.openCamera);

        //promition
        if(ContextCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);

        }

        name = findViewById(R.id.textView);
        retrieveData();
        //Intent i = getIntent();
        //username = i.getStringExtra("userName");
        //name.setText("היי " + username);


        //חיבור למצלמה, שינוי גודל התמונה,
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap captureImage = (Bitmap) result.getData().getExtras().get("data");
                    int dimention = Math.min(captureImage.getWidth(),captureImage.getHeight());
                    captureImage = ThumbnailUtils.extractThumbnail(captureImage,dimention,dimention);
                    imageView.setImageBitmap(captureImage);

                    captureImage = Bitmap.createScaledBitmap(captureImage,64,64,false);
                    classifyImage(captureImage);

                    Intent ii = new Intent(MainActivity2.this,AnimalInfo.class);
                    startActivity(ii);

                }
                //Intent ii = new Intent(MainActivity2.this,AnimalInfo.class);
                //startActivity(ii);
            }
        });
        //מחכה לפתיחת המצלמה ברגע שלוחצים התחל בסריקה הוא פותח מצלמה
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
            }
        });

    }

    public void classifyImage(Bitmap captureImage){
        try {
            ConvertedModelOpt model = ConvertedModelOpt.newInstance(getApplicationContext());
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*64*64*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[64*64];
            captureImage.getPixels(intValues,0,captureImage.getWidth(),0,0,captureImage.getWidth(),captureImage.getHeight());
            int pixel = 0;
            for (int i=0; i<64; i++){
                for (int j=0; j<64; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val>>16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val>>8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ConvertedModelOpt.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float [] confi = outputFeature0.getFloatArray(); //give array contain 5 elements
            int maxPos = 0;
            float maxConfi = 0;
            for(int i=0; i<confi.length; i++){
                if (confi[i]>maxConfi){
                    maxConfi = confi[i];
                    maxPos = i;
                }
            }
            String [] calsses = {"Bee", "Bird", "Dog", "Fox", "Horse"};
            animal = calsses[maxPos];
            saveData(animal);
            // Releases model resources if no longer used.
            model.close();

        } catch ( IOException e) {
            // TODO Handle the exception
        }
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
                Intent home = new Intent(MainActivity2.this, MainActivity2.class);
                startActivity(home);
                return true;

            case R.id.menu2:
                Intent apo = new Intent(MainActivity2.this, appolonia.class);
                startActivity(apo);


                return true;
            case R.id.menu3:
                Intent help = new Intent(MainActivity2.this, help.class);
                startActivity(help);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveData(){
        sharedPreferences = getSharedPreferences("saveName",MODE_PRIVATE);
        username = sharedPreferences.getString("userName","רב");
        name.setText("היי " + username);


    }
    public void saveData(String animal){
        sharedPreferences = getSharedPreferences("saveAnimal",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("animal",animal);
        editor.commit();
    }

}//class main activity2