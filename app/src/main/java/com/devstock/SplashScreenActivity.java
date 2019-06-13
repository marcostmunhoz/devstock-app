package com.devstock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Usuario;

import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        apiHandler = ApiHandler.getInstance(this);

        final String token = Helpers.getPrefs(this, "AUTH_TOKEN");

        if (token != null) {
            try {
                Helpers.verificarSessao(this, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            Usuario usuario = Helpers.deserialize(((JSONObject) response).getJSONObject("data").getJSONObject("user").toString(), Usuario.class);

                            ApiHandler.setToken(token);
                            ApiHandler.setUser(usuario);
                            openMenu();
                        } catch (Exception ex) {
                            Toast.makeText(SplashScreenActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreenActivity.this, "Sessão expirada.\nPor favor, realize o login novamente.", Toast.LENGTH_LONG).show();
                        openLogin();
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(SplashScreenActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            openLogin();
        }
    }

    public void openMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void openLogin() {
        Helpers.setPrefs(this, "AUTH_TOKEN", null);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
