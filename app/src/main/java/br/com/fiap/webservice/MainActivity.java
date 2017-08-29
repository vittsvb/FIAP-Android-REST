package br.com.fiap.webservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView resposta;
    private EditText codigo;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cadastar) {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_cep) {
            Intent intent = new Intent(this, ActivityCep.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_listar) {
            //Navega para a tela de listagem
            Intent intent = new Intent(this, ListarActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resposta = (TextView) findViewById(R.id.txt_resposta);
        codigo = (EditText) findViewById(R.id.edt_codigo);
    }

    public void buscar(View v) {
        //Instanciar a classe Task
        BuscaTask task = new BuscaTask();
        //Chamar o método execute
        task.execute(Integer.parseInt(codigo.getText().toString()));
    }

    private class BuscaTask extends AsyncTask<Integer, Void, String> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Aguarde... ", "Buscando dados");
        }

        //Método executado após o método doInBackground
        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            //Recuperar os valores do JSON
            if (s != null) {
                try {
                    JSONObject json = new JSONObject(s);
                    long codigo = json.getLong("codigo");
                    String descricao = json.getString("descricao");
                    int quantidade = json.getInt("quantidade");
                    double preco = json.getDouble("preco");

                    resposta.setText("Código: " + codigo + "\nDescrição: " + descricao + "\nQuantidade: " + quantidade + "\nPreço: R$ " + preco);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(MainActivity.this, "Erro ao realizar consulta", Toast.LENGTH_LONG).show();

            }
//            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
        }

        //Método que executa a tarefa "pesada"
        @Override
        protected String doInBackground(Integer... params) {
            //Chamar o webservice restful
            try {
                //Criar a URL (localhost é 10.0.2.2)
                URL url = new URL("http://10.20.63.61:8080/MercadoFiap/rest/mercado/" + params[0]);
                //Obter uma conexão
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Configurar a requisição
                //Método HTTP -> GET (Busca)
                connection.setRequestMethod("GET");
                //Tipo de dado que será devolvido pelo webservice (JSON)
                connection.setRequestProperty("Accept", "application/json");

                //Status Code HTTP 200 OK ->Sucesso!
                if (connection.getResponseCode() == 200) {
                    //Ler a resposta enviada pelo webservice
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    StringBuilder json = new StringBuilder();
                    String linha;
                    //Ler todas as linhas retornadas pelo ws
                    while ((linha = reader.readLine()) != null) {
                        //Adiciona cada linha no builder
                        json.append(linha);
                    }
                    connection.disconnect();
                    return json.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
