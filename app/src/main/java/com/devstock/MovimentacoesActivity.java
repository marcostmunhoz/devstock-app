package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.MovimentacaoAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Movimentacao;

import java.util.ArrayList;
import java.util.Arrays;

public class MovimentacoesActivity extends AppCompatActivity {
    ApiHandler apiHandler;
    EditText etDtInicio, etDtFim;
    Button btnConsultar, btnLimpar, btnCadastrar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao);

        apiHandler = ApiHandler.getInstance(this);

        etDtInicio = findViewById(R.id.etDtInicio);
        etDtFim = findViewById(R.id.etDtFim);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        listView = findViewById(R.id.listView);

        if (!ApiHandler.permiteRealizarMovimentacao()) {
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
                limparDatas();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaMovimentacoes(null);
            }
        });

        realizarBusca();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            limparDatas();
        }
    }

    public void realizarBusca() {
        listView.setAdapter(null);
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Buscando movimentações...");

            apiHandler.getMovimentacoes(etDtInicio.getText().toString(), etDtFim.getText().toString(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        setListaMovs(response.toString());
                    } catch (Exception ex) {
                        Toast.makeText(MovimentacoesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(MovimentacoesActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(MovimentacoesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(MovimentacoesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void limparDatas() {
        etDtInicio.setText("");
        etDtFim.setText("");
        realizarBusca();
    }

    public void setListaMovs(String data) {
        Movimentacao[] movs = Helpers.deserialize(data, Movimentacao[].class);
        MovimentacaoAdapter listMovs = new MovimentacaoAdapter(new ArrayList<>(Arrays.asList(movs)), this);
        listMovs.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                abrirTelaMovimentacoes(id);
            }
        });

        listView.setAdapter(listMovs);
    }

    public void abrirTelaMovimentacoes(Integer id) {
        Intent intent = new Intent(this, FornAlteracaoActivity.class);
        if (id != null) {
            intent.putExtra("id_movimentacao", id);
        }
        startActivityForResult(intent, 1);
    }
}
