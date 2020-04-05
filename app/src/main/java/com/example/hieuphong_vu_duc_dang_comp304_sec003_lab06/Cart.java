package com.example.hieuphong_vu_duc_dang_comp304_sec003_lab06;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class Cart{
    static List<Item> cartItems=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addToCart(Item item,int lastQuantity){
        Item itemInCart=cartItems.stream().filter(x->x.name.equals(item.name)).findFirst().orElse(null);
        if(itemInCart!=null){
            itemInCart.quantity=item.quantity+lastQuantity;
        }
        else{
            cartItems.add(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void subFromCart(Item item,int lastQuantity){
        Item itemInCart=cartItems.stream().filter(x->x.name.equals(item.name)).findFirst().orElse(null);
        if(itemInCart!=null){
            itemInCart.quantity=lastQuantity-item.quantity;
            if(itemInCart.quantity<=0){
                cartItems.remove(itemInCart);
            }
        }
        else{
            Toast.makeText(MainActivity.mainContext,"This item is not in cart!",Toast.LENGTH_SHORT).show();
        }
    }
}
