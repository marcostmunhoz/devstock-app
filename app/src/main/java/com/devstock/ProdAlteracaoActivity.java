package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.FornecedorAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.models.Fornecedor;
import com.devstock.models.Produto;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSalvar, btnVoltar;
    EditText etCod, etDesc, etQtd, etDtCad, etDtEdit;
    Spinner spinForn;
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
        spinForn = findViewById(R.id.spinForn);

        if (!ApiHandler.permiteEditarProduto()) {
            btnSalvar.setEnabled(false);
        }

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

        spinForn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        getFornecedores();
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

    public void getFornecedores() {
        spinForn.setAdapter(null);

        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando lista de fornecedores...");

        apiHandler.getFornecedores(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    carregaFornecedores(response.toString());
                } catch (Exception ex) {
                    Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
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
    }

    public void carregaFornecedores(String data) {
        Fornecedor[] fornecedores = Helpers.deserialize(data, Fornecedor[].class);
        FornecedorAdapter adapter = new FornecedorAdapter(new ArrayList<>(Arrays.asList(fornecedores)), this);
        adapter.setSimple(true);
        spinForn.setAdapter(adapter);
    }

    public void getProduto(int id) {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando informações do produto...");

        apiHandler.getProduto(idProduto, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
            try {
                Produto p = Helpers.deserialize(response.toString(), Produto.class);

                carregarInfosProduto(p);
            } catch (Exception ex) {
                Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                dialog.cancel();
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
    }

    public void carregarInfosProduto(Produto p) throws Exception {
        if (p != null) {
            etCod.setText(p.codProduto);
            etDesc.setText(p.nmProduto);
            etQtd.setText(String.valueOf(p.nrQtdEstocada));
            etDtCad.setText(p.getDtCadastro());
            etDtEdit.setText(p.getDtEdicao());
        } else {
            throw new Exception("Produto não encontrado.");
        }
    }

    public void cadastrarProduto() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");
            Produto p = new Produto(
                    null,
                    etCod.getText().toString(),
                    etDesc.getText().toString(),
                    Integer.parseInt(etQtd.getText().toString()),
                    null,
                    null
            );

            apiHandler.newProduto(p, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(ProdAlteracaoActivity.this, "Produto cadastrado com sucesso.", Toast.LENGTH_LONG).show();

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
            Produto p = new Produto(
                    idProduto,
                    etCod.getText().toString(),
                    etDesc.getText().toString(),
                    Integer.parseInt(etQtd.getText().toString()),
                    null,
                    null
            );

            apiHandler.setProduto(p, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(ProdAlteracaoActivity.this, "Produto editado com sucesso.", Toast.LENGTH_LONG).show();

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
