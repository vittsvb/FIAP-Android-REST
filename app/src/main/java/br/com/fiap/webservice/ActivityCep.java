package br.com.fiap.webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityCep extends AppCompatActivity {
    private EditText cep;
    private EditText edt_logradouro;
    private EditText edt_complemento;
    private EditText edt_bairro;
    private EditText edt_localidade;
    private EditText edt_uf;
    private EditText edt_ibge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cep);

        cep = (EditText) findViewById(R.id.edt_cep);
        edt_logradouro = (EditText) findViewById(R.id.logradouro);
        edt_complemento = (EditText) findViewById(R.id.complemento);
        edt_bairro = (EditText) findViewById(R.id.bairro);
        edt_localidade = (EditText) findViewById(R.id.localidade);
        edt_uf = (EditText) findViewById(R.id.uf);
        edt_ibge = (EditText) findViewById(R.id.ibge);

    }

    public void buscarCEP(View view){
        //Instanciar a classe Task
        BuscaCepTask task = new BuscaCepTask();
        //Chamar o método execute
        task.execute(cep.getText().toString());
    }

    private class BuscaCepTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                try {
                    JSONObject json = new JSONObject(s);
                    String bairro = json.getString("bairro");
                    String cidade = json.getString("cidade");
                    String logradouro = json.getString("logradouro");
                    String estado = json.getString("estado");
                    String codigo_ibge = json.getString("codigo_ibge");

                    edt_logradouro.setText(logradouro);
                    edt_complemento.setText(estado);
                    edt_bairro.setText(bairro);
                    edt_localidade.setText(cidade);
                    edt_uf.setText(estado);
                    edt_ibge.setText(codigo_ibge);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(ActivityCep.this, "Erro ao realizar consulta", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //Criar a URL (localhost é 10.0.2.2)
                URL url = new URL("http://api.postmon.com.br/v1/cep/"+params[0]);
                //Obter uma conexão
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Configurar a requisição
                //Método HTTP -> GET (Busca)
                connection.setRequestMethod("GET");
                //Tipo de dado que será devolvido pelo webservice (JSON)
                connection.setRequestProperty("Accept","application/json");

                //Status Code HTTP 200 OK ->Sucesso!
                if (connection.getResponseCode() == 200){
                    //Ler a resposta enviada pelo webservice
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder json = new StringBuilder();
                    String linha;
                    //Ler todas as linhas retornadas pelo ws
                    while ((linha = reader.readLine()) != null){
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

