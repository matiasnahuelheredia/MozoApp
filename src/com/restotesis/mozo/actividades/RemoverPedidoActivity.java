package com.restotesis.mozo.actividades;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.restotesis.mozo.R;
import com.restotesis.mozo.adaptadores.PedidoAdapter;
import com.restotesis.mozo.networking.Conexion;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RemoverPedidoActivity extends Activity {

	private RequestQueue colaSolicitud;
	private ArrayList<Pedido> arregloPedidos;
	private Pedido unPedido;	
	private PedidoAdapter eleccionAdapter;
	private ListView lstView;
	private String urlInvokeBorrarPedido;
	private static final int PEDIDO_MODIFICADO_ELIMINADO = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remover_pedido);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
		urlInvokeBorrarPedido = getIntent().getExtras().getString("urlInvoke");
		arregloPedidos = new ArrayList<Pedido>();
		arregloPedidos = getIntent().getExtras().getParcelableArrayList(
				"listaPedidos");
		eleccionAdapter = new PedidoAdapter(getApplicationContext(),
				arregloPedidos);
		lstView = (ListView) findViewById(R.id.listSeleccion);
		lstView.setAdapter(eleccionAdapter);
		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				unPedido = (Pedido) parent.getAdapter().getItem(position);
				if (unPedido.getTitle().contains("Vacio")) {
					JSONObject item = itemToJSON(unPedido);
					JsonObjectRequest postRemovePedido = new JsonObjectRequest(
							Request.Method.POST, urlInvokeBorrarPedido,
							item, new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub
									actualizarArregloPedidos(response);
									Bundle bundle = new Bundle();
									bundle.putParcelableArrayList(
											"arregloPedidos", arregloPedidos);
									Intent intentRegreso = new Intent();
									intentRegreso.putExtras(bundle);
									setResult(PEDIDO_MODIFICADO_ELIMINADO,
											intentRegreso);
									finish();

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
								}
						
					};
					colaSolicitud.add(postRemovePedido);

				} else {
					Toast.makeText(getApplicationContext(),
							"El pedido debe estar vacio", Toast.LENGTH_LONG)
							.show();
					finish();
				}

			}
		});
	}

	private JSONObject itemToJSON(Pedido unPedido) {

		JSONObject item = null;
		try {
			JSONObject href = new JSONObject();
			href.put("href", unPedido.getUrlDetalle());
			JSONObject value = new JSONObject();
			value.put("value", href);
			item = new JSONObject();
			item.put("pedido", value);
			System.out.println(item.toString());
			return item;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return item;
	}

	private void actualizarArregloPedidos(JSONObject json) {
		try {
			arregloPedidos = new ArrayList<Pedido>();
			JSONArray valuePedidos = json.getJSONObject("result").getJSONObject("members")
					.getJSONObject("pedidos").getJSONArray("value");
			for (int i = 0; i < valuePedidos.length(); i++) {
				JSONObject pedido = valuePedidos.getJSONObject(i);
				Pedido unPedido = new Pedido();
				unPedido.setTitle(pedido.optString("title"));
				unPedido.setUrlDetalle(pedido.optString("href"));
				JSONObject members = pedido.getJSONObject("value")
						.getJSONObject("members");
				unPedido.setUrlPedirBebidas(members
						.getJSONObject("pedirBebidas").getJSONArray("links")
						.getJSONObject(0).optString("href"));
				unPedido.setUrlEnviar(members.getJSONObject("enviar")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlPedirGuarniciones(members
						.getJSONObject("pedirGuarniciones")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlPedirPlatosEntrada(members
						.getJSONObject("pedirPlatosEntrada")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlPedirPlatosPrincipales(members
						.getJSONObject("pedirPlatosPrincipales")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlPedirPostres(members
						.getJSONObject("pedirPostres").getJSONArray("links")
						.getJSONObject(0).optString("href"));
				unPedido.setUrlPedirOferta(members
						.getJSONObject("pedirOfertas").getJSONArray("links")
						.getJSONObject(0).optString("href"));
				unPedido.setUrlRemoveFromBebidas(members
						.getJSONObject("removeFromBebidas")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlRemoveFromComanda(members
						.getJSONObject("removeFromComanda")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlRemoveFromMenues(members
						.getJSONObject("removeFromMenues")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlRemoveFromOferta(members
						.getJSONObject("removeFromOfertas")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setUrlTomarMenues(members.getJSONObject("tomarMenues")
						.getJSONArray("links").getJSONObject(0)
						.optString("href"));
				unPedido.setEstadocomanda(members.getJSONObject("comanda")
						.getJSONObject("value").optString("title"));
				JSONObject productosComanda = members
						.getJSONObject("productosComanda");
				JSONArray valueProductos = productosComanda
						.getJSONArray("value");
				for (int j = 0; j < valueProductos.length(); j++) {
					JSONObject producto = valueProductos.getJSONObject(j);
					Producto unProducto = new Producto();
					unProducto.setTitle(producto.optString("title"));
					unProducto.setUrlDetalle(producto.optString("href"));
					unPedido.getListaProductos().add(unProducto);
				}

				JSONObject bebidasDelPedido = members.getJSONObject("bebidas");
				JSONArray valueBebidas = bebidasDelPedido.getJSONArray("value");
				for (int k = 0; k < valueBebidas.length(); k++) {
					JSONObject producto = valueBebidas.getJSONObject(k);
					Producto unProducto = new Producto();
					unProducto.setTitle(producto.optString("title"));
					unProducto.setUrlDetalle(producto.optString("href"));
					unPedido.getListaProductos().add(unProducto);
				}

				JSONObject menuesComanda = members
						.getJSONObject("menuesComanda");
				JSONArray valueMenues = menuesComanda.getJSONArray("value");
				for (int l = 0; l < valueMenues.length(); l++) {
					JSONObject producto = valueMenues.getJSONObject(l);
					Producto unProducto = new Producto();
					unProducto.setTitle(producto.optString("title"));
					unProducto.setUrlDetalle(producto.optString("href"));
					unPedido.getListaProductos().add(unProducto);
				}
				JSONObject ofertasComanda = members
						.getJSONObject("ofertasComanda");
				JSONArray valueOfertas = ofertasComanda.getJSONArray("value");
				for (int l = 0; l < valueOfertas.length(); l++) {
					JSONObject producto = valueOfertas.getJSONObject(l);
					Producto unProducto = new Producto();
					unProducto.setTitle(producto.optString("title"));
					unProducto.setUrlDetalle(producto.optString("href"));
					unPedido.getListaProductos().add(unProducto);
				}
				arregloPedidos.add(unPedido);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remover_pedido, menu);
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
}
