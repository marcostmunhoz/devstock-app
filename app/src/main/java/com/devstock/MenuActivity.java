package com.devstock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.devstock.handlers.ApiHandler;

public class MenuActivity extends AppCompatActivity {
    Button btnFornecedores, btnProdutos, btnMovimentacoes, btnUsuarios, btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnFornecedores = findViewById(R.id.btnFornecedores);
        btnProdutos = findViewById(R.id.btnProdutos);
        btnMovimentacoes = findViewById(R.id.btnMovimentacoes);
        btnUsuarios = findViewById(R.id.btnUsuarios);
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

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ApiHandler.isLoggedIn()) {
                    ApiHandler.setToken(null);
                    ApiHandler.setUser(null);
                    Helpers.removePrefs(MenuActivity.this, "AUTH_TOKEN");
                    startActivity(5);
                }
            }
        });
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
                intent = new Intent(this, Movimentacao.class);
                break;
            case 4:
                intent = new Intent(this, UsuariosActivity.class);
                break;
            case 5:
                intent = new Intent(this, LoginActivity.class);
                super.startActivity(intent);
                finish();
                return;
        }

        super.startActivity(intent);
    }
}
