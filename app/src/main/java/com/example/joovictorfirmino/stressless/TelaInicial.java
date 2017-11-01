package com.example.joovictorfirmino.stressless;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaInicial extends AppCompatActivity {

    EditText editBrinco, editFr, editTr;
    Button btnVoltar, btnSalvar;
    String url = "";
    String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        editBrinco = (EditText)findViewById(R.id.editBrinco);
        editFr = (EditText)findViewById(R.id.editFr);
        editTr = (EditText)findViewById(R.id.editTr);
        btnVoltar = (Button)findViewById(R.id.btnVoltar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()){

                    String brinco = editBrinco.getText().toString();
                    String fr = editFr.getText().toString();
                    String tr = editTr.getText().toString();

                    if(brinco.isEmpty() || fr.isEmpty() || tr.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        url = "https://joaogarciafirmino.000webhostapp.com/dados_sensores.php";

                        parametros = "brinco=" + brinco + "&fr=" + fr + "&tr=" + tr;

                        new TelaInicial.SolicitaDados().execute(url);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi encontrada", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls ){

            return Conexao.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            if (resultado.contains("brinco_erro")){
                Toast.makeText(getApplicationContext(), "Brinco já cadastrado!", Toast.LENGTH_LONG).show();
            }else if (resultado.contains("registro_ok")){
                Toast.makeText(getApplicationContext(), "Cadastro realizado!", Toast.LENGTH_LONG).show();
                Intent abreTelaInicial = new Intent(TelaInicial.this, TelaDados.class);
                startActivity(abreTelaInicial);
            } else {
                Toast.makeText(getApplicationContext(), "Dado Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
                finish();

            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}