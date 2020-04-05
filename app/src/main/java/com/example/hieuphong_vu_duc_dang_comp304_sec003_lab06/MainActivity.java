package com.example.hieuphong_vu_duc_dang_comp304_sec003_lab06;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ListView list;
    Button buttonChooseCategory;
    SearchView searchView;
    TextView textViewCartInfo;
    Button buttonCheckout;
    EditText editTextEmail;
    EditText editTextPhoneNo;
    EditText editTextYourName;

    String chosenCategory;
    ArrayList<Item> items;

    static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContext=MainActivity.this;

        textViewCartInfo=(TextView)findViewById(R.id.textViewCartInfo);
        textViewCartInfo.setMovementMethod(new ScrollingMovementMethod());

        buttonChooseCategory=(Button)findViewById(R.id.buttonChooseCategory);
        buttonChooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.options_menu);
                popup.show();
            }
        });

        items=ItemRepo.getItems();
        ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                R.layout.list_row, items);
        list = (ListView) findViewById(R.id.listViewItems);
        list.setAdapter(adapter);

        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Item> chosenCategoryList =items.stream().filter(x->x.name.contains(query)).collect(Collectors.toList());
                ArrayList<Item> chosenCategoryItems= new ArrayList<Item>(chosenCategoryList);

                ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                        R.layout.list_row, chosenCategoryItems);
                list.setAdapter(adapter);
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String query) {
                List<Item> chosenCategoryList =items.stream().filter(x->x.name.contains(query)).collect(Collectors.toList());
                ArrayList<Item> chosenCategoryItems= new ArrayList<Item>(chosenCategoryList);

                ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                        R.layout.list_row, chosenCategoryItems);
                list.setAdapter(adapter);
                return false;
            }
        });

        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPhoneNo=(EditText) findViewById(R.id.editTextPhoneNo);
        editTextYourName=(EditText) findViewById(R.id.editTextYourName);

        buttonCheckout=(Button)findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(Intent.ACTION_SEND);
                    it.putExtra(Intent.EXTRA_EMAIL, new String[]{editTextEmail.getText().toString()});
                    it.putExtra(Intent.EXTRA_SUBJECT,"Your Receipt");
                    String output="";
                    if(Cart.cartItems.size()==0){
                        throw new Exception("Must have at least 1 item!");
                    }
                    else{
                        output+="To Mr./Mrs "+editTextYourName.getText().toString()+":\n";
                        for (Item i:Cart.cartItems){
                            output+=i.getName()+" x "+i.getQuantity()+" = $"+i.getQuantity()*i.getPrice()+"\n";
                        }
                        output+="===============================\n" +
                                "Total Price: $"+Cart.cartItems.stream().mapToDouble(x->x.getPrice()*x.getQuantity()).sum();
                    }

                    if(editTextPhoneNo.getText().toString().equals("")){
                        throw new Exception("Please enter phone number!");
                    }
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(editTextPhoneNo.getText().toString(),null,output,null,null);
                    Toast.makeText(getApplicationContext(),"SMS Receipt Sent!",Toast.LENGTH_SHORT).show();

                    it.putExtra(Intent.EXTRA_TEXT,output);
                    it.setType("message/rfc822");
                    startActivity(Intent.createChooser(it,"Choose Mail App"));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.cat_action) {
            chosenCategory = "Action";
        } else if (item.getItemId() == R.id.cat_horror) {
            chosenCategory = "Horror";
        } else if (item.getItemId() == R.id.cat_romance) {
            chosenCategory = "Romance";
        } else {
            ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                    R.layout.list_row, items);
            list.setAdapter(adapter);
            return true;
        }
        List<Item> chosenCategoryList =items.stream().filter(x->x.category==chosenCategory).collect(Collectors.toList());
        ArrayList<Item> chosenCategoryItems= new ArrayList<Item>(chosenCategoryList);

        ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                R.layout.list_row, chosenCategoryItems);
        list.setAdapter(adapter);
        return true;
    }
}
