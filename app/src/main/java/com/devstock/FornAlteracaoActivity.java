package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Fornecedor;

import java.util.ArrayList;
import java.util.HashSet;

public class FornAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSalvar, btnVoltar;
    EditText etCnpj, etRazao, etNomeFantasia, etEnd, etFone, etEmail, etDtCad, etDtEdit;
    Integer idForn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores_crud);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

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

                getFornecedor();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

    public void getFornecedor() {
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
                    Helpers.tratarRetorno(FornAlteracaoActivity.this, error, true);
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
            Fornecedor f = getInstanceFornecedor();
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");

            apiHandler.newFornecedor(f, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        dialog.cancel();
                        Helpers.tratarRetorno(FornAlteracaoActivity.this, response, false);
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
                        Helpers.tratarRetorno(FornAlteracaoActivity.this, error, true);
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
            Fornecedor f = getInstanceFornecedor();
            final ProgressDialog dialog = Helpers.showLoading(this, "Salvando alterações...");

            apiHandler.setFornecedor(f, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        dialog.cancel();
                        Helpers.tratarRetorno(FornAlteracaoActivity.this, response, false);
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
                        Helpers.tratarRetorno(FornAlteracaoActivity.this, error, true);
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

    public Fornecedor getInstanceFornecedor() throws Exception {
        String razao = etRazao.getText().toString(),
                nomeFantasia = etNomeFantasia.getText().toString(),
                cnpj = etCnpj.getText().toString(),
                endereco = etEnd.getText().toString(),
                fone = etFone.getText().toString(),
                email = etEmail.getText().toString();

        ArrayList<String> errors = new ArrayList<>();

        if (razao.length() == 0) {
            errors.add("A razão social é obrigatória.");
        }

        if (nomeFantasia.length() == 0) {
            errors.add("O nome fantasia é obrigatório.");
        }

        if (cnpj.length() < 14) {
            errors.add("O CNPJ deve possuir 14 caracteres.");
        }

        if (endereco.length() == 0) {
            errors.add("O endereço é obrigatório.");
        }

        if (fone.length() != 10 && fone.length() != 11) {
            errors.add("O telefone deve possuir 10 ou 11 dígitos (DDD + número).");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("Um e-mail válido é obrigatório.");
        }

        if (errors.size() > 0) {
            throw new Exception(TextUtils.join("\n", errors));
        } else {
            return new Fornecedor(idForn, razao, nomeFantasia, cnpj, endereco, fone, email);
        }
    }
}
