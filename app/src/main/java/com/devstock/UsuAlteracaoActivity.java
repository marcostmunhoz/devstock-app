package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Usuario;

public class UsuAlteracaoActivity extends AppCompatActivity {
    ApiHandler apiHandler;

    Button btnSalvar, btnVoltar;
    EditText etNmUsuario, etLogin, etPassword, etEmail, etDtCad, etDtEdit;
    CheckBox cbFlgEditUsu, cbFlgEditForn, cbFlgEditProd, cbFlgMov;
    Integer idUsuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usu_alteracao);

        apiHandler = ApiHandler.getInstance(this);

        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltar = findViewById(R.id.btnVoltar);
        etNmUsuario = findViewById(R.id.etNmUsuario);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etDtCad = findViewById(R.id.etDtCad);
        etDtEdit = findViewById(R.id.etDtEdit);

        cbFlgEditUsu = findViewById(R.id.cbFlgEditUsu);
        cbFlgEditForn = findViewById(R.id.cbFlgEditForn);
        cbFlgEditProd = findViewById(R.id.cbFlgEditProd);
        cbFlgMov = findViewById(R.id.cbFlgMov);

        if (!ApiHandler.permiteEditarUsuario()) {
            btnSalvar.setEnabled(false);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UsuAlteracaoActivity.this.idUsuario != null) {
                    editarForn();
                } else {
                    cadastrarUsuario();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.containsKey("id_usuario")) {
                idUsuario = bundle.getInt("id_usuario");
                etPassword.setEnabled(false);

                getUsuario(idUsuario);
            }
        }
    }

    public void getUsuario(int id) {
        final ProgressDialog dialog = Helpers.showLoading(this, "Carregando informações do usuário...");

        apiHandler.getUsuario(idUsuario, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    Usuario u = Helpers.deserialize(response.toString(), Usuario.class);

                    carregarInfosUsu(u);
                } catch (Exception ex) {
                    Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String content = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(UsuAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    dialog.cancel();
                }
            }
        });
    }

    public void carregarInfosUsu(Usuario u) throws Exception {
        if (u != null) {
            etNmUsuario.setText(u.nmUsuario);
            etLogin.setText(u.login);
            etEmail.setText(u.email);
            cbFlgEditUsu.setChecked(u.getFlgEditUsu());
            cbFlgEditForn.setChecked(u.getFlgEditForn());
            cbFlgEditProd.setChecked(u.getFlgEditProd());
            cbFlgMov.setChecked(u.getFlgMov());
            etDtCad.setText(u.getDtCadastro());
            etDtEdit.setText(u.getDtEdicao());
        } else {
            throw new Exception("Usuário não encontrado.");
        }
    }

    public void cadastrarUsuario() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Realizando cadastro...");
            Usuario u = new Usuario(
                    null,
                    etNmUsuario.getText().toString(),
                    etLogin.getText().toString(),
                    etPassword.getText().toString(),
                    etEmail.getText().toString(),
                    cbFlgEditUsu.isChecked(),
                    cbFlgEditForn.isChecked(),
                    cbFlgEditProd.isChecked(),
                    cbFlgMov.isChecked()
            );

            apiHandler.newUsuario(u, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(UsuAlteracaoActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    try {
                        Usuario u = Helpers.deserialize(response.toString(), Usuario.class);

                        idUsuario = u.idUsuario;

                        carregarInfosUsu(u);
                    } catch (Exception ex) {
                        Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(UsuAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void editarForn() {
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Salvando alterações...");
            Usuario u = new Usuario(
                    idUsuario,
                    etNmUsuario.getText().toString(),
                    etLogin.getText().toString(),
                    null,
                    etEmail.getText().toString(),
                    cbFlgEditUsu.isChecked(),
                    cbFlgEditForn.isChecked(),
                    cbFlgEditProd.isChecked(),
                    cbFlgMov.isChecked()
            );

            apiHandler.setUsuario(u, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    dialog.cancel();
                    Toast.makeText(UsuAlteracaoActivity.this, "Usuário editado com sucesso.", Toast.LENGTH_LONG).show();

                    try {
                        Usuario u = Helpers.deserialize(response.toString(), Usuario.class);

                        carregarInfosUsu(u);
                    } catch (Exception ex) {
                        Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(UsuAlteracaoActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(UsuAlteracaoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
