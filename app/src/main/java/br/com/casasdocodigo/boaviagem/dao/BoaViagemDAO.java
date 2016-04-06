package br.com.casasdocodigo.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.casasdocodigo.boaviagem.Constantes;
import br.com.casasdocodigo.boaviagem.DatabaseHelper;
import br.com.casasdocodigo.boaviagem.R;
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
            Viagem viagem = cursorToBeanViagem(cursor);
            viagens.add(viagem);
        }
        cursor.close();
        return viagens;
    }

    private ContentValues setContentValues(Viagem viagem) {
        ContentValues values = new ContentValues();
        values.put("destino",viagem.getDestino());
        values.put("data_chegada",viagem.getDataChegada().getTime());
        values.put("data_saida",viagem.getDataSaida().getTime());
        values.put("orcamento",viagem.getOrcamento());
        values.put("quantidade_pessoas",viagem.getQuantidadePessoas());
        values.put("tipo_viagem", viagem.getTipoViagem());

        return values;
    }

    public Viagem inserirViagem(Viagem viagem) {
        ContentValues values = setContentValues(viagem);
        long id = getDb().insert(DatabaseHelper.Viagem.TABELA, null, values);
        if(id != -1) {
            viagem.setId(id);
            return viagem;
        } else {
            return null;
        }
    }

    public Viagem atualizarViagem(Viagem viagem) {
        ContentValues values = setContentValues(viagem);
        long id = getDb().update("viagem", values, "_id = ?", new String[]{String.valueOf(viagem.getId())});
        if(id != -1) {
            return viagem;
        } else {
            return null;
        }
    }

    public int removerViagem(String id) {
        String where [] = new String[] {String.valueOf(id)};
        getDb().delete("gasto","viagem_id = ?", where);
        int success = getDb().delete("viagem", "_id = ?", where);
        return success;
    }

    public Viagem cursorToBeanViagem(final Cursor cursor) {
        final long id = cursor.getLong(0);
        final String destino = cursor.getString(1);
        final long dataChegada = cursor.getLong(2);
        final long dataSaida = cursor.getLong(3);
        final int tipoViagem = cursor.getInt(4);
        final double orcamento = cursor.getDouble(5);
        final int quantidadePessoas = cursor.getInt(6);
        return new Viagem(id, destino, tipoViagem, new Date(dataChegada), new Date(dataSaida), orcamento, quantidadePessoas);
    }

    public Viagem findOne(final Long id) {
        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,DatabaseHelper.Viagem.COLUNAS,DatabaseHelper.Viagem._ID+" = "+id,null,null,null,null);
        cursor.moveToFirst();
        final Viagem viagem = cursorToBeanViagem(cursor);
        return viagem;
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
