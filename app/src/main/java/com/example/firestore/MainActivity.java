package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText enterTitle;
    private EditText enterDescription;
    private Button saveButton, showButton, updateTitle,deleteAll;
    private TextView recTitle, recDescription;

    //Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "DESCRIPTION";


    //connecting to firestore
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
//    private DocumentReference journalRef = db.document("Journal/First Thought");
    private DocumentReference journalRef = db.collection("Journal")
            .document("First Thought");
    private CollectionReference collectionReference = db.collection("Journal");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.enterTitle);
        enterDescription = findViewById(R.id.enter_description);
        saveButton = findViewById(R.id.saveButton);
        showButton = findViewById(R.id.show_data);
        recTitle = findViewById(R.id.rec_title);
        recDescription = findViewById(R.id.rec_description);
        updateTitle = findViewById(R.id.update_data);
        deleteAll = findViewById(R.id.delete_data);

        deleteAll.setOnClickListener(this);

        updateTitle.setOnClickListener(this);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDescription();
//                journalRef.get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                                if(documentSnapshot.exists()) {
//                                    String title = documentSnapshot.getString(KEY_TITLE);
//                                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
//
//                                    recTitle.setText(title);
//                                    recDescription.setText(description);
//
//
//                                }else {
//                                    Toast.makeText(MainActivity.this,"No data Exists", Toast.LENGTH_LONG).show();
//                                }
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.toString());
//
//
//                            }
//                        });
            }
        });



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDescription();

//                String title = enterTitle.getText().toString().trim();
//                String description = enterDescription.getText().toString().trim();
//
////                Journal journal = new Journal();
////                journal.setTitle(title);
////                journal.setDescription(description);
//
//
//                Map<String, Object> data = new HashMap<>();
//                data.put(KEY_TITLE, title);
//                data.put(KEY_DESCRIPTION, description);
//
//
//               journalRef.set(data)
//                       .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_LONG).show();
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.toString());
//
//                            }
//                        });




            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        journalRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null) {
                    Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
//                    Journal journal = documentSnapshot.toObject(Journal.class);

                    recTitle.setText(title);
                    recDescription.setText(description);


                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_data:
                updateMyTitle();
                break;
            case R.id.delete_data:
                deleteAll();
                break;
        }
    }
    private void getDescription() {
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                            Log.d(TAG, "onSuccess: " + snapshot.getId());
                            Journal journal = snapshot.toObject(Journal.class);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void deleteAll() {
        journalRef.update(KEY_TITLE, FieldValue.delete());
        journalRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }



    private void updateMyTitle() {
        String title = enterTitle.getText().toString().trim();
        String description = enterDescription.getText().toString().trim();

        Map<String,Object> data = new HashMap<>();


        data.put(KEY_TITLE, title);
        data.put(KEY_DESCRIPTION, description);

        journalRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Updated!!", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.toString());


                    }
                });
    }

    private void addDescription() {
        String title = enterTitle.getText().toString().trim();
        String description = enterDescription.getText().toString().trim();

        Journal journal = new Journal(title,description);
//        journal.setTitle(title);
//        journal.setDescription(description);

        collectionReference.add(journal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}