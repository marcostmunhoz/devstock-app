package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.devstock.helpers.Helpers;
import com.devstock.R;
import com.devstock.models.Fornecedor;

import java.util.ArrayList;

public class FornSelecionarAdapter extends ArrayAdapter<Fornecedor> implements View.OnClickListener {
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onButtonClickListener;

    Context context;

    public FornSelecionarAdapter(ArrayList<Fornecedor> data, Context context) {
        super(context, R.layout.item_fornecedor_select, data);
        this.context = context;
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        this.onButtonClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSelecionarFornecedor) {
            if (this.onButtonClickListener != null) {
                this.onButtonClickListener.onClick(v);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.item_fornecedor_select, null);
        }

        Fornecedor f = getItem(position);

        if (f != null) {
            TextView tvRazao = v.findViewById(R.id.tvRazao),
                    tvCnpj = v.findViewById(R.id.tvCnpj);
            Button btnSelecionarFornecedor = v.findViewById(R.id.btnSelecionarFornecedor);

            if (tvRazao != null) {
                tvRazao.setText(f.razaoSocial);
            }

            if (tvCnpj != null) {
                tvCnpj.setText(Helpers.formatString("##.###.###/####-##", f.cnpjFornecedor));
            }

            if (btnSelecionarFornecedor != null) {
                btnSelecionarFornecedor.setOnClickListener(this);
                btnSelecionarFornecedor.setTag(R.id.item_id, f.idFornecedor);
                btnSelecionarFornecedor.setTag(R.id.item_razao_social, f.razaoSocial);
            }

            v.setOnClickListener(this);
        }

        return v;
    }
}