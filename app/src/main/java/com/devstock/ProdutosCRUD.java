package com.devstock;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ProdutosCRUD extends AppCompatActivity {
    SharedPreferences pref;
    ApiHandler apiHandler;

    EditText etCod;
    Button btnConsultar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_crud);

        final Context ctx = this;

        pref = getSharedPreferences("devstock_prefs", MODE_PRIVATE);
        apiHandler = ApiHandler.getInstance(this);

        etCod = findViewById(R.id.etCod);
        btnConsultar = findViewById(R.id.btnConsultar);
        listView = findViewById(R.id.listView);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);
                try {
                    apiHandler.getAllProdutos(pref.getString("token", ""), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONArray arr = (JSONArray) response;
                                ArrayList<String> nomes = new ArrayList<>();
                                for (int i = 0; i < arr.length(); i++) {
                                    nomes.add((String) arr.get(i));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, nomes);
                                listView.setAdapter(adapter);
                            } catch (Exception ex) {
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String content = new String(error.networkResponse.data, "UTF-8");
                                Toast.makeText(ctx, content, Toast.LENGTH_LONG).show();
                            } catch (Exception ex) {
                            }
                        }
                    });
                } catch (Exception ex) { }
            }
        });
    }
}
