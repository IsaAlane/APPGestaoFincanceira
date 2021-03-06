package com.ifmg.carteiramensal.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ifmg.carteiramensal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemListaEventos extends ArrayAdapter<Evento> {

    private Context contextoPai;
    private ArrayList<Evento> eventos;

    private static class ViewHolder{
        private TextView nomeTxt;
        private TextView valorTxt;
        private TextView dataTxt;
        private TextView repeteTxt;
        private TextView fotoTxt;

    }


    public ItemListaEventos(Context contexto, ArrayList<Evento> dados){
        super(contexto, R.layout.item_lista_eventos, dados);

        this.contextoPai = contexto;
        this.eventos = dados;

    }

    @NonNull
    @Override
    public View getView(int indice, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(indice, convertView, parent);

        Evento eventoAtual = eventos.get(indice);
        ViewHolder novaView;

        final View resultado;

        if(convertView ==null){
            novaView = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_eventos, parent, false);

            novaView.dataTxt = (TextView) convertView.findViewById(R.id.dataItem);
            novaView.fotoTxt = (TextView) convertView.findViewById(R.id.fotoItem);
            novaView.nomeTxt = (TextView) convertView.findViewById(R.id.nomeItem);
            novaView.repeteTxt = (TextView) convertView.findViewById(R.id.repeteItem);
            novaView.valorTxt = (TextView) convertView.findViewById(R.id.valorItem);

            resultado = convertView;
            convertView.setTag(novaView);
        }else{
            novaView = (ViewHolder) convertView.getTag();
            resultado = convertView;
        }

        //setar valores de cada campo
        novaView.nomeTxt.setText(eventoAtual.getNome());
        novaView.valorTxt.setText(eventoAtual.getValor()+"");
        novaView.fotoTxt.setText(eventoAtual.getCaminhoFoto() == null ? "Não" : "Sim");
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        novaView.dataTxt.setText(formataData.format(eventoAtual.getOcorreu()));

        //Verifica se repete o evento
        Calendar data1 = Calendar.getInstance();
        data1.setTime(eventoAtual.getOcorreu());

        Calendar data2 = Calendar.getInstance();
        data2.setTime(eventoAtual.getValida());

        if(data1.get(Calendar.MONTH) != data2.get(Calendar.MONTH)){
            novaView.repeteTxt.setText("Sim");
        }else{
            novaView.repeteTxt.setText("Não");
        }
        return resultado;

    }
}
