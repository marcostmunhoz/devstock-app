package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.FornSelecionarAdapter;
import com.devstock.adapters.FornecedorAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Fornecedor;

import java.util.ArrayList;
import java.util.Arrays;

public class FornSelecionarActivity extends AppCompatActivity {
    ApiHandler apiHandler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forn_selecionar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);
        listView = findViewById(R.id.listView);

        carregarFornecedores();
    }

    public void carregarFornecedores() {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando fornecedores...");

        apiHandler.getFornecedores(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    setListaForns(response.toString());
                } catch (Exception ex) {
                    Toast.makeText(FornSelecionarActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Helpers.tratarRetorno(FornSelecionarActivity.this, error, true);
                } catch (Exception ex) {
                    Toast.makeText(FornSelecionarActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        });
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

    public void setListaForns(String data) {
        Fornecedor[] forns = Helpers.deserialize(data, Fornecedor[].class);
        final FornSelecionarAdapter listForns = new FornSelecionarAdapter(new ArrayList<>(Arrays.asList(forns)), this);
        listForns.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer id = (Integer) v.getTag(R.id.item_id);
                final String razao = (String) v.getTag(R.id.item_razao_social);

                selecionarForn(id, razao);
            }
        });

        listView.setAdapter(listForns);
    }

    public void selecionarForn(Integer id, String razaoSocial) {
        Intent data = new Intent();
        data.putExtra("id_forn", id);
        data.putExtra("razao_social_forn", razaoSocial);
        setResult(RESULT_OK, data);
        finish();
    }
}
