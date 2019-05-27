package com.devstock.models;

import com.devstock.Helpers;

import java.util.Date;

public class Produto implements IBaseModel {
    public Integer idProduto;
    public String codProduto;
    public String nmProduto;
    private Integer flgStatus;
    public Integer nrQtdEstocada;
    private String dtCadastro;
    private String dtEdicao;

    public boolean getFlgStatus() {
        return this.flgStatus >= 1 ? true : false;
    }

    public Date getDtCadastro() throws Exception {
        return Helpers.dateFromString(this.dtCadastro);
    }

    public Date getDtEdicao() throws Exception {
        return Helpers.dateFromString(this.dtEdicao);
    }
}
