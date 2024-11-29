package com.example.firebase_test;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ContentActivity extends AppCompatActivity {

    EditText tTitle, tBody;
    FloatingActionButton bEdit, bDelete;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tTitle = findViewById(R.id.title);
        tBody = findViewById(R.id.body);
        bEdit = findViewById(R.id.btnEdit);
        bDelete = findViewById(R.id.btnDelete);

        Post post = (Post) getIntent().getSerializableExtra("post");

        tTitle.setText(post.getTitle());
        tBody.setText(post.getBody());

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post);
            }
        });
        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = tTitle.getText().toString();
                String body = tBody.getText().toString();
                //check values
                if (title.isEmpty()) {
                    tTitle.setText("Cannot be empty");
                    return;
                }
                if (body.isEmpty()) {
                    tBody.setText("Cannot be empty");
                    return;

                }
                post.setTitle(title);
                post.setBody(body);
                //add to database
                editData(post);
            }
        });

    }

    private void editData(Post post) {
        dbref = FirebaseDatabase.getInstance().getReference("posts").child(post.getKey());
        dbref.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ContentActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void deletePost(Post post) {
        // creating a variable for our Database
        // Reference for Firebase.
        dbref = FirebaseDatabase.getInstance().getReference().child("posts");
        // we are use add listerner
        // for event listener method
        // which is called with query.
        Query query = dbref.child(post.getKey());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // remove the value at reference
                dataSnapshot.getRef().removeValue();
                Toast.makeText(ContentActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContentActivity.this, "Error" + databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}