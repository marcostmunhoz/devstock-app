package com.devstock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

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
                        ApiHandler.setToken(token);
                        openMenu();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreenActivity.this, "Sessão expirada.\nPor favor, realize o login novamente.", Toast.LENGTH_SHORT).show();
                        openLogin();
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(SplashScreenActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
