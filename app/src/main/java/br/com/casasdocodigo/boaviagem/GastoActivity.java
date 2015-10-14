package br.com.casasdocodigo.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import br.com.casasdocodigo.boaviagem.dao.GastoDAO;
import br.com.casasdocodigo.boaviagem.domain.Gasto;
import br.com.casasdocodigo.boaviagem.domain.Viagem;

/**
 * Created by Lucas on 30/08/2015.
 */
public class GastoActivity extends Activity {

    private int ano, mes, dia;
    private Button dataGasto;
    private Spinner categoria;
    private String viagemId;
    private GastoDAO gastoDAO;
    private EditText valor,descricao,local;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);
        viagemId = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        gastoDAO = new GastoDAO(this);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGasto = (Button)findViewById(R.id.data);
        dataGasto.setText(dia + "/" + (mes + 1) + "/" + ano);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categoria_gasto,android.R.layout.simple_spinner_item);
        categoria = (Spinner)findViewById(R.id.categoria);
        categoria.setAdapter(adapter);

        valor = (EditText)this.findViewById(R.id.valor);
        descricao = (EditText)this.findViewById(R.id.descricao);
        local = (EditText)this.findViewById(R.id.local);

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataGasto.setText(dia+"/"+(mes+1)+"/"+ano);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if(R.id.data == id) {
            return new DatePickerDialog(this,listener,ano,mes,dia);
        }
        return null;
    }

    public void registrarGasto(View view) {

        Gasto gasto = new Gasto();
        gasto.setDescricao(descricao.getText().toString());
        gasto.setLocal(local.getText().toString());
        gasto.setValor(Double.parseDouble(valor.getText().toString()));
        gasto.setViagemId(Integer.parseInt(viagemId));
        gasto.setCategoria(categoria.getSelectedItem().toString());

        Calendar data = Calendar.getInstance();
        data.set(Calendar.YEAR,ano);
        data.set(Calendar.MONTH,mes);
        data.set(Calendar.DAY_OF_MONTH, dia);
        gasto.setData(data.getTime());

        gasto = gastoDAO.inserirGasto(gasto);
        if(gasto.getId() != -1) {
            Toast.makeText(this,"Gasto Salvo com Sucesso!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Erro ao salvar",Toast.LENGTH_SHORT).show();
        }
    }



    public void selecionarData(View view) {
        showDialog(view.getId());
    }
}
