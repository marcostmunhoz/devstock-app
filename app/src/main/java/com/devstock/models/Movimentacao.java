package com.devstock.models;

import com.devstock.helpers.Helpers;
import com.google.gson.annotations.Expose;

public class Movimentacao extends BaseModel {
    @Expose
    public Integer idMovimentacao;
    @Expose
    public Integer tpMovimentacao;
    @Expose
    public String dsMovimentacao;
    @Expose (serialize = false)
    public Integer idUsuario;
    @Expose (serialize = false)
    public Usuario usuario;
    @Expose
    public ProdutoMovimentacao[] produtosMovimentacao;

    public Movimentacao(Integer tpMovimentacao, String dsMovimentacao, ProdutoMovimentacao[] produtosMovimentacao) {
        this.tpMovimentacao = tpMovimentacao;
        this.dsMovimentacao = dsMovimentacao;
        this.produtosMovimentacao = produtosMovimentacao;
    }

    public Movimentacao() {
        this(null, null, null);
    }
}
