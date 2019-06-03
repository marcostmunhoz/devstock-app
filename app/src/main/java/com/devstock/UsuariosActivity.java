package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.adapters.UsuariosAdapter;
import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.models.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

public class UsuariosActivity extends AppCompatActivity {
    ApiHandler apiHandler;
    EditText etBusca;
    Button btnConsultar, btnLimpar, btnCadastrar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        apiHandler = ApiHandler.getInstance(this);

        etBusca = findViewById(R.id.etBusca);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        listView = findViewById(R.id.listView);

        if (!ApiHandler.permiteEditarUsuario()) {
            btnCadastrar.setEnabled(false);
        }

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBusca();
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampo();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaUsuario(null);
            }
        });

        realizarBusca();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            limparCampo();
        }
    }

    public void realizarBusca() {
        listView.setAdapter(null);
        try {
            final ProgressDialog dialog = Helpers.showLoading(this, "Buscando usuários...");

            apiHandler.getUsuariosLike(etBusca.getText().toString(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        setListaUsuarios(response.toString());
                    } catch (Exception ex) {
                        Toast.makeText(UsuariosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String content = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(UsuariosActivity.this, content, Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(UsuariosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        dialog.cancel();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(UsuariosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void limparCampo() {
        etBusca.setText("");
        realizarBusca();
    }

    public void setListaUsuarios(String data) {
        Usuario[] usus = Helpers.deserialize(data, Usuario[].class);
        UsuariosAdapter listUsus = new UsuariosAdapter(new ArrayList<>(Arrays.asList(usus)), this);
        listUsus.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                abrirTelaUsuario(id);
            }
        });
        listUsus.setOnDeleteButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                deleteUsuario(id);
            }
        });
        listUsus.setOnResetPasswordButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag(R.id.item_id);

                resetSenhaUsuario(id);
            }
        });

        listView.setAdapter(listUsus);
    }

    public void deleteUsuario(int id) {
        apiHandler.deleteUsuario(id, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(UsuariosActivity.this, "Usuário excluído com sucesso", Toast.LENGTH_LONG).show();
                realizarBusca();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String content = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(UsuariosActivity.this, content, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(UsuariosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void resetSenhaUsuario(int id) {
        apiHandler.resetSenhaUsuario(id, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(UsuariosActivity.this, "Senha resetada com sucesso", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String content = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(UsuariosActivity.this, content, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(UsuariosActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaUsuario(Integer id) {
        Intent intent = new Intent(this, FornAlteracaoActivity.class);
        if (id != null) {
            intent.putExtra("id_usuario", id);
        }
        startActivityForResult(intent, 3);
    }
}
