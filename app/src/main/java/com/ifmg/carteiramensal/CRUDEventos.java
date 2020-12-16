package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifmg.carteiramensal.modelo.Evento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Ferramentas.EventosDB;

public class CRUDEventos extends AppCompatActivity {

    private DatePickerDialog calendarioUser;
    private TextView tituloTxt, dataTxt;
    private EditText nomeTxt, valorTxt;
    private CheckBox repeteBtn;
    private Button salvarBtn, cancelarBtn, fotoBtn;
    private ImageView foto;
    private Calendar calendarioTemp;


    //0 = cadastro entrada
    //1 = cadastro saida
    //2 = edicao entrada
    //3 edicao saida
    private int acao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r_u_d_eventos);

        tituloTxt = (TextView) findViewById(R.id.tituloCadastroTxt);
        nomeTxt = (EditText) findViewById(R.id.nomeCadastroTxt);
        valorTxt = (EditText) findViewById(R.id.valorCadastroTxt);
        dataTxt =  (TextView) findViewById(R.id.dataCadastroTxt);
        repeteBtn = (CheckBox) findViewById(R.id.repeteBtn);
        salvarBtn = (Button) findViewById(R.id.salvarCadastroBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarCadastroBtn);
        fotoBtn = (Button) findViewById(R.id.fotoBtn);
        foto = (ImageView) findViewById(R.id.fotoTxt);

        Intent intencao = getIntent();
        acao = intencao.getIntExtra("acao", -1);

        ajustaPorAcao();
        cadastraEventos();

    }

    private void cadastraEventos(){

        calendarioTemp = Calendar.getInstance();
        calendarioUser = new DatePickerDialog(CRUDEventos.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);
                dataTxt.setText(dia+"/"+(mes+1)+"/"+ano);
            }
        }, calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH));

        dataTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioUser.show();
            }
        });

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastraNovoEvento();
            }
        });


    }

    private void ajustaPorAcao(){

        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        dataTxt.setText(formatador.format(hoje.getTime()));
        switch (acao){
            case 0: {
                tituloTxt.setText("Cadastro de Entrada");
                break;
            }
            case 1: {
                tituloTxt.setText("Cadastro de Saída");
                break;
            }
            case 2: {
                tituloTxt.setText("Edição de Entrada");
                break;
            }
            case 3: {
                tituloTxt.setText("Edição de saída");
                break;
            } default:{

            }
        }

    }

    private void cadastraNovoEvento(){
        String nome = nomeTxt.getText().toString();
        double valor = Double.parseDouble(valorTxt.getText().toString());

        if(acao==1 || acao==3){
            valor *= -1;
        }


        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String dataStrg = dataTxt.getText().toString();

        try {
            Date diaEvento = formatador.parse(dataStrg);
            Calendar dataLimite = Calendar.getInstance();
            dataLimite.setTime(calendarioTemp.getTime());


            if(repeteBtn.isChecked()){

            }

            dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));

            Evento novoEvento = new Evento(nome,null, valor, new Date(), dataLimite.getTime(), diaEvento);
            EventosDB bd = new EventosDB(CRUDEventos.this);
            bd.insereEvento(novoEvento);

            Toast.makeText(CRUDEventos.this,"Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
            finish();

        }catch(ParseException ex){
            System.err.print("erro no formato da data");
        }



    }
}