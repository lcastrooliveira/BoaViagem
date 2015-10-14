package br.com.casasdocodigo.boaviagem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.casasdocodigo.boaviagem.DatabaseHelper;
import br.com.casasdocodigo.boaviagem.domain.Gasto;

/**
 * Created by Lucas on 13/10/2015.
 */
public class GastoDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public GastoDAO(Context context) {
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

    public List<Gasto> listarGastos(Long id) {
        Cursor cursor = getDb().query(DatabaseHelper.Gasto.TABELA, DatabaseHelper.Gasto.COLUNAS,DatabaseHelper.Gasto.VIAGEM_ID+" = "+id,null,null,null,null);
        List<Gasto> gastos = new ArrayList<>();
        while(cursor.moveToNext()) {
            Gasto gasto = cursorToBeanGasto(cursor);
            gastos.add(gasto);
        }
        cursor.close();
        return gastos;
    }

    private Gasto cursorToBeanGasto(Cursor cursor) {

        Integer viagemId = cursor.getInt(0);
        String categoria = cursor.getString(1);
        Date data = new Date(cursor.getLong(2));
        String descricao = cursor.getString(3);
        Double valor = cursor.getDouble(4);
        String local = cursor.getString(5);
        return new Gasto(null,data,categoria,descricao,valor,local,viagemId);

    }

    public Gasto inserirGasto(Gasto gasto) {
        ContentValues values = setContentValues(gasto);
        long id = getDb().insert(DatabaseHelper.Gasto.TABELA, null, values);
        if(id != -1) {
            gasto.setId(id);
            return gasto;
        } else {
            return null;
        }
    }

    private ContentValues setContentValues(Gasto gasto) {
        ContentValues values = new ContentValues();
        values.put("viagem_id",gasto.getViagemId());
        values.put("categoria",gasto.getCategoria());
        values.put("data",gasto.getData().getTime());
        values.put("descricao",gasto.getDescricao());
        values.put("valor",gasto.getValor());
        values.put("local",gasto.getLocal());
        return values;
    }

}
