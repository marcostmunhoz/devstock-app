package com.devstock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.models.Produto;

public class ProdAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnCadastrar, btnAlterar, btnVoltar;
    EditText etCod, etDesc, etQtd, etDtCad, etDtEdit, etForn;
    CheckBox cbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_alteracao);

        apiHandler = ApiHandler.getInstance(this);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnAlterar = findViewById(R.id.btnAlterar);
        btnVoltar = findViewById(R.id.btnVoltar);
        etCod = findViewById(R.id.etCod);
        etDesc = findViewById(R.id.etDesc);
        etQtd = findViewById(R.id.etQtd);
        etDtCad = findViewById(R.id.etDtCad);
        etDtEdit = findViewById(R.id.etDtEdit);
        etForn = findViewById(R.id.etForn);
        cbStatus = findViewById(R.id.cbStatus);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarProduto();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.containsKey("id_produto")) {
                int id = bundle.getInt("id_produto");

                apiHandler.getProduto(id, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            Produto p = Helpers.deserialize(response.toString(), Produto.class);

                            carregarInfosProduto(p);
                        } catch (Exception ex) {
                            Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String content = new String(error.networkResponse.data, "UTF-8");
                            Toast.makeText(ProdAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    public void carregarInfosProduto(Produto p) throws Exception {
        if (p != null) {
            etCod.setText(p.codProduto);
            etDesc.setText(p.nmProduto);
            etQtd.setText(String.valueOf(p.nrQtdEstocada));
            etDtCad.setText(p.getDtCadastro().toString());
            etDtEdit.setText(p.getDtEdicao().toString());
            cbStatus.setChecked(p.getFlgStatus());
        } else {
            throw new Exception("Produto não encontrado.");
        }
    }

    public void cadastrarProduto() {
        try {
            apiHandler.newProduto(Helpers.createJsonObject(
                    "cod_produto", etCod.getText().toString(),
                    "nm_produto", etDesc.getText().toString()
            ), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(ProdAlteracaoActivity.this, "Produto cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(ProdAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
