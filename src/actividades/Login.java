package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import representacion.Mesa;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.Volley;
import com.example.volleytesting.R;
import com.example.volleytesting.R.id;
import com.example.volleytesting.R.layout;
import com.example.volleytesting.R.menu;

import forTesting.Auxiliar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	String usuario;
	String password;
	String url = ":8080/resto-webapp/restful/services/dom.mesa.MesaServicio/actions/listarMesasAsignadas/invoke";
	ArrayList<Mesa> mesasAsignadas = new ArrayList<Mesa>();
	EditText txtIpserver;
	EditText txtUsuario;
	EditText txtPassword;
	private String TAG = this.getClass().getSimpleName();
	static RequestQueue solicitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtIpserver = (EditText) findViewById(R.id.etxtIpServer);
		txtUsuario = (EditText) findViewById(R.id.etxtUsuario);
		txtPassword = (EditText) findViewById(R.id.etxtPassword);
		Button btnEntrar = (Button) findViewById(R.id.btnEntrar);
		solicitud = Volley.newRequestQueue(this);
		btnEntrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				url = txtIpserver.getText().toString().concat(url);
				usuario = txtUsuario.getText().toString();
				password = txtPassword.getText().toString();
				Bundle parametros = new Bundle();
				parametros.putString("url", url);
				parametros.putString("user", usuario);
				parametros.putString("password", password);
				Intent intent = new Intent(Login.this,
						MesaActivity.class);
				intent.putExtras(parametros);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
