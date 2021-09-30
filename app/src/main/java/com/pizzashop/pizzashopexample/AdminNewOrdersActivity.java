package com.pizzashop.pizzashopexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pizzashop.pizzashopexample.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private String user="",status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");


        ordersList=findViewById(R.id.admin_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolders> adapter= new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolders>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolders holder, @SuppressLint("RecyclerView") final int position, @NonNull final AdminOrders model) {
                holder.userName.setText("Name: "+ model.getName());
                holder.userPhoneNumber.setText("Phone: "+ model.getPhone());
                holder.userTotalPrice.setText("Total Amount= ₹"+ model.getTotalAmount());
                holder.userDateTime.setText("Ordered at "+ model.getDate()+ " "+ model.getTime());
                holder.userTable.setText("Table No: "+ model.getTable());
                holder.orderStatus.setText("Status : "+model.getState());
                holder.confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String uID= getRef(position).getKey();
                        ordersRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                status=dataSnapshot.child("state").getValue().toString();
                                //Toast.makeText(AdminNewOrdersActivity.this, status, Toast.LENGTH_SHORT).show();
                                if(status.equals("Order Confirmed")) {
                                    Toast.makeText(AdminNewOrdersActivity.this, "Order already Confirmed", Toast.LENGTH_SHORT).show();
                                }else{
                                    HashMap<String, Object> confirmOrderMap = new HashMap<>();
                                    confirmOrderMap.put("state","Order Confirmed");

                                    ordersRef.child(uID).updateChildren(confirmOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AdminNewOrdersActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID= getRef(position).getKey();
                        Intent intent = new Intent (AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("Oid",uID );
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]= new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder= new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you Delivered this order products ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    String uID= getRef(position).getKey();
                                    RemoveOrder(uID);
                                    String OID=uID.substring(10,15);
                                    DatabaseReference AdminOrderRef=FirebaseDatabase.getInstance().getReference().child("Admin Orders").child(OID);
                                    AdminOrderRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(AdminNewOrdersActivity.this,AdminNewOrdersActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }else{
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolders(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolders extends RecyclerView.ViewHolder{

        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userTable,orderStatus,DeliveryStatus;
        public Button ShowOrdersBtn,confirmOrderBtn;

        public AdminOrdersViewHolders(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_Phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userTable=itemView.findViewById(R.id.order_table_no);
            ShowOrdersBtn=itemView.findViewById(R.id.show_All_products_btn);
            orderStatus=itemView.findViewById(R.id.order_status);
            confirmOrderBtn = itemView.findViewById(R.id.confirm_order_btn);
            confirmOrderBtn.setVisibility(View.VISIBLE);

        }
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }
}