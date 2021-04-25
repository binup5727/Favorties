package com.example.final_poc;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class favorite_fragment extends Fragment {
    ListView list;
    ArrayList<String> stock_list = new ArrayList<String>();

    DBHelper dbHelper;




    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.nav_favorite, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            System.out.println("user signed in");
            System.out.println(stock_list.size() + " list size");

            dbHelper = new DBHelper(getContext());



            stock_list = dbHelper.getFavArray(user.getEmail());
            writelist(view);
            // User is signed in
        } else {
            System.out.println();
            System.out.println("user signed out");
            // No user is signed in
        }

        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container
                        , new search_fragment()).commit();
            }

            });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("long click");
                PopupWindow popup = new PopupWindow(getContext());
                popup.showAtLocation(getView(), Gravity.CENTER, 10, 10);
                popup.update(20, 20, 30, 30);
                return false;
            }
        });








        return view;
    }

    public void writelist(View view){

        System.out.println("stock list below");
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                stock_list);
        list = (ListView)view.findViewById(R.id.stock_list);
        list.setAdapter(itemsAdapter);
        System.out.println("stock list above");
    }




}
