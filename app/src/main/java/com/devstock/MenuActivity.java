package com.devstock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button btnFornecedores, btnProdutos, btnMovimentacoes, btnUsuarios, btnConfiguracoes, btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnFornecedores = findViewById(R.id.btnFornecedores);
        btnProdutos = findViewById(R.id.btnProdutos);
        btnMovimentacoes = findViewById(R.id.btnMovimentacoes);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnConfiguracoes = findViewById(R.id.btnConfiguracoes);
        btnSair = findViewById(R.id.btnSair);

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

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(5);
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiHandler.isLoggedIn()) {
                    ApiHandler.setToken(null);
                    Helpers.removePrefs(MenuActivity.this, "AUTH_TOKEN");
                    startActivity(6);
                }
            }
        });
    }

    public void startActivity(int tela) {
        Intent intent = null;

        switch (tela) {
            case 1:
                intent = new Intent(this, FornecedoresCRUD.class);
                break;
            case 2:
                intent = new Intent(this, ProdutosActivity.class);
                break;
            case 3:
                intent = new Intent(this, Movimentacao.class);
                break;
            case 4:
                //intent = new Intent(this, Usuarios.class);
                break;
            case 5:
                //intent = new Intent(this, Configuracoes.class);
                break;
            case 6:
                intent = new Intent(this, LoginActivity.class);
                super.startActivity(intent);
                finish();
                return;
        }

        super.startActivity(intent);
    }
}
