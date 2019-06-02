package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.devstock.handlers.ApiHandler;
import com.devstock.R;
import com.devstock.models.Usuario;

import java.util.ArrayList;

public class UsuariosAdapter extends ArrayAdapter<Usuario> implements View.OnClickListener {
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onDeleteButtonClickListener;
    private View.OnClickListener onResetPasswordButtonClickListener;

    Context context;

    public UsuariosAdapter(ArrayList<Usuario> data, Context context) {
        super(context, R.layout.item_usuario, data);
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnDeleteButtonClickListener(View.OnClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    public void setOnResetPasswordButtonClickListener(View.OnClickListener listener) {
        this.onResetPasswordButtonClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExcluirUsuario) {
            if (this.onDeleteButtonClickListener != null) {
                this.onDeleteButtonClickListener.onClick(v);
            }
        } else if (v.getId() == R.id.btnResetarSenha) {
            if (this.onResetPasswordButtonClickListener != null) {
                this.onResetPasswordButtonClickListener.onClick(v);
            }
        } else {
            if (this.onItemClickListener != null) {
                this.onItemClickListener.onClick(v);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.item_usuario, null);
        }

        Usuario u = getItem(position);

        if (u != null) {
            TextView tvNome = v.findViewById(R.id.tvNome),
                    tvLogin = v.findViewById(R.id.tvLogin),
                    tvEmail = v.findViewById(R.id.tvEmail);
            Button btnExcluirUsuario = v.findViewById(R.id.btnExcluirUsuario),
                    btnResetarSenha = v.findViewById(R.id.btnResetarSenha);

            if (tvNome != null) {
                tvNome.setText(u.nmUsuario);
            }

            if (tvLogin != null) {
                tvLogin.setText(u.login);
            }

            if (tvEmail != null) {
                tvEmail.setText(u.email);
            }

            if (btnExcluirUsuario != null) {
                btnExcluirUsuario.setOnClickListener(this);
                btnExcluirUsuario.setTag(R.id.item_id, u.idUsuario);

                if (!ApiHandler.permiteEditarUsuario()) {
                    btnExcluirUsuario.setEnabled(false);
                }
            }

            if (btnResetarSenha != null) {
                btnResetarSenha.setOnClickListener(this);
                btnResetarSenha.setTag(R.id.item_id, u.idUsuario);

                if (!ApiHandler.permiteEditarUsuario()) {
                    btnResetarSenha.setEnabled(false);
                }
            }

            v.setTag(R.id.item_id, u.idUsuario);
            v.setOnClickListener(this);
        }

        return v;
    }
}
