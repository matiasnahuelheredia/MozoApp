package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import networking.Conexion;

import org.json.JSONArray;
import org.json.JSONException;
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
	
	private ListView lstView;
	private ArrayList<Pedido> arregloPedidos;
	private ArrayList<Producto> arregloProductos;
	private PedidoAdapter adaptadorPedidos;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedidos);
		final RequestQueue colaSolicitud = Conexion.getInstance(
				getApplicationContext()).getRequestQueue();
		arregloPedidos = new ArrayList<Pedido>();
		arregloPedidos = getIntent().getExtras().getParcelableArrayList(
				"listaPedidos");
		adaptadorPedidos = new PedidoAdapter(getApplicationContext(),
				arregloPedidos);
		lstView = (ListView) findViewById(R.id.listPedidos);
		lstView.setAdapter(adaptadorPedidos);
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Pedido unPedido = (Pedido) parent.getAdapter()
						.getItem(position);
				final ProgressDialog pd = ProgressDialog.show(
						PedidosActivity.this, "Aguarde por favor...",
						"Aguarde por favor...");
				JsonObjectRequest solicitudProductos = new JsonObjectRequest(Request.Method.GET, unPedido.getUrlDetalle(), null, 
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								
								
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
						return Conexion.createBasicAuthHeader();
					}};
				Bundle bundle = new Bundle();
				bundle.putParcelable("pedido", unPedido);				
				Intent intent = new Intent(PedidosActivity.this,
						ProductosActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

	}
	
	private void parseJSON(JSONObject json) {
		JSONObject members = null;
		try {

			// Con esto obtengo result donde en value tengo la lista d mesas
			members = json.getJSONObject("members");
			// Con esto obtengo los title de las mesas
			JSONObject productosComanda = members
					.getJSONObject("productosComanda");
			JSONArray valueProductos = productosComanda.getJSONArray("value");
			for (int i = 0; i < valueProductos.length(); i++) {
				JSONObject producto = valueProductos.getJSONObject(i);
				Producto unProducto = new Producto();
				unProducto.setTitle(producto.optString("title"));
				unProducto.setUrlDetalle(producto.optString("href"));
				arregloProductos.add(unProducto);
			}

			JSONObject bebidasDelPedido = members.getJSONObject("bebidas");
			JSONArray valueBebidas = bebidasDelPedido.getJSONArray("value");
			for (int i = 0; i < valueBebidas.length(); i++) {
				JSONObject producto = valueBebidas.getJSONObject(i);
				Producto unProducto = new Producto();
				unProducto.setTitle(producto.optString("title"));
				unProducto.setUrlDetalle(producto.optString("href"));
				arregloProductos.add(unProducto);
			}

			JSONObject menuesComanda = members.getJSONObject("menuesComanda");
			JSONArray valueMenues = menuesComanda.getJSONArray("value");
			for (int i = 0; i < valueMenues.length(); i++) {
				JSONObject producto = valueMenues.getJSONObject(i);
				Producto unProducto = new Producto();
				unProducto.setTitle(producto.optString("title"));
				unProducto.setUrlDetalle(producto.optString("href"));
				arregloProductos.add(unProducto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		unPedido.setUrlPedirBebidas(setearURL("pedirBebidas", members));
		unPedido.setUrlRemoveFromBebidas(setearURL("removeFromBebidas", members));
		unPedido.setUrlEnviar(setearURL("enviar", members));
		unPedido.setUrlTomarMenues(setearURL("tomarMenues", members));
		unPedido.setUrlRemoveFromMenues(setearURL("removeFromMenues", members));
		unPedido.setUrlPedirPlatosEntrada(setearURL("pedirPlatosEntrada",
				members));
		unPedido.setUrlPedirPlatosPrincipales(setearURL(
				"pedirPlatosPrincipales", members));
		unPedido.setUrlPedirGuarniciones(setearURL("pedirGuarniciones", members));
		unPedido.setUrlPedirPostres(setearURL("pedirPostres", members));
		unPedido.setUrlRemoveFromComanda(setearURL("removeFromComanda", members));
	}

	private String setearURL(String accion, JSONObject unJSONObject) {

		try {
			JSONObject accionDo = unJSONObject.getJSONObject(accion);
			JSONArray linkDo = accionDo.getJSONArray("links");
			JSONObject arregloDo = linkDo.getJSONObject(0);
			return arregloDo.optString("href");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
