package com.devstock.models;

import com.google.gson.annotations.Expose;

public class Fornecedor extends BaseModel {
    @Expose
    public Integer idFornecedor;
    @Expose
    public String razaoSocial;
    @Expose
    public String nomeFantasia;
    @Expose
    public String cnpjFornecedor;
    @Expose
    public String endFornecedor;
    @Expose
    public String foneFornecedor;
    @Expose
    public String emailFornecedor;

    public Fornecedor(Integer idFornecedor, String razaoSocial, String nomeFantasia, String cnpjFornecedor, String endFornecedor, String foneFornecedor, String emailFornecedor) {
        this.idFornecedor = idFornecedor;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpjFornecedor = cnpjFornecedor;
        this.endFornecedor = endFornecedor;
        this.foneFornecedor = foneFornecedor;
        this.emailFornecedor = emailFornecedor;
    }

    public Fornecedor() {
        this(null, null, null, null, null, null, null);
    }
}
