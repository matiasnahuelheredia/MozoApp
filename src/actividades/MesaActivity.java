package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import representacion.Mesa;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleytesting.R;
import com.example.volleytesting.R.id;
import com.example.volleytesting.R.layout;
import com.example.volleytesting.R.menu;

import adaptadores.MesaAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MesaActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private RequestQueue mRequestQueue;
	private ArrayList<Mesa> arrNews;
	private MesaAdapter va;
	private ProgressDialog pd;
	private String usuario;
	private String password;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mesa);
		arrNews = new ArrayList<Mesa>();
		va = new MesaAdapter(getApplicationContext(), arrNews);
		lstView = (ListView) findViewById(R.id.lista_mesas);
		lstView.setAdapter(va);
		mRequestQueue = Volley.newRequestQueue(this);
		url = getIntent().getExtras().getString("url");
		usuario = getIntent().getExtras().getString("user");
		password = getIntent().getExtras().getString("password");
		pd = ProgressDialog.show(this, "Aguarde por favor...",
				"Aguarde por favor...");
		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.i(TAG, response.toString());
						parseJSON(response);
						va.notifyDataSetChanged();
						pd.dismiss();
						;
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(TAG, error.getMessage());
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
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
		mRequestQueue.add(jr);
		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Mesa unaMesa = (Mesa) parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putParcelable("mesa", unaMesa);
				bundle.putString("user", usuario);
				bundle.putString("password", password);
				Intent intent = new Intent(MesaActivity.this,
						PedidosActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

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
				arrNews.add(unaMesa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.mesa, menu);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), "Esta Saliendo de la APP", Toast.LENGTH_LONG).show();		
		super.onDestroy();
	}
	
	
}
