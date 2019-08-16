package com.example.admin_hermes;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseStoreDB = FirebaseFirestore.getInstance();

    //stop구현
    private Map<String, Object> docData = new HashMap<>();
    private String stop = "false";

    private String count = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseStoreDB.collection("admin")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value,
                                        @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            count = document.get("count").toString();
                            docData.put("count",count);
                        }
                    }
                });



    }

    public void meal_bnt(View v) {
        Intent intent_meal = new Intent(this, MealActivity.class);
        startActivity(intent_meal);
    }


    public void order_bnt(View v) {
        Intent intent_order = new Intent(this, OrderActivity.class);
        startActivity(intent_order);
    }

    public void stop_bnt(View v){
        if(stop == "false"){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("STOP");
            alert.setMessage("승객의 IFE를 멈추시겠습니까?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    docData.put("stop","true");
                    stop = "true";
                    Toast.makeText(getApplicationContext(), "승객의 IFE를 정지시킵니다", Toast.LENGTH_LONG).show();
                    setDocData();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alert.show();
            /**
             **alert를 삭제할 경우
             docData.put("stop",true);
             stop = true;
             Toast.makeText(getApplicationContext(), "승객의 IFE를 정지시킵니다", Toast.LENGTH_LONG).show();
             **/
        }else{
            docData.put("stop","false");
            stop = "false";
            Toast.makeText(getApplicationContext(), "승객의 IFE를 재생시킵니다", Toast.LENGTH_LONG).show();
            setDocData();
        }

    }

    public void setDocData() {
        firebaseStoreDB.collection("admin").document("admin")
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }

                });
    }
}
