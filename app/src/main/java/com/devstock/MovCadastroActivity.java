package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.ProdutoMovimentacaoAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Movimentacao;
import com.devstock.models.Produto;
import com.devstock.models.ProdutoMovimentacao;

import java.util.ArrayList;
import java.util.Arrays;

public class MovCadastroActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSelecionar, btnAddProd, btnSalvar, btnVoltar;
    RadioButton rbEntrada, rbSaida;
    EditText etDtHrMovimentacao, etDesc, etProduto, etQtd, etVlrUnitario;
    ListView listView;
    ArrayList<ProdutoMovimentacao> produtos = new ArrayList<>();
    Produto produtoSelecionado = null;
    Integer idMovimentacao = null;
    ProdutoMovimentacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentacao_cad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);

        btnSelecionar = findViewById(R.id.btnSelecionar);
        btnAddProd = findViewById(R.id.btnAddProd);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnSalvar = findViewById(R.id.btnSalvar);
        rbEntrada = findViewById(R.id.rbEntrada);
        rbSaida = findViewById(R.id.rbSaida);
        etDtHrMovimentacao = findViewById(R.id.etDtHrMovimentacao);
        etDesc = findViewById(R.id.etDesc);
        etProduto = findViewById(R.id.etProduto);
        etQtd = findViewById(R.id.etQtd);
        etVlrUnitario = findViewById(R.id.etVlrUnitario);
        listView = findViewById(R.id.listView);

        if (!ApiHandler.permiteRealizarMovimentacao()) {
            btnSalvar.setEnabled(false);
        }

        btnSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSelectProduto();
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProdutoLista();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarMov();
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

            if (bundle != null && bundle.containsKey("id_movimentacao")) {
                idMovimentacao = bundle.getInt("id_movimentacao");

                getMovimentacao();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("produto")) {
                    produtoSelecionado = Helpers.deserialize(data.getStringExtra("produto"), Produto.class);
                    etProduto.setText(produtoSelecionado.nmProduto);
                    return;
                }
            }

            produtoSelecionado = null;
            etProduto.setText("");
        }
    }

    public void getMovimentacao() {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando informações da movimentação...");

        apiHandler.getMovimentacao(idMovimentacao, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    Movimentacao m = Helpers.deserialize(response.toString(), Movimentacao.class);

                    carregarInfosMov(m);
                } catch (Exception ex) {
                    Toast.makeText(MovCadastroActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Helpers.tratarRetorno(MovCadastroActivity.this, error, true);
                } catch (Exception ex) {
                    Toast.makeText(MovCadastroActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        });
    }

    public void carregarInfosMov(Movimentacao m) throws Exception {
        if (m != null) {
            if (m.tpMovimentacao == 1) {
                rbEntrada.setChecked(true);
            } else {
                rbSaida.setChecked(true);
            }

            etDtHrMovimentacao.setText(m.getDtCadastro());
            etDesc.setText(m.dsMovimentacao);
            produtos = new ArrayList<>(Arrays.asList(m.produtosMovimentacao));
            resetDataAdapter();
        } else {
            throw new Exception("Movimentação não encontrada.");
        }
    }

    public void cadastrarMov() {
        try {
            Movimentacao m = getInstanceMovimentacao();
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");

            apiHandler.newMovimentacao(m, new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                    try {
                        dialog.cancel();
                        Helpers.tratarRetorno(MovCadastroActivity.this, response, false);

                        Movimentacao m = Helpers.deserialize(response.toString(), Movimentacao.class);

                        idMovimentacao = m.idMovimentacao;

                        carregarInfosMov(m);
                    } catch (Exception ex) {
                        Toast.makeText(MovCadastroActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Helpers.tratarRetorno(MovCadastroActivity.this, error, true);
                    } catch (Exception ex) {
                        Toast.makeText(MovCadastroActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(MovCadastroActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Movimentacao getInstanceMovimentacao() throws Exception {
        boolean entrada = rbEntrada.isChecked(),
                saida = rbSaida.isChecked();
        String desc = etDesc.getText().toString();

        ArrayList<String> errors = new ArrayList<>();

        if (!entrada && !saida) {
            errors.add("O tipo é obrigatório.");
        }

        if (desc.length() == 0) {
            errors.add("A descrição é obrigatória.");
        }

        if (produtos.size() == 0) {
            errors.add("É necessário adicionar ao menos um produto.");
        }

        if (errors.size() > 0) {
            throw new Exception(TextUtils.join("\n", errors));
        } else {
            ProdutoMovimentacao[] produtosMovimentacao = new ProdutoMovimentacao[produtos.size()];
            produtosMovimentacao = produtos.toArray(produtosMovimentacao);
            return new Movimentacao((entrada ? 1 : 2), desc, produtosMovimentacao);
        }
    }

    public void abrirSelectProduto() {
        Intent intent = new Intent(this, ProdSelecionarActivity.class);
        startActivityForResult(intent, 6);
    }

    public void addProdutoLista() {
        if (produtoSelecionado != null) {
            try {
                String qtdString = etQtd.getText().toString(),
                        vlrUnitarioString = etVlrUnitario.getText().toString();
                Integer qtd = (qtdString.length() == 0 ? 0 : Integer.parseInt(qtdString));
                Float vlrUnitario = (vlrUnitarioString.length() == 0 ? 0 : Float.parseFloat(vlrUnitarioString));

                ArrayList<String> errors = new ArrayList<>();

                if (qtd <= 0) {
                    errors.add("A quantidade precisa ser um número inteiro positivo.");
                }

                if (vlrUnitario <= 0) {
                    errors.add("O valor unitário precisa ser positivo.");
                }

                if (errors.size() > 0) {
                    throw new Exception(TextUtils.join("\n", errors));
                } else {
                    ProdutoMovimentacao novo = new ProdutoMovimentacao(qtd, vlrUnitario, produtoSelecionado);
                    produtos.add(novo);
                    resetDataAdapter();
                    produtoSelecionado = null;
                    etProduto.setText("");
                    etQtd.setText("");
                    etVlrUnitario.setText("");
                }
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "É necessário selecionar um produto para adicionar.", Toast.LENGTH_LONG).show();
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

    public void resetDataAdapter() {
        adapter = new ProdutoMovimentacaoAdapter(produtos, this);
        adapter.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                produtos.remove(position);
                resetDataAdapter();
            }
        });
        listView.setAdapter(adapter);
    }
}
