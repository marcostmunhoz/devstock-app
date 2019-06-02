package com.devstock.models;

import com.google.gson.annotations.Expose;

public class Produto extends BaseModel {
    @Expose
    public Integer idProduto;
    @Expose
    public String codProduto;
    @Expose
    public String nmProduto;
    @Expose
    public Integer nrQtdEstocada;
    @Expose
    public Integer idFornecedor;
    public Fornecedor fornecedor;

    public Produto(Integer idProduto, String codProduto, String nmProduto, Integer nrQtdEstocada, Integer idFornecedor, Fornecedor fornecedor) {
        this.idProduto = idProduto;
        this.codProduto = codProduto;
        this.nmProduto = nmProduto;
        this.nrQtdEstocada = nrQtdEstocada;
        this.idFornecedor = idFornecedor;
        this.fornecedor = fornecedor;
    }

    public Produto() {
        this(null, null, null, null, null, null);
    }
}
