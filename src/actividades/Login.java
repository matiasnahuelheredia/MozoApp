package actividades;

import java.util.ArrayList;
import java.util.Map;
import networking.Conexion;
import org.json.JSONArray;
import org.json.JSONObject;
import representacion.Mesa;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.volleytesting.R;
import adaptadores.MesaAdapter;
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
import android.widget.Toast;

public class Login extends Activity {

	String usuario;
	String password;
	String url = ":8080/resto-webapp/restful/services/dom.mesa.MesaServicio/actions/listarMesasAsignadas/invoke";
	ArrayList<Mesa> mesasAsignadas = new ArrayList<Mesa>();
	EditText txtIpserver;
	EditText txtUsuario;
	EditText txtPassword;
	private String TAG = this.getClass().getSimpleName();
	MesaAdapter adaptadorMesa;
	ArrayList<Mesa> arregloMesas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txtIpserver = (EditText) findViewById(R.id.etxtIpServer);
		txtUsuario = (EditText) findViewById(R.id.etxtUsuario);
		txtPassword = (EditText) findViewById(R.id.etxtPassword);
		Button btnEntrar = (Button) findViewById(R.id.btnEntrar);
		final RequestQueue colaSolicitud = Conexion.getInstance(
				getApplicationContext()).getRequestQueue();
		arregloMesas = new ArrayList<Mesa>();
		btnEntrar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				url = txtIpserver.getText().toString().concat(url);
				usuario = txtUsuario.getText().toString();
				password = txtPassword.getText().toString();
				Conexion.setUser(usuario);
				Conexion.setPassword(password);
				final ProgressDialog pd = ProgressDialog.show(Login.this,
						"Aguarde por favor...", "Aguarde por favor...");
				JsonObjectRequest solicitudMesas = new JsonObjectRequest(
						Request.Method.GET, url, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {

								Log.i(TAG, response.toString());
								System.out.println("Entrooooo");
								parseJSON(response);
								Bundle parametros = new Bundle();
								parametros.putParcelableArrayList(
										"listaDeMesas", arregloMesas);
								Intent intent = new Intent(Login.this,
										MesaActivity.class);
								intent.putExtras(parametros);
								startActivity(intent);
								pd.dismiss();

							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								System.out.println("NOoooooo Entrooooo");
								pd.dismiss();
								Toast.makeText(getApplicationContext(),
										"Combinacion User/Password Invalida",
										Toast.LENGTH_LONG).show();
								
								Log.i(TAG, error.getMessage());
							}
						}) {

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						return Conexion.createBasicAuthHeader();
					}

				};
				colaSolicitud.add(solicitudMesas);

			}

		});

	}

	private void parseJSON(JSONObject json) {
		try {

			JSONObject result = json.getJSONObject("result");
			JSONArray value = result.getJSONArray("value");
			for (int i = 0; i < value.length(); i++) {
				JSONObject mesa = value.getJSONObject(i);
				Mesa unaMesa = new Mesa();
				unaMesa.setTitle(mesa.optString("title"));
				unaMesa.setUrlDetalle(mesa.optString("href"));
				arregloMesas.add(unaMesa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
