package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.models.Produto;

import org.json.JSONObject;

public class ProdAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSalvar, btnVoltar;
    EditText etCod, etDesc, etQtd, etDtCad, etDtEdit, etForn;
    Integer idProduto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_alteracao);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        etCod = findViewById(R.id.etCod);
        etDesc = findViewById(R.id.etDesc);
        etQtd = findViewById(R.id.etQtd);
        etDtCad = findViewById(R.id.etDtCad);
        etDtEdit = findViewById(R.id.etDtEdit);
        etForn = findViewById(R.id.etForn);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ProdAlteracaoActivity.this.idProduto != null) {
                    editarProduto();
                } else {
                    cadastrarProduto();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.containsKey("id_produto")) {
                idProduto = bundle.getInt("id_produto");

                getProduto(idProduto);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, ProdutosActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    public void getProduto(int id) {
        apiHandler.getProduto(idProduto, new Response.Listener() {
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

    public void carregarInfosProduto(Produto p) throws Exception {
        if (p != null) {
            etCod.setText(p.codProduto);
            etDesc.setText(p.nmProduto);
            etQtd.setText(String.valueOf(p.nrQtdEstocada));
            etDtCad.setText(p.getDtCadastro().toString());
            etDtEdit.setText(p.getDtEdicao().toString());
        } else {
            throw new Exception("Produto não encontrado.");
        }
    }

    public void cadastrarProduto() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");

            apiHandler.newProduto(Helpers.createJsonObject(
                    "cod_produto", etCod.getText().toString(),
                    "nm_produto", etDesc.getText().toString()
            ), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(ProdAlteracaoActivity.this, "Produto cadastrado com sucesso.", Toast.LENGTH_SHORT).show();

                    try {
                        Produto p = Helpers.deserialize(response.toString(), Produto.class);

                        idProduto = p.idProduto;

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
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editarProduto() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Salvando alterações...");

            JSONObject obj = Helpers.createJsonObject(
                    "cod_produto", etCod.getText().toString(),
                    "nm_produto", etDesc.getText().toString()
            );

            apiHandler.setProduto(idProduto, obj, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(ProdAlteracaoActivity.this, "Produto editado com sucesso.", Toast.LENGTH_SHORT).show();

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
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
