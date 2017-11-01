package com.example.joovictorfirmino.stressless;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    EditText editNome, editEmail2, editSenha2;
    Button btnVoltar, btnRegistrar;
    String url = "";
    String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        editNome = (EditText)findViewById(R.id.editNome);
        editEmail2 = (EditText)findViewById(R.id.editEmail2);
        editSenha2 = (EditText)findViewById(R.id.editSenha2);
        btnVoltar = (Button)findViewById(R.id.btnVoltar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()){

                    String nome = editNome.getText().toString();
                    String email = editEmail2.getText().toString();
                    String senha = editSenha2.getText().toString();

                    if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        url = "https://joaogarciafirmino.000webhostapp.com/registrar.php";

                        parametros = "nome=" + nome + "&email=" + email + "&senha=" + senha;

                        new SolicitaDados().execute(url);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma conexão foi encontrada", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class SolicitaDados extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls ){

            return Conexao.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            if (resultado.contains("email_erro")){
                Toast.makeText(getApplicationContext(), "Email já cadastrado!", Toast.LENGTH_LONG).show();
            }else if (resultado.contains("registro_ok")){
                Toast.makeText(getApplicationContext(), "Cadastro realizado!", Toast.LENGTH_LONG).show();
                Intent abreTelaInicial = new Intent(TelaCadastro.this, TelaLogin.class);
                startActivity(abreTelaInicial);
            } else {
                Toast.makeText(getApplicationContext(), "Email Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
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
