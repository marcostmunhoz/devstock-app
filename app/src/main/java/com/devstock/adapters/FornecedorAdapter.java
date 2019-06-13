package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.devstock.handlers.ApiHandler;
import com.devstock.helpers.Helpers;
import com.devstock.R;
import com.devstock.models.Fornecedor;

import java.util.ArrayList;

public class FornecedorAdapter extends ArrayAdapter<Fornecedor> implements View.OnClickListener {
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onButtonClickListener;

    Context context;

    public FornecedorAdapter(ArrayList<Fornecedor> data, Context context) {
        super(context, R.layout.item_fornecedor, data);
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        this.onButtonClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExcluirFornecedor) {
            if (this.onButtonClickListener != null) {
                this.onButtonClickListener.onClick(v);
            }
        } else {
            if (this.onItemClickListener != null) {
                this.onItemClickListener.onClick(v);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.item_fornecedor, null);
        }

        Fornecedor f = getItem(position);

        if (f != null) {
            TextView tvRazao = v.findViewById(R.id.tvRazao),
                    tvNomeFantasia = v.findViewById(R.id.tvNomeFantasia),
                    tvCnpj = v.findViewById(R.id.tvCnpj);
            Button btnExcluirFornecedor = v.findViewById(R.id.btnExcluirFornecedor);

            if (tvRazao != null) {
                tvRazao.setText(f.razaoSocial);
            }

            if (tvNomeFantasia != null) {
                tvNomeFantasia.setText(f.nomeFantasia);
            }

            if (tvCnpj != null) {
                tvCnpj.setText(Helpers.formatString("##.###.###/####-##", f.cnpjFornecedor));
            }

            if (btnExcluirFornecedor != null) {
                btnExcluirFornecedor.setOnClickListener(this);
                btnExcluirFornecedor.setTag(R.id.item_id, f.idFornecedor);

                if (!ApiHandler.permiteEditarFornecedor()) {
                    btnExcluirFornecedor.setEnabled(false);
                }
            }

            v.setTag(R.id.item_id, f.idFornecedor);
            v.setOnClickListener(this);
        }

        return v;
    }
}
