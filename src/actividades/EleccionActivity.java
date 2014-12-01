package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import representacion.Eleccion;
import representacion.Pedido;
import representacion.Producto;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleytesting.R;
import adaptadores.EleccionAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EleccionActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private ProgressDialog pd;
	private String url;
	private ArrayList<Eleccion> arregloElecciones;
	private EleccionAdapter eleccionAdapter;
	private String urlInvoke;
	private String tipo;
	private Producto unProducto;
	private String urlPedido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eleccion);
		arregloElecciones = new ArrayList<Eleccion>();
		eleccionAdapter = new EleccionAdapter(getApplicationContext(),
				arregloElecciones);
		lstView = (ListView) findViewById(R.id.listEleccion);
		lstView.setAdapter(eleccionAdapter);
		url = getIntent().getExtras().getString("url");
		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Eleccion unaEleccion = (Eleccion) parent.getAdapter().getItem(
						position);
				JSONObject postItem = itemToAdd(unaEleccion.getUrl());
				unProducto = new Producto();
				unProducto.setTitle(unaEleccion.getTitle());
				unProducto.setUrlDetalle(unaEleccion.getUrl());
				JsonObjectRequest jsonPost = new JsonObjectRequest(Request.Method.POST, urlInvoke, postItem, 
						new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println("Entrooo");
						
						
						
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				}){

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						return createBasicAuthHeader(usuario, password);
					}
					
					Map<String, String> createBasicAuthHeader(String username,
							String password) {
						Map<String, String> headerMap = new HashMap<String, String>();

						String credentials = username + ":" + password;
						String encodedCredentials = Base64.encodeToString(
								credentials.getBytes(), Base64.NO_WRAP);
						headerMap.put("Authorization", "Basic " + encodedCredentials);				
						return headerMap;
					}
					
				};
			Intent intentRegreso = new Intent();
			intentRegreso.putExtra("productoNew", unProducto);
			intentRegreso.putExtra("urlPedido", urlPedido);
			setResult(RESULT_OK, intentRegreso);
			finish();
			}
						
		});		
		
	}
	

	private JSONObject itemToAdd(String unaURL) {

		JSONObject item = null;
		try {
			JSONObject href = new JSONObject();
			href.put("href", unaURL);
			JSONObject value = new JSONObject();
			value.put("value", href);
			item = new JSONObject();
			item.put(tipo, value);
			System.out.println(item.toString());
			return item;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return item;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eleccion, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void parseJSONChoices(JSONObject json) {
		try {
			JSONArray links = json.getJSONArray("links");
			urlInvoke = links.getJSONObject(2).getString("href").toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
			if (temp.toString().contains("bebida1")) {
				item = parameters.getJSONObject("bebida1");
				tipo = "bebida1";
			}
			if (temp.toString().contains("platoEntrada1")) {
				item = parameters.getJSONObject("platoEntrada1");
				tipo = "platoEntrada1";
			}
			if (temp.toString().contains("platoPrincipal1")) {
				item = parameters.getJSONObject("platoPrincipal1");
				tipo = "platoPrincipal1";
			}
			if (temp.toString().contains("postre1")) {
				item = parameters.getJSONObject("postre1");
				tipo = "postre1";
			}
			if (temp.toString().contains("guarnición1")) {
				item = parameters.getJSONObject("guarnición1");
				tipo = "guarnici�";
			}
			if (temp.toString().contains("menu1")) {
				item = parameters.getJSONObject("menu1");
				tipo = "menu1";
			}

			JSONArray choices = item.getJSONArray("choices");
			for (int i = 0; i < choices.length(); i++) {
				JSONObject unaOpcion = choices.getJSONObject(i);
				Eleccion unaEleccion = new Eleccion();
				unaEleccion.setTitle(unaOpcion.optString("title"));
				unaEleccion.setUrl(unaOpcion.optString("href"));
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
