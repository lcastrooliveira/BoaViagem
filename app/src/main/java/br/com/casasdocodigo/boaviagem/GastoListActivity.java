package br.com.casasdocodigo.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lucas on 6/09/2015.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private String dataAnterior = "";

    private List<Map<String,Object>> gastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private List<Map<String,Object>> listarGastos() {
        gastos = new ArrayList<Map<String,Object>>();
        Map<String, Object> item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor", "R$ 260,00");
        item.put("categoria", R.color.categoria_hospedagem);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("data", "05/02/2012");
        item2.put("descricao", "Lembracinha");
        item2.put("valor", "R$ 2210,00");
        item2.put("categoria", R.color.categoria_outros);

        gastos.add(item);
        gastos.add(item2);

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
