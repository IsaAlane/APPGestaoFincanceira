package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ifmg.carteiramensal.modelo.Evento;

import java.util.ArrayList;
import java.util.Calendar;

import Ferramentas.EventosDB;

public class MainActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView entrada, saida, saldo;
    private ImageButton entradaBtn, saidaBtn;
    private Button anteriorBtn, proximoBtn, novoBtn;
    private Calendar hoje;
    static Calendar dataApp;


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
        atualizaValores();
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
               // EventosDB db = new EventosDB(MainActivity.this);
                //db.insereEvento();


                //erro no método .show() - can not find
                // Toast.makeText(MainActivity.this , db.getDatabaseName(), Toast.LENGTH_LONG.show());
            }
        });

        entradaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaActv = new Intent(MainActivity.this, ViewEventos.class);
                trocaActv.putExtra("acao", 0);
                startActivityForResult(trocaActv, 0);
            }
        });

        saidaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaActv = new Intent(MainActivity.this, ViewEventos.class);
                trocaActv.putExtra("acao", 1);
                startActivityForResult(trocaActv,1);

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
        atualizaValores();
        configuraPermissoes();
    }

    private void configuraPermissoes(){
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    private void atualizaValores(){
        EventosDB db = new EventosDB(MainActivity.this);


        ArrayList<Evento> saidasLista = db.buscaEventos(1, dataApp);
        ArrayList<Evento> entradasLista = db.buscaEventos(0, dataApp);

        double entradaTotal = 0.0;
        double saidaTotal = 0.0;

        for(int i=0; i< entradasLista.size(); i++){
            entradaTotal += entradasLista.get(i).getValor();
        }

        for(int i=0; i< saidasLista.size(); i++){
            saidaTotal += saidasLista.get(i).getValor();
        }

        double saldoTotal = entradaTotal - saidaTotal;

        entrada.setText(String.format("%.2f"+entradaTotal));
        saida.setText(String.format("%.2f"+saidaTotal));
        saldo.setText(String.format("%.2f"+saldoTotal));

    }

    protected void onActivityResult(int codigoRequest, int codigoResultado, Intent data) {
        super.onActivityResult(codigoRequest, codigoResultado, data);

        atualizaValores();
    }
}