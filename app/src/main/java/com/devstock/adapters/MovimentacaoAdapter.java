package com.devstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devstock.R;
import com.devstock.models.Movimentacao;

import java.util.ArrayList;

public class MovimentacaoAdapter extends ArrayAdapter<Movimentacao> implements View.OnClickListener {
    private View.OnClickListener onItemClickListener;
    Context context;

    public MovimentacaoAdapter(ArrayList<Movimentacao> data, Context context) {
        super(context, R.layout.item_movimentacao, data);
        this.context = context;
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (this.onItemClickListener != null) {
            this.onItemClickListener.onClick(v);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        String dtHr;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.item_movimentacao, null);
        }

        Movimentacao m = getItem(position);

        try {
            dtHr = m.getDthrMovimentacao();
        } catch (Exception ex) {
            dtHr = m.dthrMovimentacao;
        }

        if (m != null) {
            TextView tvDtHrMovimentacao = v.findViewById(R.id.tvDtHrMovimentacao),
                    tvTpMovimentacao = v.findViewById(R.id.tvTpMovimentacao),
                    tvDsMovimentacao = v.findViewById(R.id.tvDsMovimentacao);

            if (tvDtHrMovimentacao != null) {
                tvDtHrMovimentacao.setText(dtHr);
            }

            if (tvTpMovimentacao != null) {
                tvTpMovimentacao.setText(m.tpMovimentacao == 1 ? "SAÍDA" : "ENTRADA");
            }

            if (tvDsMovimentacao != null) {
                tvDsMovimentacao.setText(m.dsMovimentacao);
            }

            v.setTag(R.id.item_id, m.idMovimentacao);
            v.setOnClickListener(this);
        }

        return v;
    }
}
