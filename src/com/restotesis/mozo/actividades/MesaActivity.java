package com.restotesis.mozo.actividades;

import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.restotesis.mozo.R;
import com.restotesis.mozo.adaptadores.MesaAdapter;
import com.restotesis.mozo.networking.Conexion;
import com.restotesis.mozo.representacion.Mesa;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MesaActivity extends Activity {

	private ListView lstView;
	private ArrayList<Mesa> arregloMesas;
	private ArrayList<Pedido> arregloPedidos;
	private MesaAdapter adaptadorMesas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mesa);
		arregloMesas = getIntent().getExtras().getParcelableArrayList(
				"listaDeMesas");
		adaptadorMesas = new MesaAdapter(getApplicationContext(), arregloMesas);
		lstView = (ListView) findViewById(R.id.lista_mesas);
		lstView.setAdapter(adaptadorMesas);
		final RequestQueue colaSolicitud = Conexion.getInstance(
				getApplicationContext()).getRequestQueue();
		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Mesa unaMesa = (Mesa) parent.getAdapter().getItem(position);
				final int posicionMesa = position;
				final ProgressDialog pd = ProgressDialog.show(
						MesaActivity.this, "Aguarde por favor...",
						"Aguarde por favor...");
				JsonObjectRequest solicitudPedidos = new JsonObjectRequest(
						Request.Method.GET, unaMesa.getUrlDetalle(), null,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								System.out.println("Entroooooooo");
								arregloPedidos = new ArrayList<Pedido>();
								parseJSON(response);
								arregloMesas.get(posicionMesa).llenarLinks(
										response);
								Bundle bundle = new Bundle();
								bundle.putParcelable("unaMesa",
										arregloMesas.get(posicionMesa));
								bundle.putParcelableArrayList("listaPedidos",
										arregloPedidos);
								Intent intent = new Intent(MesaActivity.this,
										PedidosActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
								pd.dismiss();

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								System.out.println("Noooo Entroooooooo");
								pd.dismiss();

							}
						}) {

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						return Conexion.createBasicAuthHeader();
					}

				};
				colaSolicitud.add(solicitudPedidos);

			}
		});
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

		Toast.makeText(getApplicationContext(), "Esta Saliendo de la APP",
				Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	private void parseJSON(JSONObject json) {
		try {

			JSONArray valuePedidos = json.getJSONObject("members")
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

}
