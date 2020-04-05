package com.example.hieuphong_vu_duc_dang_comp304_sec003_lab06;

import java.util.ArrayList;

public class ItemRepo {

    public static ArrayList<Item> getItems(){
        Item item1=new Item("Hello World!",12,"Romance");
        Item item2=new Item("BOO!",13.5,"Horror");
        Item item3=new Item("The Adventure",15.5,"Action");
        Item item4=new Item("Ghost Story",13,"Horror");
        Item item5=new Item("Love is in the Air",20,"Romance");
        Item item6=new Item("Vampire vs Werewolf",22.5,"Horror");
        Item item7=new Item("Tomb Explorer",12,"Action");
        Item item8=new Item("You are my Angel",30,"Romance");
        Item item9=new Item("Tales of Roman",24,"Action");

        ArrayList<Item> items=new ArrayList<Item>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);
        items.add(item9);
        return items;
    }
}
