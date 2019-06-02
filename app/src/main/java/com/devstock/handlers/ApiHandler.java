package com.devstock.handlers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devstock.Helpers;
import com.devstock.models.Fornecedor;
import com.devstock.models.Produto;
import com.devstock.models.Usuario;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiHandler {
    private static final String BASE_PATH = "http://devstock.herokuapp.com/api";
    private static HashMap<Context, ApiHandler> instances = new HashMap<>();
    private static String AUTH_TOKEN = null;
    private static Usuario USER = null;

    private RequestQueue queue;

    private ApiHandler(Activity act) {
        this.queue = Volley.newRequestQueue(act);
    }

    private static synchronized Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + AUTH_TOKEN);
        return map;
    }

    public static synchronized ApiHandler getInstance(Activity act) {
        if (!instances.containsKey(act)) {
            instances.put(act, new ApiHandler(act));
        }

        return instances.get(act);
    }

    public static synchronized void setToken(String token) {
        AUTH_TOKEN = token;
    }

    public static synchronized boolean isLoggedIn() {
        return (AUTH_TOKEN != null);
    }

    public static synchronized void setUser(Usuario usuario) {
        USER = usuario;
    }

    public static synchronized Usuario getUser() {
        if (isLoggedIn()) {
            return USER;
        }

        return null;
    }

    public static synchronized boolean permiteEditarUsuario() {
        if (isLoggedIn()) {
            return getUser().getFlgEditUsu();
        }

        return false;
    }

    public static synchronized boolean permiteEditarFornecedor() {
        if (isLoggedIn()) {
            return getUser().getFlgEditForn();
        }

        return false;
    }

    public static synchronized boolean permiteEditarProduto() {
        if (isLoggedIn()) {
            return getUser().getFlgEditProd();
        }

        return false;
    }

    public static synchronized boolean permiteRealizarMovimentacao() {
        if (isLoggedIn()) {
            return getUser().getFlgMov();
        }

        return false;
    }

    public void logInUser(String username, String password, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject body = Helpers.createJsonObject(
                "login", username,
                "password", password
        );

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/login", body, success, error));
    }

    public void validateToken(final String token, Response.Listener success, Response.ErrorListener error) throws Exception {

        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/check-token/" + Uri.encode(token), null, success, error));
    }

    public void getProdutos(Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produtos", null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getProdutosLike(String query, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produtos/" + Uri.encode(query), null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getProduto(int idProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void newProduto(Produto prod, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(prod, Produto.class);

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/produto", dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void setProduto(Produto prod, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(prod, Produto.class);

        this.queue.add(new JsonObjectRequest(Request.Method.PUT, BASE_PATH + "/produto/" + prod.idProduto, dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void deleteProduto(int idProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.DELETE, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getFornecedores(Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/fornecedores", null, success, error) {
            @Override
            public Map<String, String> getHeaders() { return ApiHandler.getHeaders(); }
        });
    }

    public void getFornecedoresLike(String query, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/fornecedores/" + Uri.encode(query), null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getFornecedor(int idForn, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/fornecedor/" + idForn, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void newFornecedor(Fornecedor forn, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(forn, Fornecedor.class);

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/fornecedor", dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void setFornecedor(Fornecedor forn, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(forn, Fornecedor.class);

        this.queue.add(new JsonObjectRequest(Request.Method.PUT, BASE_PATH + "/fornecedor/" + forn.idFornecedor, dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void deleteFornecedor(int idForn, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.DELETE, BASE_PATH + "/fornecedor/" + idForn, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getUsuarios(Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/usuarios", null, success, error) {
            @Override
            public Map<String, String> getHeaders() { return ApiHandler.getHeaders(); }
        });
    }

    public void getUsuariosLike(String query, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/usuarios/" + Uri.encode(query), null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getUsuario(int idUsu, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/usuario/" + idUsu, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void newUsuario(Usuario usu, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(usu, Usuario.class);

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/usuario", dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void setUsuario(Usuario usu,Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject dados = Helpers.serialize(usu, Usuario.class);

        this.queue.add(new JsonObjectRequest(Request.Method.PUT, BASE_PATH + "/usuario/" + usu.idUsuario, dados, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void deleteUsuario(int idUsu, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.DELETE, BASE_PATH + "/usuario/" + idUsu, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void resetSenhaUsuario(int idUsu, Response.Listener success, Response.ErrorListener error) {

    }
 }
