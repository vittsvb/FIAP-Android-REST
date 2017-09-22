package br.com.fiap.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.webservice.model.Item;

public class ListarActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        listView = (ListView) findViewById(R.id.list_itens);

        //Chama o webservice
        ListaTask task = new ListaTask();
        task.execute();
    }

    private class ListaTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(
                        "http://localhost:8080/MercadoFiap/rest/mercado/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String linha;
                    StringBuilder builder = new StringBuilder();
                    while ((linha = reader.readLine()) != null) {
                        builder.append(linha);
                    }
                    connection.disconnect();
                    return builder.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                //Ler o JSON Array
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray jsonArray = json.getJSONArray("itens");

                    List<Item> lista = new ArrayList<Item>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int codigo = item.getInt("codigo");
                        String desc = item.getString("descricao");
                        int qtd = item.getInt("quantidade");
                        double preco = item.getDouble("preco");
                        Item item1 = new Item(codigo, desc, qtd, preco);
                        lista.add(item1);
                    }

                    //Exibir a lista de itens na tela
                    ListAdapter adapter = new ArrayAdapter(
                            ListarActivity.this, android.R.layout.simple_list_item_1,
                            lista);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ListarActivity.this,
                        "Erro", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
