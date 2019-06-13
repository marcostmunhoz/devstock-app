package com.devstock;

import android.app.ProgressDialog;
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
import com.devstock.adapters.ProdSelecionarAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Fornecedor;
import com.devstock.models.Produto;

import java.util.ArrayList;
import java.util.Arrays;

public class ProdSelecionarActivity extends AppCompatActivity {
    ApiHandler apiHandler;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_selecionar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);
        listView = findViewById(R.id.listView);

        carregarProdutos();
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

    public void carregarProdutos() {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando produtos...");

        apiHandler.getProdutos(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    setListaProds(response.toString());
                } catch (Exception ex) {
                    Toast.makeText(ProdSelecionarActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Helpers.tratarRetorno(ProdSelecionarActivity.this, error, true);
                } catch (Exception ex) {
                    Toast.makeText(ProdSelecionarActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        });
    }

    public void setListaProds(String data) {
        Produto[] prods = Helpers.deserialize(data, Produto[].class);
        final ProdSelecionarAdapter listProds = new ProdSelecionarAdapter(new ArrayList<>(Arrays.asList(prods)), this);
        listProds.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Produto prod = (Produto) v.getTag(R.id.item_produto);

                try {
                    selecionarProd(prod);
                } catch (Exception ex) {
                    Toast.makeText(ProdSelecionarActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setAdapter(listProds);
    }

    public void selecionarProd(Produto prod) throws Exception {
        Intent data = new Intent();
        data.putExtra("produto", Helpers.serialize(prod, Produto.class).toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
