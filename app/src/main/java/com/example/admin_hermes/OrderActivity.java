package com.example.admin_hermes;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class OrderActivity extends AppCompatActivity {
    private ListView listView_order;
    private OrderListViewAdapter adapter_order;

    private Drawable order_icon = null;
    private String seatNumber = null;
    private String orderStr = null;
    private String category = null;
    private String id = null;

    private String getId = null;

    private Context mContext;

    private FirebaseFirestore firebaseStoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //order구현 본문
        listView_order = (ListView) findViewById(R.id.listView_order);
        adapter_order = new OrderListViewAdapter();
        listView_order.setAdapter(adapter_order);

        mContext = getApplicationContext();

        firebaseStoreDB.collection("customer_order")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        adapter_order.clear();
                        for (QueryDocumentSnapshot document : value) {
                            if (document.get("seatNumber") != null) {
                                seatNumber = document.get("seatNumber").toString();
                                orderStr = document.get("order").toString();
                                category = document.get("category").toString();
                                id = document.getId();
                                switch (category){
                                    case "beverages":
                                        order_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.order_beverages);
                                        break;
                                    case"foods":
                                        order_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.order_foods);
                                        break;
                                    case "amenities":
                                        order_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.order_amenities);
                                        break;
                                    case "pills":
                                        order_icon = (Drawable) ContextCompat.getDrawable(mContext, R.drawable.order_pills);
                                        break;
                                }
                                adapter_order.addItem(order_icon,seatNumber, orderStr.toUpperCase(), category.toUpperCase(), id);
                            }
                        }
                        adapter_order.notifyDataSetChanged();
                    }
                });

        //listView_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        listView_order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
            //public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                OrderListViewItem item = (OrderListViewItem) parent.getItemAtPosition(position);

                String getSeatNumber = item.getTitle() ;
                String getOrder = item.getDesc() ;
                String getCategory= item.getSubtitle();

                getId = item.getSub();

                //Toast.makeText(getApplicationContext(),getId, Toast.LENGTH_LONG).show();

                firebaseStoreDB.collection("customer_order").document(getId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
                Toast.makeText(getApplicationContext(), "주문이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                return false;
                /**
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("Delete");
                alert.setMessage("주문을 삭제하시겠습니까?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseStoreDB.collection("customer_order").document(getId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error deleting document", e);
                                    }
                                });
                        Toast.makeText(getApplicationContext(), "주문이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alert.show();
                **/
            }
        });
    }
}
