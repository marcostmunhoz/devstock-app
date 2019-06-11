package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.devstock.R;
import com.devstock.models.Produto;

import java.util.ArrayList;

public class ProdSelecionarAdapter extends ArrayAdapter<Produto> implements View.OnClickListener {
    private View.OnClickListener onButtonClickListener;

    Context context;

    public ProdSelecionarAdapter(ArrayList<Produto> data, Context context) {
        super(context, R.layout.item_prod_select, data);
        this.context = context;
    }

    public void setOnButtonClickListener(View.OnClickListener listener) {
        this.onButtonClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (this.onButtonClickListener != null) {
            this.onButtonClickListener.onClick(v);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.item_prod_select, null);
        }

        Produto p = getItem(position);

        if (p != null) {
            TextView tvCod = v.findViewById(R.id.tvCod),
                    tvNome = v.findViewById(R.id.tvNome),
                    tvQtd = v.findViewById(R.id.tvQtd);
            Button btnSelecionar = v.findViewById(R.id.btnSelecionar);

            if (tvCod != null) {
                tvCod.setText(p.codProduto);
            }

            if (tvNome != null) {
                tvNome.setText(p.nmProduto);
            }

            if (tvQtd != null) {
                tvQtd.setText("Quantidade: " + p.nrQtdEstocada.toString());
            }

            if (btnSelecionar != null) {
                btnSelecionar.setOnClickListener(this);
                btnSelecionar.setTag(R.id.item_produto, p);
            }
        }

        return v;
    }
}
