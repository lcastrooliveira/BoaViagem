package br.com.casasdocodigo.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.casasdocodigo.boaviagem.dao.GastoDAO;
import br.com.casasdocodigo.boaviagem.domain.Gasto;

/**
 * Created by Lucas on 6/09/2015.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private String dataAnterior = "";

    private String viagemId;

    private List<Map<String,Object>> gastos;

    private GastoDAO gastoDAO;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gastoDAO = new GastoDAO(this);
        viagemId = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        Log.i(GastoListActivity.class.getName(),viagemId);
        String[] de = {
                "data","descricao","valor","categoria"
        };
        int[] para = {R.id.data, R.id.descricao,R.id.valor,R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this,listarGastos(),R.layout.lista_gasto,de,para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        registerForContextMenu(getListView());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Map<String,Object> map = gastos.get(i);
        String descricao = (String)map.get("descricao");
        String mensagem = "Gasto selecionado: "+descricao;
        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
    }

    private int categoriaColor(Gasto gasto) {
        switch (gasto.getCategoria()) {
            case Constantes.HOSPEDAGEM:
                return R.color.categoria_hospedagem;
            case Constantes.ALIMENTACAO:
                return R.color.categoria_alimentacao;
            case Constantes.TRANSPORTE:
                return R.color.categoria_transporte;
            default:
                return R.color.categoria_outros;
        }
    }

    private List<Map<String,Object>> listarGastos() {
        gastos = new ArrayList<Map<String,Object>>();
        List<Gasto> gastosBanco = gastoDAO.listarGastos(new Long(viagemId));
        for(Gasto gasto : gastosBanco) {
            Map<String, Object> item = new HashMap<>();
            item.put("data", dateFormat.format(gasto.getData()));
            item.put("descricao", gasto.getDescricao());
            item.put("valor", String.valueOf(gasto.getValor()));
            item.put("categoria", categoriaColor(gasto));
            gastos.add(item);
        }
        return gastos;
    }

    private class GastoViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data,String textRepresentation) {
            if(view.getId() == R.id.data) {
                if(!dataAnterior.equals(data)) {
                    TextView textView = (TextView)view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if(view.getId() == R.id.categoria) {
                Integer id = (Integer)data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.remover) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            //remover do bd
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
