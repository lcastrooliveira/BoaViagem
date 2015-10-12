package br.com.casasdocodigo.boaviagem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.casasdocodigo.boaviagem.DatabaseHelper;
import br.com.casasdocodigo.boaviagem.domain.Viagem;

/**
 * Created by Lucas on 12/10/2015.
 */
public class BoaViagemDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public List<Viagem> listarViagens() {
        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,DatabaseHelper.Viagem.COLUNAS,null,null,null,null,null);
        List<Viagem> viagens = new ArrayList<>();
        while(cursor.moveToNext()) {
            Viagem viagem = criarViagem(cursor);
            viagens.add(viagem);
        }
        cursor.close();
        return viagens;
    }

    public Viagem criarViagem(Cursor cursor) {
        long id = cursor.getLong(0);
        int tipoViagem = cursor.getInt(1);
        String destino = cursor.getString(2);
        long dataChegada = cursor.getLong(3);
        long dataSaida = cursor.getLong(4);
        double orcamento = cursor.getDouble(5);
        int quantidadePessoas = cursor.getInt(6);
        return new Viagem(id, destino, tipoViagem, new Date(dataChegada), new Date(dataSaida), orcamento, quantidadePessoas);
    }

    public BoaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb() {
        if(db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }

}
