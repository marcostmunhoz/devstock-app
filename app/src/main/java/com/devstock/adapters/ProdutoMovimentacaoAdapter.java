package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.devstock.R;
import com.devstock.models.ProdutoMovimentacao;

import java.util.ArrayList;

public class ProdutoMovimentacaoAdapter extends ArrayAdapter<ProdutoMovimentacao> implements View.OnClickListener {
    private View.OnClickListener onButtonClickListener;

    Context context;

    public ProdutoMovimentacaoAdapter(ArrayList<ProdutoMovimentacao> data, Context context) {
        super(context, R.layout.item_produto, data);
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
            v = inflater.inflate(R.layout.item_prod_mov, null);
        }

        ProdutoMovimentacao p = getItem(position);

        if (p != null) {
            TextView tvNomeProduto = v.findViewById(R.id.tvNomeProduto),
                    tvQtd = v.findViewById(R.id.tvQtd),
                    tvVlrUnitario = v.findViewById(R.id.tvVlrUnitario);
            Button btnExcluir = v.findViewById(R.id.btnExcluir);

            if (tvNomeProduto != null) {
                tvNomeProduto.setText(p.produto.nmProduto);
            }

            if (tvQtd != null) {
                tvQtd.setText("Qtd: " + p.nrQtdMovimentada.toString());
            }

            if (tvVlrUnitario != null) {
                tvVlrUnitario.setText("Valor unit: " + p.vlrUnitario.toString());
            }

            if (btnExcluir != null) {
                btnExcluir.setOnClickListener(this);
                btnExcluir.setTag(position);
            }
        }

        return v;
    }
}
