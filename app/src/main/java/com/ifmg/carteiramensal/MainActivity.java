package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import Ferramentas.EventosDB;

public class MainActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView entrada, saida, saldo;
    private ImageButton entradaBtn, saidaBtn;
    private Button anteriorBtn, proximoBtn, novoBtn;
    private Calendar hoje;
    private Calendar dataApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = (TextView) findViewById(R.id.tituloMain);
        entrada = (TextView) findViewById(R.id.entradaText);
        saida = (TextView) findViewById(R.id.saidaText);
        saldo = (TextView) findViewById(R.id.saldoText);

        entradaBtn = (ImageButton) findViewById(R.id.entradaBtn);
        saidaBtn = (ImageButton) findViewById(R.id.saidaBtn);

        anteriorBtn = (Button) findViewById(R.id.anteriorBtn);
        proximoBtn = (Button) findViewById(R.id.proximoBtn);
        novoBtn = (Button) findViewById(R.id.novoBtn);

        cadastroEvento();

        dataApp = Calendar.getInstance();
        hoje = Calendar.getInstance();


        mostraDataApp();

    }

    private void cadastroEvento(){
        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(-1);
            }
        });

        proximoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(1);
            }
        });

        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventosDB db = new EventosDB(MainActivity.this);
                db.insereEvento();


                //erro no método .show() - can not find
                // Toast.makeText(MainActivity.this , db.getDatabaseName(), Toast.LENGTH_LONG.show());
            }
        });
    }


    private void mostraDataApp(){
        String nomeMes[] = {"Janeiro", "Fevereiro", "Março", "Abril",
                "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro","Dezembro"};

        int mes = dataApp.get(Calendar.MONTH);
        int ano = dataApp.get(Calendar.YEAR);

        titulo.setText(nomeMes[mes] +"/"+ ano);

    }

    private void atualizaMes(int ajuste){

        dataApp.add(Calendar.MONTH, ajuste);
        if(ajuste>0){
                if(dataApp.after(hoje)){
                    dataApp.add(Calendar.MONTH, -1);
                }
        }else{

        }

        mostraDataApp();
    }


}