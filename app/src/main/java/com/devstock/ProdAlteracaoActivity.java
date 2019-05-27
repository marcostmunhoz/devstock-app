package com.devstock;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ProdAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnCadastrar, btnAlterar, btnVoltar;
    EditText etCod, etDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_alteracao);

        final Context ctx = this;

        apiHandler = ApiHandler.getInstance(this);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnAlterar = findViewById(R.id.btnAlterar);
        btnVoltar = findViewById(R.id.btnVoltar);
        etCod = findViewById(R.id.etCod);
        etDesc = findViewById(R.id.etDesc);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    apiHandler.newProduto(Helpers.createJsonObject(
                            "cod_produto", etCod.getText().toString(),
                            "nm_produto", etDesc.getText().toString()
                    ), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Toast.makeText(ProdAlteracaoActivity.this, "Produto cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String content = new String(error.networkResponse.data, "UTF-8");
                                Toast.makeText(ProdAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                            } catch (Exception ex) {
                                Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(ProdAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
