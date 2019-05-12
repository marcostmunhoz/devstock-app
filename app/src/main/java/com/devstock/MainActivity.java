package com.devstock;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText etLogin;
    EditText etSenha;
    Button btnEntrar;
    Button btnSair;
    ApiHandler apiHandler;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiHandler = ApiHandler.getInstance(this);
        pref = getSharedPreferences("devstock_prefs", MODE_PRIVATE);

        if (pref.contains("token")) {
            // implementar entrada automática caso o token esteja ativo
        }

        etLogin = findViewById(R.id.etLogin);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnSair = findViewById(R.id.btnSair);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString(),
                        senha = etSenha.getText().toString();

                try {
                    apiHandler.logInUser(login, senha, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            handleLogin(response);
                        }
                    }, null);
                } catch (Exception ex) {
                    // tratar erros
                }
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void handleLogin(Object data) {
        try {
            JSONObject json = Helpers.flattenJsonObject((JSONObject) data, null, null);

            if (json.has("status") && json.getString("status").equals("ok")) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", json.getString("data.token"));
                editor.putString("name", json.getString("data.user.nm_usuario"));
                editor.commit();

                openMenu();
            }
        } catch (Exception ex) {
            // tratar erros
        }
    }

    public void openMenu() {
        Intent intent = new Intent(this, ProdutosCRUD.class);

        Toast.makeText(this, "Seja bem vindo, " + pref.getString("name", ""), Toast.LENGTH_SHORT).show();

        startActivity(intent);
        finish();
    }
}
