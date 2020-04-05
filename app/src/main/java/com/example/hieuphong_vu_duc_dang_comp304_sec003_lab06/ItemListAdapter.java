package com.example.hieuphong_vu_duc_dang_comp304_sec003_lab06;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;


class ItemListAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    int mResourse;

    public ItemListAdapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        mContext=context;
        mResourse=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String name=getItem(position).getName();
        double price=getItem(position).getPrice();
        String category=getItem(position).getCategory();

        Item item=new Item(name,price,category);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        convertView=inflater.inflate(mResourse,parent,false);

        TextView tvName=(TextView)convertView.findViewById(R.id.textViewName);
        TextView tvPrice=(TextView)convertView.findViewById(R.id.textViewPrice);
        TextView tvCategory=(TextView)convertView.findViewById(R.id.textViewCategory);

        tvName.setText(name);
        tvPrice.setText("$"+price);
        tvCategory.setText(category);

        EditText editTextQuantity=(EditText)convertView.findViewById(R.id.editTextQuantity);

        Button buttonAdd=(Button)convertView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try{
                    Item itemToAdd=(Item) getItem(position);
                    Item itemInCart=Cart.cartItems.stream().filter(x->x.name.equals(item.name)).findFirst().orElse(null);
                    int lastQuantity=0;

                    if(itemInCart!=null){
                        lastQuantity=itemInCart.getQuantity();
                    }

                    if(editTextQuantity.getText().toString().equals("")){
                        throw new Exception("Please enter quantity!");
                    }
                    itemToAdd.setQuantity(Integer.parseInt(editTextQuantity.getText().toString()));
                    Cart.addToCart(itemToAdd,lastQuantity);

                    TextView textViewCartInfo = (TextView)((Activity) MainActivity.mainContext).findViewById(R.id.textViewCartInfo);
                    String output="";
                    for(Item cartItem:Cart.cartItems){
                        output+=String.format("%-30s %-8s\n",
                                "    "+cartItem.name+" x"+cartItem.quantity,"$"+cartItem.price*cartItem.quantity);
                    }
                    textViewCartInfo.setText(output);

                    double totalPrice=Cart.cartItems.stream().mapToDouble(x->x.getPrice()*x.getQuantity()).sum();
                    Button buttonCheckout=(Button)((Activity) MainActivity.mainContext).findViewById(R.id.buttonCheckout);
                    buttonCheckout.setText("Checkout ($"+totalPrice+")");
                }
                catch (Exception e){
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonSub=(Button)convertView.findViewById(R.id.buttonSub);
        buttonSub.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try{
                    Item itemToSub=(Item) getItem(position);
                    Item itemInCart=Cart.cartItems.stream().filter(x->x.name.equals(item.name)).findFirst().orElse(null);
                    int lastQuantity=0;

                    if(itemInCart!=null){
                        lastQuantity=itemInCart.getQuantity();
                    }

                    if(editTextQuantity.getText().toString().equals("")){
                        throw new Exception("Please enter quantity!");
                    }
                    itemToSub.setQuantity(Integer.parseInt(editTextQuantity.getText().toString()));
                    Cart.subFromCart(itemToSub,lastQuantity);

                    TextView textViewCartInfo = (TextView)((Activity) MainActivity.mainContext).findViewById(R.id.textViewCartInfo);
                    String output="";
                    for(Item cartItem:Cart.cartItems){
                        output+=String.format("%-30s %-8s\n",
                                "    "+cartItem.name+" x"+cartItem.quantity,"$"+cartItem.price*cartItem.quantity);
                    }
                    textViewCartInfo.setText(output);

                    double totalPrice=Cart.cartItems.stream().mapToDouble(x->x.getPrice()*x.getQuantity()).sum();
                    Button buttonCheckout=(Button)((Activity) MainActivity.mainContext).findViewById(R.id.buttonCheckout);
                    buttonCheckout.setText("Checkout ($"+totalPrice+")");
                }
                catch (Exception e){
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
