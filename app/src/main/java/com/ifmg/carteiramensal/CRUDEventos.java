package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ifmg.carteiramensal.modelo.Evento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import Ferramentas.EventosDB;

public class CRUDEventos extends AppCompatActivity {

    private DatePickerDialog calendarioUser;
    private TextView tituloTxt, dataTxt;
    private EditText nomeTxt, valorTxt;
    private CheckBox repeteBtn;
    private Button salvarBtn, cancelarBtn, fotoBtn;
    private ImageView foto;
    private Calendar calendarioTemp;
    private Spinner mesesRepeteSpi;


    //0 = cadastro entrada
    //1 = cadastro saida
    //2 = edicao entrada
    //3 edicao saida
    private int acao = -1;
    private Evento eventoSelecionado;
    private String nomeFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r_u_d_eventos);

        tituloTxt = (TextView) findViewById(R.id.tituloCadastroTxt);
        nomeTxt = (EditText) findViewById(R.id.nomeCadastroTxt);
        valorTxt = (EditText) findViewById(R.id.valorCadastroTxt);
        dataTxt = (TextView) findViewById(R.id.dataCadastroTxt);
        repeteBtn = (CheckBox) findViewById(R.id.repeteBtn);
        salvarBtn = (Button) findViewById(R.id.salvarCadastroBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarCadastroBtn);
        fotoBtn = (Button) findViewById(R.id.fotoBtn);
        foto = (ImageView) findViewById(R.id.fotoTxt);
        mesesRepeteSpi = (Spinner) findViewById(R.id.mesesSpinner);

        Intent intencao = getIntent();
        acao = intencao.getIntExtra("acao", -1);

        ajustaPorAcao();
        cadastraEventos();
        configuraSpinner();

    }

    private void configuraSpinner() {
        List<String> mes = new ArrayList<>();

        for (int i = 0; i <= 24; i++) {
            mes.add(i + "");
        }

        ArrayAdapter<String> listaAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, mes);
        mesesRepeteSpi.setAdapter(listaAdapter);
        mesesRepeteSpi.setEnabled(false);
    }


    private void cadastraEventos() {

        calendarioTemp = Calendar.getInstance();
        calendarioUser = new DatePickerDialog(CRUDEventos.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);
                dataTxt.setText(dia + "/" + (mes + 1) + "/" + ano);
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
                if(acao<2){
                    cadastraNovoEvento();
                }else{
                    //update no banco de dados pelo método
                    updateEvento();
                }
            }
        });


        //desativar e ativar spinner
        repeteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeteBtn.isChecked()) {
                    mesesRepeteSpi.setEnabled(true);
                } else {
                    mesesRepeteSpi.setEnabled(false);
                }
            }
        });


        //finalizando execução da activity
        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conferindo acao para ativar o botao
                if(acao<2){
                    finish();
                }else{
                    //deletar dados pelo método
                }

            }
        });

        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraActivity, 100);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            Bitmap imagemUser = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(imagemUser);
            foto.setBackground(null);

            salvarImagem(imagemUser);
        }
    }

    private void salvarImagem(Bitmap imagem){
        Random gerador = new Random();
        Date instante = new Date();

        //define nome da imagem
        String nome = gerador.nextInt() +""+ instante.getTime()+ ".png";

        nomeFoto = nome;
        File sd = Environment.getExternalStorageDirectory();
        File fotoArquivo =new File(sd, nome);

        //gravando imagem no dispositivo
        try{
            FileOutputStream gravador = new FileOutputStream(fotoArquivo);
            //comprimindo imagem
            imagem.compress(Bitmap.CompressFormat.PNG, 100, gravador);
            gravador.flush();
            gravador.close();

        }catch (Exception ex){
            System.out.print("erro ao armazenar a foto no dispositivo");
        }

    }

    private void carregarImagem(){
        if(nomeFoto!= null){
            //encontrar a pasta
            File sd = Environment.getExternalStorageDirectory();
            File arquivoLeitura = new File(sd, nomeFoto);


            try{
                //leitura
                FileInputStream leitor = new FileInputStream(arquivoLeitura);
                Bitmap imagem = BitmapFactory.decodeStream(leitor);

                foto.setImageBitmap(imagem);
                foto.setBackground(null);


            }catch (Exception e){
                System.out.print("erro na leitura do arquivo");
            }
        }
    }

    private void ajustaPorAcao() {

        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        dataTxt.setText(formatador.format(hoje.getTime()));
        switch (acao) {
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
                ajusteEdicao();
                break;
            }
            case 3: {
                tituloTxt.setText("Edição de saída");
                ajusteEdicao();
                break;
            }
            default: {

            }
        }

    }

    private void ajusteEdicao(){
        cancelarBtn.setText("excluir");
        salvarBtn.setText("atualizar");


        //carregar informação do evento do bd
        int id = Integer.parseInt(getIntent().getStringExtra("id"));

        if(id!=0){
            EventosDB db = new EventosDB(CRUDEventos.this);
            eventoSelecionado = db.buscaEventoId(id);

            //carregar informações nos campos
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");


            nomeTxt.setText(eventoSelecionado.getNome());
            valorTxt.setText(eventoSelecionado.getValor()+"");
            dataTxt.setText(formatador.format(eventoSelecionado.getOcorreu()));

            nomeFoto = eventoSelecionado.getCaminhoFoto();
            carregarImagem();

            //criando variáveis para a diferença
            Calendar d1 = Calendar.getInstance();
            d1.setTime(eventoSelecionado.getValida());

            Calendar d2 = Calendar.getInstance();
            d2.setTime(eventoSelecionado.getOcorreu());

            repeteBtn.setChecked(d1.get(Calendar.MONTH) != d2.get(Calendar.MONTH)? true : false);
            if(repeteBtn.isChecked()){
                mesesRepeteSpi.setEnabled(true);

                //calculo diferenca mes cadastro e mes valido
                mesesRepeteSpi.setSelection(d1.get(Calendar.MONTH) - d2.get(Calendar.MONTH) -1);

            }


        }
    }

    private void updateEvento(){
        eventoSelecionado.setNome(nomeTxt.getText().toString());
        eventoSelecionado.setValor(Double.parseDouble(valorTxt.getText().toString()));

        if(acao==3){
            eventoSelecionado.setValor(eventoSelecionado.getValor()*-1);
        }
        eventoSelecionado.setOcorreu(calendarioTemp.getTime());
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.setTime(calendarioTemp.getTime());


        if (repeteBtn.isChecked()) {
            String mesStr = (String) mesesRepeteSpi.getSelectedItem();

            dataLimite.add(Calendar.MONTH, Integer.parseInt(mesStr));
        }

        dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));
        eventoSelecionado.setValida(dataLimite.getTime());
        eventoSelecionado.setCaminhoFoto(nomeFoto);

        EventosDB db = new EventosDB(CRUDEventos.this);
        db.updateEvento(eventoSelecionado);
        finish();
    }



    private void cadastraNovoEvento() {
        String nome = nomeTxt.getText().toString();
        double valor = Double.parseDouble(valorTxt.getText().toString());

        if (acao == 1 || acao == 3) {
            valor *= -1;
        }


        //SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        //String dataStrg = dataTxt.getText().toString();

        //try {
        Date diaEvento = calendarioTemp.getTime();
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.setTime(calendarioTemp.getTime());


        if (repeteBtn.isChecked()) {
            String mesStr = (String) mesesRepeteSpi.getSelectedItem();

            dataLimite.add(Calendar.MONTH, Integer.parseInt(mesStr));
        }

        dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));

        Evento novoEvento = new Evento(nome, nomeFoto, valor, new Date(), dataLimite.getTime(), diaEvento);
        EventosDB bd = new EventosDB(CRUDEventos.this);
        bd.insereEvento(novoEvento);

        Toast.makeText(CRUDEventos.this, "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
        finish();

        /*}catch(ParseException ex){
        System.err.print("erro no formato da data");
        }*/
    }
}
