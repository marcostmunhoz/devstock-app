package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.FornecedorAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Fornecedor;

import java.util.ArrayList;
import java.util.Arrays;

public class FornecedoresActivity extends AppCompatActivity {
    ApiHandler apiHandler;
    EditText etCnpj;
    Button btnConsultar, btnLimpar, btnCadastrar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);

        etCnpj = findViewById(R.id.etCNPJ);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        listView = findViewById(R.id.listView);

        if (!ApiHandler.permiteEditarFornecedor()) {
            btnCadastrar.setEnabled(false);
        }

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBusca();
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampo();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaFornecedor(null);
            }
        });

        realizarBusca();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            realizarBusca();
        }
    }

    public void realizarBusca() {
        listView.setAdapter(null);
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Buscando fornecedores...");

            apiHandler.getFornecedoresLike(etCnpj.getText().toString(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        setListaForns(response.toString());
                    } catch (Exception ex) {
                        Toast.makeText(FornecedoresActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Helpers.tratarRetorno(FornecedoresActivity.this, error, true);
                    } catch (Exception ex) {
                        Toast.makeText(FornecedoresActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(FornecedoresActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void limparCampo() {
        etCnpj.setText("");
        realizarBusca();
    }

    public void setListaForns(String data) {
        Fornecedor[] forns = Helpers.deserialize(data, Fornecedor[].class);
        FornecedorAdapter listForns = new FornecedorAdapter(new ArrayList<>(Arrays.asList(forns)), this);
        listForns.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                abrirTelaFornecedor(id);
            }
        });
        listForns.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = (Integer) v.getTag(R.id.item_id);

                Helpers.confirmDialog(FornecedoresActivity.this, "Excluir", "Deseja realmente excluir o fornecedor?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFornecedor(id);
                    }
                });
            }
        });

        listView.setAdapter(listForns);
    }

    public void deleteFornecedor(int id) {
        apiHandler.deleteFornecedor(id, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    Helpers.tratarRetorno(FornecedoresActivity.this, response, false);
                    realizarBusca();
                } catch (Exception ex) {
                    Toast.makeText(FornecedoresActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Helpers.tratarRetorno(FornecedoresActivity.this, error, true);
                } catch (Exception ex) {
                    Toast.makeText(FornecedoresActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaFornecedor(Integer id) {
        Intent intent = new Intent(this, FornAlteracaoActivity.class);
        if (id != null) {
            intent.putExtra("id_fornecedor", id);
        }
        startActivityForResult(intent, 1);
    }
}
