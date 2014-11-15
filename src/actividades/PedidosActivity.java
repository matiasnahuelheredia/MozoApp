package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import representacion.Mesa;
import representacion.Pedido;

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

import adaptadores.PedidoAdapter;
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

public class PedidosActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private RequestQueue mRequestQueue;
	private ArrayList<Pedido> arrNews;
	private PedidoAdapter va;
	private ProgressDialog pd;
	private String usuario;
	private String password;
	private String url;
	private String urlInvoke;
	private Mesa unaMesa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedidos);
		arrNews = new ArrayList<Pedido>();
		va = new PedidoAdapter(getApplicationContext(), arrNews);
		lstView = (ListView) findViewById(R.id.listPedidos);
		lstView.setAdapter(va);
		mRequestQueue = Volley.newRequestQueue(this);
		unaMesa = getIntent().getExtras().getParcelable("mesa");
		url = unaMesa.getUrlDetalle();
		usuario = getIntent().getExtras().getString("user");
		password = getIntent().getExtras().getString("password");
		pd = ProgressDialog.show(this, "Aguarde por favor...",
				"Aguarde por favor...");
		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						System.out.println("Entroooooooooooooo");
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
						System.out.println("Nooooooo Entroooooooooooooo");
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
				Pedido unPedido = (Pedido) parent.getAdapter()
						.getItem(position);
				Bundle bundle = new Bundle();
				bundle.putParcelable("pedido", unPedido);
				bundle.putString("user", usuario);
				bundle.putString("password", password);
				Intent intent = new Intent(PedidosActivity.this,
						ProductosActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

	}

	private void parseJSON(JSONObject json) {
		try {

			JSONObject members = json.getJSONObject("members");
			JSONObject pedidos = members.getJSONObject("pedidos");
			JSONArray value = pedidos.getJSONArray("value");
			for (int i = 0; i < value.length(); i++) {
				JSONObject pedido = value.getJSONObject(i);
				Pedido unPedido = new Pedido();
				unPedido.setTitle(pedido.optString("title"));
				unPedido.setUrlDetalle(pedido.optString("href"));
				arrNews.add(unPedido);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.pedidos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.pedido) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
