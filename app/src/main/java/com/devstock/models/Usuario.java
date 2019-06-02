package com.devstock.models;

import com.google.gson.annotations.Expose;

public class Usuario extends BaseModel {
    @Expose
    public Integer idUsuario;
    @Expose
    public String nmUsuario;
    @Expose
    public String login;
    @Expose
    public String password;
    @Expose
    public String email;
    @Expose
    protected Integer flgEditUsu;
    @Expose
    protected Integer flgEditForn;
    @Expose
    protected Integer flgEditProd;
    @Expose
    protected Integer flgMov;

    public boolean getFlgEditUsu() {
        return this.flgEditUsu == 1;
    }

    public boolean getFlgEditForn() {
        return this.flgEditForn == 1;
    }

    public boolean getFlgEditProd() {
        return this.flgEditProd == 1;
    }

    public boolean getFlgMov() {
        return this.flgMov == 1;
    }
}
