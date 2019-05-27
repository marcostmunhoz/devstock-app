package com.devstock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.ProdutosAdapter;
import com.devstock.models.Produto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdutosCRUD extends AppCompatActivity {
    ApiHandler apiHandler;

    EditText etCod;
    Button btnConsultar, btnLimpar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_crud);

        apiHandler = ApiHandler.getInstance(this);

        etCod = findViewById(R.id.etCod);
        btnConsultar = findViewById(R.id.btnConsultar);
        listView = findViewById(R.id.listView);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBusca();
            }
        });
    }

    public void realizarBusca() {
        listView.setAdapter(null);
        try {
            apiHandler.getProdutosLike(etCod.getText().toString(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        setListaProdutos(response.toString());
                    } catch (Exception ex) {
                        Toast.makeText(ProdutosCRUD.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(ProdutosCRUD.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(ProdutosCRUD.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(ProdutosCRUD.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void limparCampo(int id) {

    }

    public void setListaProdutos(String data) {
        Produto[] produtos = Helpers.deserialize(data, Produto[].class);
        ProdutosAdapter listProdutos = new ProdutosAdapter(new ArrayList<>(Arrays.asList(produtos)), this);
        listProdutos.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                abrirEdicaoProduto(id);
            }
        });
        listProdutos.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                deleteProduto(id);
            }
        });

        listView.setAdapter(listProdutos);
    }

    public void deleteProduto(int id) {
        apiHandler.deleteProduto(id, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(ProdutosCRUD.this, "Produto excluído com sucesso", Toast.LENGTH_SHORT).show();
                btnConsultar.performClick();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String content = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(ProdutosCRUD.this, content, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(ProdutosCRUD.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirEdicaoProduto(int id) {
        Intent intent = new Intent(this, ProdAlteracaoActivity.class);
        intent.putExtra("id_produto", id);
        startActivity(intent);
    }
}
