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

public class SplashScreen extends AppCompatActivity {
    SharedPreferences pref;
    ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pref = getSharedPreferences("devstock_prefs", MODE_PRIVATE);
        apiHandler = ApiHandler.getInstance(this);

        final Context ctx = this;

        if (pref.contains("token")) {
            try {
                Helpers.verificarSessao(this, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        openMenu();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "Sessão expirada.\nPor favor, realize o login novamente.", Toast.LENGTH_SHORT).show();
                        openLogin();
                    }
                });
            } catch (Exception ex) { }
        } else {
            openLogin();
        }
    }

    public void openMenu() {
        Intent intent = new Intent(this, ProdutosCRUD.class);
        startActivity(intent);
        finish();
    }

    public void openLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
