package com.devstock.models;

import com.google.gson.annotations.Expose;

public class Usuario extends BaseModel {
    @Expose
    public Integer idUsuario;
    @Expose
    public String nmUsuario;
    @Expose
    public String login;
    @Expose (deserialize = false)
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

    public Usuario(Integer idUsuario, String nmUsuario, String login, String password, String email, boolean flgEditUsu, boolean flgEditForn, boolean flgEditProd, boolean flgMov) {
        this.idUsuario = idUsuario;
        this.nmUsuario = nmUsuario;
        this.login = login;
        this.password = password;
        this.email = email;
        this.flgEditUsu = (flgEditUsu ? 1 : 0);
        this.flgEditForn = (flgEditForn ? 1 : 0);
        this.flgEditProd = (flgEditProd ? 1 : 0);
        this.flgMov = (flgMov ? 1 : 0);
    }

    public Usuario() {
        this(null, null, null, null, null, false, false, false, false);
    }

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
