package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Fornecedor;

public class FornAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSalvar, btnVoltar;
    EditText etCnpj, etRazao, etNomeFantasia, etEnd, etFone, etEmail, etDtCad, etDtEdit;
    Integer idForn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores_crud);

        apiHandler = ApiHandler.getInstance(this);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        etCnpj = findViewById(R.id.etCNPJ);
        etRazao = findViewById(R.id.etRazao);
        etNomeFantasia = findViewById(R.id.etNomeFantasia);
        etEnd = findViewById(R.id.etEnd);
        etFone = findViewById(R.id.etFone);
        etEmail = findViewById(R.id.etEmail);
        etDtCad = findViewById(R.id.etDtCad);
        etDtEdit = findViewById(R.id.etDtEdit);

        if (!ApiHandler.permiteEditarFornecedor()) {
            btnSalvar.setEnabled(false);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FornAlteracaoActivity.this.idForn != null) {
                    editarForn();
                } else {
                    cadastrarForn();
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

            if (bundle != null && bundle.containsKey("id_fornecedor")) {
                idForn = bundle.getInt("id_fornecedor");

                getFornecedor(idForn);
            }
        }
    }

    public void getFornecedor(int id) {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando informações do fornecedor...");

        apiHandler.getFornecedor(idForn, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    Fornecedor f = Helpers.deserialize(response.toString(), Fornecedor.class);

                    carregarInfosForn(f);
                } catch (Exception ex) {
                    Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String content = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(FornAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        });
    }

    public void carregarInfosForn(Fornecedor f) throws Exception {
        if (f != null) {
            etCnpj.setText(f.cnpjFornecedor);
            etRazao.setText(f.razaoSocial);
            etNomeFantasia.setText(f.nomeFantasia);
            etEnd.setText(f.endFornecedor);
            etFone.setText(f.foneFornecedor);
            etEmail.setText(f.emailFornecedor);
            etDtCad.setText(f.getDtCadastro());
            etDtEdit.setText(f.getDtEdicao());
        } else {
            throw new Exception("Fornecedor não encontrado.");
        }
    }

    public void cadastrarForn() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");
            Fornecedor f = new Fornecedor(
                    null,
                    etRazao.getText().toString(),
                    etNomeFantasia.getText().toString(),
                    etCnpj.getText().toString(),
                    etEnd.getText().toString(),
                    etFone.getText().toString(),
                    etEmail.getText().toString()
            );

            apiHandler.newFornecedor(f, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(FornAlteracaoActivity.this, "Fornecedor cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    try {
                        Fornecedor f = Helpers.deserialize(response.toString(), Fornecedor.class);

                        idForn = f.idFornecedor;

                        carregarInfosForn(f);
                    } catch (Exception ex) {
                        Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(FornAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editarForn() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Salvando alterações...");
            Fornecedor f = new Fornecedor(
                    idForn,
                    etRazao.getText().toString(),
                    etNomeFantasia.getText().toString(),
                    etCnpj.getText().toString(),
                    etEnd.getText().toString(),
                    etFone.getText().toString(),
                    etEmail.getText().toString()
            );

            apiHandler.setFornecedor(f, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(FornAlteracaoActivity.this, "Fornecedor editado com sucesso.", Toast.LENGTH_LONG).show();

                    try {
                        Fornecedor f = Helpers.deserialize(response.toString(), Fornecedor.class);

                        carregarInfosForn(f);
                    } catch (Exception ex) {
                        Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(FornAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(FornAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
