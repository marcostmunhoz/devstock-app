package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devstock.R;
import com.devstock.models.Produto;

import java.util.ArrayList;

public class ProdutosAdapter extends ArrayAdapter<Produto> implements View.OnClickListener {
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onButtonClickListener;

    Context context;

    public ProdutosAdapter(ArrayList<Produto> data, Context context) {
        super(context, R.layout.item_produto, data);
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
        if (v.getId() == R.id.btnExcluirProduto) {
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
            v = inflater.inflate(R.layout.item_produto, null);
        }

        Produto p = getItem(position);

        if (p != null) {
            TextView tvCod = v.findViewById(R.id.tvCod),
                    tvNome = v.findViewById(R.id.tvNome),
                    tvQtd = v.findViewById(R.id.tvQtd);
            Button btnExcluirProduto = v.findViewById(R.id.btnExcluirProduto);

            if (tvCod != null) {
                tvCod.setText(p.codProduto);
            }

            if (tvNome != null) {
                tvNome.setText(p.nmProduto);
            }

            if (tvQtd != null) {
                tvQtd.setText("Quantidade: " + String.valueOf(p.nrQtdEstocada));
            }

            if (btnExcluirProduto != null) {
                btnExcluirProduto.setOnClickListener(this);
                btnExcluirProduto.setTag(R.id.item_id, p.idProduto);
            }

            v.setTag(R.id.item_id, p.idProduto);
            v.setOnClickListener(this);
        }

        return v;
    }
}
