package com.devstock.models;

import com.devstock.helpers.Helpers;
import com.google.gson.annotations.Expose;

public class BaseModel {
    @Expose (serialize = false)
    protected String dtCadastro;
    @Expose (serialize = false)
    protected String dtEdicao;
    @Expose (serialize = false)
    protected Integer flgStatus;

    public boolean getFlgStatus() {
        return this.flgStatus >= 1;
    }

    public String getDtCadastro() throws Exception {
        return Helpers.dateFromString(this.dtCadastro);
    }

    public String getDtEdicao() throws Exception {
        return Helpers.dateFromString(this.dtEdicao);
    }
}
