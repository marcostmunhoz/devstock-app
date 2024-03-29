package com.devstock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;

public class MenuActivity extends AppCompatActivity {
    Button btnFornecedores, btnProdutos, btnMovimentacoes, btnUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnFornecedores = findViewById(R.id.btnFornecedores);
        btnProdutos = findViewById(R.id.btnProdutos);
        btnMovimentacoes = findViewById(R.id.btnMovimentacoes);
        btnUsuarios = findViewById(R.id.btnUsuarios);

        btnFornecedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(1);
            }
        });

        btnProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(2);
            }
        });

        btnMovimentacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(3);
            }
        });

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(4);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuItemAlterarSenha){
            startActivity(5);
        } else if (id == R.id.menuItemSair) {
            Helpers.confirmDialog(this, "Sair", "Deseja realmente sair do sistema?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ApiHandler.isLoggedIn()) {
                        ApiHandler.setToken(null);
                        ApiHandler.setUser(null);
                        Helpers.removePrefs(MenuActivity.this, "AUTH_TOKEN");
                        startActivity(6);
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivity(int tela) {
        Intent intent = null;

        switch (tela) {
            case 1:
                intent = new Intent(this, FornecedoresActivity.class);
                break;
            case 2:
                intent = new Intent(this, ProdutosActivity.class);
                break;
            case 3:
                intent = new Intent(this, MovimentacoesActivity.class);
                break;
            case 4:
                intent = new Intent(this, UsuariosActivity.class);
                break;
            case 5:
                intent = new Intent(this, AlterarSenhaActivity.class);
                break;
            case 6:
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                super.startActivity(intent);
                finish();
                return;
        }

        super.startActivity(intent);

    }
}
