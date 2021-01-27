package Ferramentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.ifmg.carteiramensal.modelo.Evento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventosDB extends SQLiteOpenHelper {

    private Context contexto;

    public EventosDB(Context cont){
        super(cont, "evento", null, 1);
        contexto = cont;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String criaTabela ="CREATE TABLE IF NOT EXISTS evento(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT, valor REAL, imagem TEXT, dataocorreu DATE, datacadastro DATE, datavalida DATE)";

        db.execSQL(criaTabela);
    }

    public void insereEvento(Evento novoEvento){

        try(SQLiteDatabase db = this.getWritableDatabase()){

        ContentValues valores = new ContentValues();

        valores.put("nome", novoEvento.getNome());
        valores.put("valor", novoEvento.getValor());
        valores.put("imagem", novoEvento.getCaminhoFoto());
        valores.put("dataocorreu", novoEvento.getOcorreu().getTime());
        valores.put("datacadastro", new Date().getTime());
        valores.put("datavalida", novoEvento.getValida().getTime());

        //erro na tabela
        //db.insert(table"evento", nullColumnHack null, valores);



        }catch (SQLiteException ex){
            ex.printStackTrace();
        }

    }

    public void updateEvento(Evento eventoAtualizado){
        try(SQLiteDatabase db = this.getWritableDatabase()){

                ContentValues valores = new ContentValues();
                valores.put("nome", eventoAtualizado.getNome());
                valores.put("valor", eventoAtualizado.getValor());
                valores.put("imagem", eventoAtualizado.getCaminhoFoto());
                valores.put("dataocorreu", eventoAtualizado.getOcorreu().getTime());
                valores.put("datavalida", eventoAtualizado.getValida().getTime());

                db.update("evento", valores, "id=?", new String[]{eventoAtualizado.getId()+""});



        }catch (SQLiteException ex){
            System.out.print("erro na atualização do evento");
            ex.printStackTrace();
        }

    }

    public Evento buscaEventoId(int idEvento) {
        String sql = "SELECT * evento WHERE id =" + idEvento;
        Evento resultado = null;
        try (SQLiteDatabase db = this.getWritableDatabase()) {

            //extraindo informações e criando o objeto
            Cursor tupla = db.rawQuery(sql, null);
            if(tupla.moveToFirst()){
                String nome = tupla.getString(1);
                double valor = tupla.getDouble(2);
                if(valor <0){
                    valor *= -1;
                }
                String urlFoto = tupla.getString(3);
                Date dataocorreu = new Date(tupla.getLong(4));
                Date datacadastro = new Date(tupla.getLong(5));
                Date datavalida = new Date(tupla.getLong(6));

                resultado = new Evento(idEvento, nome,urlFoto, valor, datacadastro,datavalida, dataocorreu );
            }



        }catch (SQLiteException ex){
            //mensagem de bug
            System.out.print("ocorreu erro na consulta ao banco por id!");
            ex.printStackTrace();

        }
        return resultado;

    }

    public ArrayList<Evento> buscaEventos(int op, Calendar data){

        ArrayList <Evento> resultado = new ArrayList<>();

        //DIA 1 DO MES
        Calendar dia1 = Calendar.getInstance();
        dia1.setTime(data.getTime());
        dia1.set(Calendar.DAY_OF_MONTH, 1);
        dia1.set(Calendar.HOUR, -12);
        dia1.set(Calendar.MINUTE,0);
        dia1.set(Calendar.SECOND, 0);

        //ULTIMO DIA DO MES
        Calendar dia2 = Calendar.getInstance();
        dia2.setTime(data.getTime());
        dia2.set(Calendar.DAY_OF_MONTH, dia2.getActualMaximum(Calendar.DAY_OF_MONTH));
        dia2.set(Calendar.HOUR, 11);
        dia2.set(Calendar.MINUTE, 59);
        dia2.set(Calendar.SECOND, 59);

        String sql = "SELECT * from evento WHERE ((datavalida <="+dia2.getTime().getTime()+" AND datavalida >="+
                dia1.getTime().getTime()+") OR (dataocorreu <="+dia2.getTime()+
                " AND datavalida >="+dia1.getTime().getTime()+" ))";

        sql +="AND valor ";


        if(op==0){
            sql+=">= 0";
        }else{
            sql+="< 0";
        }

        try(SQLiteDatabase db = this.getWritableDatabase()){

            Cursor tuplas = db.rawQuery(sql, null);
            if(tuplas.moveToFirst()) {
                do{
                    int id = tuplas.getInt(0);
                    String nome = tuplas.getString(1);
                    double valor = tuplas.getDouble(2);
                    if(valor <0){
                        valor *= -1;
                    }
                    String urlFoto = tuplas.getString(3);
                    Date dataocorreu = new Date(tuplas.getLong(4));
                    Date datacadastro = new Date(tuplas.getLong(5));
                    Date datavalida = new Date(tuplas.getLong(6));

                    Evento temporario = new Evento((long)id, nome,urlFoto, valor, datacadastro,datavalida, dataocorreu);
                    resultado.add(temporario);

                }while(tuplas.moveToNext());
            }


        }catch (SQLiteException ex){
            System.out.print("ocorreu erro na consulta ao banco!");
            ex.printStackTrace();
        }

        return resultado;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
