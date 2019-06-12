package com.devstock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    Button btnCadastrar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        listView = findViewById(R.id.listView);

        if (!ApiHandler.permiteRealizarMovimentacao()) {
            btnCadastrar.setEnabled(false);
        }

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaMovimentacoes(null);
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
        if (requestCode == 4) {
            realizarBusca();
        }
    }

    public void realizarBusca() {
        listView.setAdapter(null);
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Buscando movimentações...");

            apiHandler.getMovimentacoes(new Response.Listener() {
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
                        Helpers.tratarRetorno(MovimentacoesActivity.this, error, true);
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
        Intent intent = new Intent(this, MovCadastroActivity.class);
        if (id != null) {
            intent.putExtra("id_movimentacao", id);
        }
        startActivityForResult(intent, 4);
    }
}
