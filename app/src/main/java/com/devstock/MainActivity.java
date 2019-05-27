package com.devstock;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    EditText etLogin;
    EditText etSenha;
    Button btnEntrar;
    Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiHandler = ApiHandler.getInstance(this);

        etLogin = findViewById(R.id.etLogin);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnSair = findViewById(R.id.btnSair);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString(),
                        senha = etSenha.getText().toString();

                final ProgressDialog dialog = Helpers.showLoading(MainActivity.this, "Realizando login...");

                try {
                    apiHandler.logInUser(login, senha, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            handleLogin(response);
                            dialog.cancel();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Usuário e/ou senha inválidos.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                String token = json.getString("data.token");
                ApiHandler.setToken(token);
                openMenu();
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
