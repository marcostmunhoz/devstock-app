package com.devstock.models;

import com.devstock.helpers.Helpers;
import com.google.gson.annotations.Expose;

public class Movimentacao extends BaseModel {
    @Expose
    public Integer idMovimentacao;
    @Expose
    public Integer tpMovimentacao;
    @Expose
    public String dthrMovimentacao;
    @Expose
    public String dsMovimentacao;
    @Expose (serialize = false)
    public Integer idUsuario;
    @Expose (serialize = false)
    public Usuario usuario;
    @Expose (serialize = false)
    public ProdutoMovimentacao[] produtosMovimentacao;

    public Movimentacao(Integer idMovimentacao, Integer tpMovimentacao, String dthrMovimentacao, String dsMovimentacao, Integer idUsuario, String dtCadastro) {
        this.idMovimentacao = idMovimentacao;
        this.tpMovimentacao = tpMovimentacao;
        this.dthrMovimentacao = dthrMovimentacao;
        this.dsMovimentacao = dsMovimentacao;
        this.idUsuario = idUsuario;
        this.dtCadastro = dtCadastro;
    }

    public Movimentacao() {
        this(null, null, null, null, null, null);
    }

    public String getDthrMovimentacao() throws Exception {
        return Helpers.dateFromString(this.dtCadastro);
    }
}
