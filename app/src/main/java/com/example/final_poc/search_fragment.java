package com.example.final_poc;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.zip.Inflater;

public class search_fragment extends Fragment {

    EditText input;
    Button search_btn, addfav, removefav;
    private RequestQueue queue;
    ArrayList<String> stock_data = new ArrayList<String>();
    private ListView list;

    DBHelper dbHelper;
    private String stockname;

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.nav_search, container, false);




        queue = Volley.newRequestQueue(getContext());
        search_btn = view.findViewById(R.id.searchbtn);
        addfav = view.findViewById(R.id.addfav);
        removefav = view.findViewById(R.id.removefav);
        input = view.findViewById(R.id.stock);


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock = String.valueOf(input.getText());
                System.out.println(stock);
                getstock(stock, view);






            }
        });

        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("starting favorite function");
                FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
                if(account != null) {
                    if(stockname != null) {
                        dbHelper = new DBHelper(getContext());
                        String add = stockname;
                        System.out.println(stock_data.get(0) + "name of stock to favorite");
                        dbHelper.addfav(account.getEmail(), add);
                    }else{
                        Toast.makeText(getContext(), "Please Make a Search First", Toast.LENGTH_LONG).show();

                    }



                }else {
                    Toast.makeText(getContext(), "Please Sign In to Save to Favorite", Toast.LENGTH_LONG).show();
                }
            }
        });

        removefav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
                if(account != null) {
                    if(stockname != null) {
                        dbHelper = new DBHelper(getContext());
                        String remove = stockname;
                        System.out.println(stock_data.get(0) + "name of stock to favorite");
                        dbHelper.addfav(account.getEmail(), remove);
                    }else{
                        Toast.makeText(getContext(), "Please Make a Search First", Toast.LENGTH_LONG).show();

                    }



                }else {
                    Toast.makeText(getContext(), "Please Sign In to Save to Favorite", Toast.LENGTH_LONG).show();
                }
            }

        });




        return view;
    }


    public void writelist(View view){

        System.out.println("stock list below");
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                stock_data);
        list = (ListView)view.findViewById(R.id.results);
        list.setAdapter(itemsAdapter);
        System.out.println("stock list above");
    }


    public void getstock(String stock, View view){

        String url = getString(R.string.URL) + "q=" + stock;
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray stock = null;
                            stock = response.getJSONArray("quotes");
                            JSONObject usstock = (JSONObject) stock.get(0);
                            String name = usstock.getString("longname");
                            String index = usstock.getString("index");
                            String score = usstock.getString("score");



                            JSONArray news_prot = response.getJSONArray("news");
                            JSONObject news = (JSONObject)news_prot.get(0);
                            String title = news.getString("title");
                            String link = news.getString("link");
                            stock_data.clear();
                            stockname = name;
                            stock_data.add("Name: " + name);
                            stock_data.add("index:" + index);
                            stock_data.add("Score: " + score);
                            stock_data.add("News");
                            stock_data.add("Title: " + title + "\n" + "Link: " + link);


                            writelist(view);

                            System.out.println(name + "|"
                            + index + "|" + score + "|" + title + "|" + link + "|");





                        } catch (JSONException e) {
                            System.out.println("ERROR WITH call");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR WITH VOLLEY");
            }
        });
        queue.add(jsonObjectRequest);



    }







}
