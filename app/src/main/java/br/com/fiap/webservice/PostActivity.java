package br.com.fiap.webservice;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostActivity extends AppCompatActivity {

    private EditText descricao;
    private EditText preco;
    private EditText quantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        descricao = (EditText) findViewById(R.id.edt_desc);
        preco = (EditText) findViewById(R.id.edt_preco);
        quantidade = (EditText) findViewById(R.id.edt_qtd);
    }

    public void cadastrar(View v){
        CadastroItemTask task = new CadastroItemTask();

        task.execute(descricao.getText().toString(), quantidade.getText().toString(), preco.getText().toString());

    }

    private class CadastroItemTask extends AsyncTask<Object, Object, Integer> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(PostActivity.this, "Aguarde...", "Enviando dados para o servidor");
        }

        @Override
        protected Integer doInBackground(Object... params) {

            try {
                //Criar a URL (localhost é 10.0.2.2)
                URL url = new URL("http://10.20.63.61:8080/MercadoFiap/rest/mercado/");
                //Obter uma conexão
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Configurar a requisição
                //Método HTTP -> GET (Busca)
                connection.setRequestMethod("POST");
                //Tipo de dado que será devolvido pelo webservice (JSON)
                connection.setRequestProperty("Content-Type","application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("descricao").value(params[0]);
                json.key("quantidade").value(params[1]);
                json.key("preco").value(params[2]);
                json.endObject();

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(json.toString());
                stream.close();

                return connection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            progress.dismiss();
            if(s == 201){
                Toast.makeText(PostActivity.this, "Cadastro Realizado", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(PostActivity.this, "Erro ao realizar cadastro", Toast.LENGTH_LONG).show();

            }
        }
    }
}
