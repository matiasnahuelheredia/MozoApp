package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import representacion.Mesa;
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
import com.example.volleytesting.R.id;
import com.example.volleytesting.R.layout;
import com.example.volleytesting.R.menu;
import adaptadores.PedidoAdapter;
import adaptadores.ProductoAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ProductosActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private RequestQueue mRequestQueue;
	private ArrayList<Producto> arrNews;
	private ProductoAdapter va;
	private ProgressDialog pd;
	private String usuario;
	private String password;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		arrNews = new ArrayList<Producto>();
		va = new ProductoAdapter(getApplicationContext(), arrNews);
		lstView = (ListView) findViewById(R.id.listProductos);
		lstView.setAdapter(va);
		mRequestQueue = Volley.newRequestQueue(this);
		Pedido unPedido = getIntent().getExtras().getParcelable("pedido");
		url = unPedido.getUrlDetalle();
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

	}

	private void parseJSON(JSONObject json) {
		try {

			// Con esto obtengo result donde en value tengo la lista d mesas
			JSONObject members = json.getJSONObject("members");
			// Con esto obtengo los title de las mesas
			JSONObject productosComanda = members
					.getJSONObject("productosComanda");
			JSONArray value = productosComanda.getJSONArray("value");
			for (int i = 0; i < value.length(); i++) {
				JSONObject producto = value.getJSONObject(i);
				Producto unProducto = new Producto();
				unProducto.setTitle(producto.optString("title"));
				unProducto.setUrlDetalle(producto.optString("href"));
				arrNews.add(unProducto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.productos, menu);
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
