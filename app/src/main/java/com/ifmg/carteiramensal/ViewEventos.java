package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEventos extends AppCompatActivity {


    private TextView tituloTxt, totalTxt;
    private ListView listaEventos;
    private Button novoBtn, cancelarBtn;

    //0 = entrada 1 = saída
    private int operacao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_eventos);

        tituloTxt = (TextView) findViewById(R.id.tituloTxt);
        totalTxt = (TextView) findViewById(R.id.totalTxt);
        listaEventos = (ListView) findViewById(R.id.listaEventos);
        novoBtn = (Button) findViewById(R.id.novoBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        Intent intencao = getIntent();
        operacao = intencao.getIntExtra("acao", -1);

        ajusteOp();
        cadastrarEventos();

    }


    private void cadastrarEventos(){
        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(operacao != -1){
                    Intent trocaActv = new Intent(ViewEventos.this, CRUDEventos.class);

                    if(operacao==0){
                        trocaActv.putExtra("acao", 0);
                    }else{
                        trocaActv.putExtra("acao", 0);
                    }
                    startActivity(trocaActv);
                }


            }
        });
    }

    private void ajusteOp(){
        if(operacao==0){
            tituloTxt.setText("Entradas");
        }else{
            if(operacao==1){
                tituloTxt.setText("Saídas");
            }else{
                Toast.makeText(ViewEventos.this, "Erro no parametro acao", Toast.LENGTH_LONG).show();
            }
        }
    }

}