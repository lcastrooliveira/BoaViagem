package br.com.casasdocodigo.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lucas on 30/08/2015.
 */
public class ViagemActivity extends Activity {

    private String id;
    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;
    private Button dataChegadaButton,dataSaidaButton;
    private int ano, mes, dia;
    private Calendar dataChegada,dataSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        Calendar calendar = Calendar.getInstance();
        dataChegada = Calendar.getInstance();
        dataSaida = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataChegadaButton = (Button)findViewById(R.id.dataChegada);
        dataSaidaButton = (Button)findViewById(R.id.dataSaida);

        destino = (EditText)findViewById(R.id.destino);
        orcamento = (EditText)findViewById(R.id.orcamento);
        quantidadePessoas = (EditText)findViewById(R.id.quantidadePessoas);
        radioGroup = (RadioGroup)findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if(id != null)
            prepararEdicao();

    }

    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                "data_saida, quantidade_pessoas, orcamento " +
                "FROM viagem WHERE _id = ?", new String[] {id});
        cursor.moveToFirst();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(cursor.getInt(0) == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }

        destino.setText(cursor.getString(1));
        dataChegada.setTimeInMillis(cursor.getLong(2));
        dataSaida.setTimeInMillis(cursor.getLong(3));
        dataChegadaButton.setText(dateFormat.format(dataChegada.getTime()));
        dataSaidaButton.setText(dateFormat.format(dataSaida.getTime()));
        quantidadePessoas.setText(cursor.getString(4));
        orcamento.setText(cursor.getString(5));
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.novo_gasto:
                startActivity(new Intent(this,GastoActivity.class));
                return true;
            case R.id.remover:
                //remover do bd
                return true;
            default:
                return super.onMenuItemSelected(featureId,item);
        }
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            dataChegadaButton.setText(dia+"/"+(mes+1)+"/"+ano);
            dataChegada.set(Calendar.YEAR, ano);
            dataChegada.set(Calendar.MONTH, mes);
            dataChegada.set(Calendar.DAY_OF_MONTH,dia);
        }
    };

    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;

            dataSaidaButton.setText(dia+"/"+(mes+1)+"/"+ano);
            dataSaida.set(Calendar.YEAR, ano);
            dataSaida.set(Calendar.MONTH, mes);
            dataSaida.set(Calendar.DAY_OF_MONTH, dia);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.dataChegada == id) {
            return new DatePickerDialog(this,listener,ano,mes,dia);
        } else if(R.id.dataSaida == id) {
            return new DatePickerDialog(this,listener2,ano,mes,dia);
        }
        return null;
    }



    public void selecionarData(View view) {
        showDialog(view.getId());
    }



    public void salvarViagem(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino",destino.getText().toString());
        values.put("data_chegada",dataChegada.getTimeInMillis());
        values.put("data_saida",dataSaida.getTimeInMillis());
        values.put("orcamento",orcamento.getText().toString());
        values.put("quantidade_pessoas",quantidadePessoas.getText().toString());

        int tipo = radioGroup.getCheckedRadioButtonId();

        if(tipo == R.id.lazer) {
            values.put("tipo_viagem",Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem",Constantes.VIAGEM_NEGOCIOS);
        }
        long resultado;
        if(id == null){
            resultado = db.insert("viagem", null, values);
        } else {
            resultado = db.update("viagem", values, "_id = ?", new String[]{ id });
        }
        if(resultado != -1) {
            Toast.makeText(this,getString(R.string.registro_salvo),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,getString(R.string.erro_salvar),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
