package com.pizzashop.pizzashopexample.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pizzashop.pizzashopexample.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductDiscount,txtDescription;
    public ImageView cartProductImg;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        cartProductImg=itemView.findViewById(R.id.cart_product_Image);
        txtProductDiscount=itemView.findViewById(R.id.cart_product_discount_price);
    }


    @Override
    public void onClick(View view) {

    }}
