package br.com.casasdocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

public class BoaViagemActivity extends Activity {

    private EditText usuario;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = (EditText)findViewById(R.id.usuario);
        senha = (EditText)findViewById(R.id.senha);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void entrarOnClick(View v) {
        final String usuarioInformado = usuario.getText().toString();
        final String senhaInformada = senha.getText().toString();
        if("leitor".equals(usuarioInformado) && "123".equals(senhaInformada)) {
            //vai para activity
            startActivity(new Intent(this,DashboardActivity.class));
        } else {
            //mostra mensagem de erro
            final String menssagemErro = getString(R.string.erro_autenticacao);
            Toast toast = Toast.makeText(this,menssagemErro,Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
