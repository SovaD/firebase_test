package com.example.firebase_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    EditText etTitle,etBody;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

     etBody=findViewById(R.id.body);
     etTitle=findViewById(R.id.title);
     button= findViewById(R.id.btnAdd);

     button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String title = etTitle.getText().toString();
             String body = etBody.getText().toString();
             //check values
             if (title.isEmpty()){
                 etTitle.setText("Cannot be empty");
                 return;
             }
             if (body.isEmpty()){
                 etBody.setText("Cannot be empty");
                 return;
                 
             }
             //add to database
             addToDataBase(title,body);
         }
     });

    }

    private void addToDataBase(String title, String body) {
        //create a hashmap
        HashMap<String, Object> postHashMap = new HashMap<>();
        postHashMap.put("title",title);
        postHashMap.put("body",body);

        //instance database connection
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");

        String key = postsRef.push().getKey();
        postHashMap.put("key",key);

        postsRef.child(key).setValue(postHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddActivity.this,"Added",Toast.LENGTH_SHORT).show();
                etTitle.getText().clear();
                etBody.getText().clear();
            }
        });

    }
}