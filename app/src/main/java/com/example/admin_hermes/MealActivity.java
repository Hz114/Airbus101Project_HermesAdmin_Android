package com.example.admin_hermes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MealActivity extends AppCompatActivity {
    private ListView listView_meal;
    private MealListViewAdapter adapter_meal;

    private String seatNumber = null;
    private String mealStr = null;
    private Drawable meal_icon = null;
    private String callCabinCrew = null;
    private Drawable call_icon = null;
    private String call_sub = null;

    private Context mContext;

    private String setSeatNumber = null;
    private FirebaseFirestore firebaseStoreDB = FirebaseFirestore.getInstance();
    private Map<String, Object> docData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        mContext = getApplicationContext();
        //meal구현 본문
        listView_meal = (ListView) findViewById(R.id.listView_meal);
        adapter_meal = new MealListViewAdapter();
        listView_meal.setAdapter(adapter_meal);

        firebaseStoreDB.collection("customer_meal")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        adapter_meal.clear();
                        for (QueryDocumentSnapshot document : value) {
                            if (document.get("seatNumber") != null) {
                                seatNumber = document.get("seatNumber").toString();
                                mealStr = document.get("meal").toString();
                                callCabinCrew = document.get("callCabinCrew").toString();
                                switch (mealStr){
                                    case "beef":
                                        meal_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.meal_beef);
                                        break;
                                    case "seafood":
                                        meal_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.meal_seafood);
                                        break;
                                    case "no":
                                        meal_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.meal_no);
                                        break;
                                }
                                switch (callCabinCrew){
                                    case "true":
                                        call_sub = "T";
                                        call_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.meal_callcabincrew_true);
                                        break;
                                    case "false":
                                        call_sub = "F";
                                        call_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.meal_callcabincrew_false);
                                        break;
                                }
                                adapter_meal.addItem(meal_icon,seatNumber,mealStr.toUpperCase(),call_icon, call_sub);
                                //Array_meal.add(mealStr);
                                //adapter_meal.add(mealStr);
                            }
                        }
                        adapter_meal.notifyDataSetChanged();
                    }
                });

        listView_meal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MealListViewItem item = (MealListViewItem) parent.getItemAtPosition(position) ;

                setSeatNumber = item.getTitle() ;
                String getMeal = item.getDesc() ;
                String getCallCabinCrew = item.getSub();
                String getCall = null;

                if(getCallCabinCrew == "T"){
                    Toast.makeText(getApplicationContext(),"승객을 확인했습니다", Toast.LENGTH_LONG).show();
                    getCall = "false";
                }else{
                    Toast.makeText(getApplicationContext(),"승객이 부릅니다", Toast.LENGTH_LONG).show();
                    getCall = "true";
                }

                docData.put("meal", getMeal.toLowerCase());
                docData.put("seatNumber", setSeatNumber);
                docData.put("callCabinCrew", getCall);

                setDocData();
            }
        }) ;
    }

    public void setDocData() {
        firebaseStoreDB.collection("customer_meal").document(setSeatNumber)
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
