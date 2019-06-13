package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;

import java.util.ArrayList;

public class AlterarSenhaActivity extends AppCompatActivity {
    EditText etSenhaAtual, etSenhaNova, etSenhaNovaConf;
    Button btnAlterarSenha;
    ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        apiHandler = ApiHandler.getInstance(this);

        etSenhaAtual = findViewById(R.id.etSenhaAtual);
        etSenhaNova = findViewById(R.id.etSenhaNova);
        etSenhaNovaConf = findViewById(R.id.etSenhaNovaConf);
        btnAlterarSenha = findViewById(R.id.btnAlterarSenha);

        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarSenha();
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

    public void alterarSenha() {
        String senhaAtual = etSenhaAtual.getText().toString(),
                senhaNova = etSenhaNova.getText().toString(),
                senhaNovaConf = etSenhaNovaConf.getText().toString();

        if (senhaAtual.length() == 0 || senhaNova.length() == 0 || senhaNovaConf.length() == 0) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos.", Toast.LENGTH_LONG).show();
        } else if (senhaNova.length() < 8) {
            Toast.makeText(this, "A nova senha deve ter ao menos 8 caracteres.", Toast.LENGTH_LONG).show();
        } else if (!senhaNova.equals(senhaNovaConf)) {
            Toast.makeText(this, "A senha nova e a confirmação da senha não conferem.", Toast.LENGTH_LONG).show();
        } else {
            try {
                final ProgressDialog dialog = Helpers.showLoading(this, "Verificando informações...");

                apiHandler.alteraSenhaUsuario(senhaAtual, senhaNova, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            Helpers.tratarRetorno(AlterarSenhaActivity.this, response, false);
                            ApiHandler.setToken(null);
                            ApiHandler.setUser(null);
                            Helpers.removePrefs(AlterarSenhaActivity.this, "AUTH_TOKEN");
                            Intent intent = new Intent(AlterarSenhaActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            AlterarSenhaActivity.super.startActivity(intent);
                            finish();
                        } catch (Exception ex) {
                            Toast.makeText(AlterarSenhaActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            dialog.cancel();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Helpers.tratarRetorno(AlterarSenhaActivity.this, error, true);
                        } catch (Exception ex) {
                            Toast.makeText(AlterarSenhaActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            dialog.cancel();
                        }
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
