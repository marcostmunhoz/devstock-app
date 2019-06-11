package com.devstock.models;

import com.google.gson.annotations.Expose;

public class ProdutoMovimentacao extends BaseModel {
    @Expose
    public Integer idProdutoMovimentacao;
    @Expose
    public Integer idProduto;
    @Expose
    public Integer idMovimentacao;
    @Expose
    public Integer nrQtdMovimentada;
    @Expose
    public Float vlrUnitario;
    @Expose (serialize = false)
    public Produto produto;

    public ProdutoMovimentacao(Integer nrQtdMovimentada, Float vlrUnitario, Produto produto) {
        this.nrQtdMovimentada = nrQtdMovimentada;
        this.vlrUnitario = vlrUnitario;
        this.produto = produto;
        if (this.produto != null) {
            this.idProduto = this.produto.idProduto;
        }
    }

    public ProdutoMovimentacao() {
        this(null, null, null);
    }
}
