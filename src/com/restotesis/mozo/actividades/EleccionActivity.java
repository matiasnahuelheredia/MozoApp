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
import com.restotesis.mozo.adaptadores.EleccionAdapter;
import com.restotesis.mozo.networking.Conexion;
import com.restotesis.mozo.representacion.Eleccion;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EleccionActivity extends Activity {

	private ListView lstView;
	private ArrayList<Eleccion> arregloElecciones;
	private ArrayList<Producto> arregloProductos;
	private Pedido unPedido;
	private EleccionAdapter eleccionAdapter;
	private RequestQueue colaSolicitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eleccion);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
		unPedido = getIntent().getExtras().getParcelable("elPedido");
		String enviar = getIntent().getExtras().getString("enviar");
		if (enviar != null) {
			if (!(enviar.contains("invoke"))) {
				Toast.makeText(getApplicationContext(), "No es Posible enviar",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Productos enviados",
						Toast.LENGTH_LONG).show();
				realizarPeticionEleccion(enviar, null);
			}

		}
		arregloElecciones = new ArrayList<Eleccion>();
		arregloElecciones = getIntent().getExtras().getParcelableArrayList(
				"listaElecciones");
		eleccionAdapter = new EleccionAdapter(getApplicationContext(),
				arregloElecciones);
		lstView = (ListView) findViewById(R.id.listEleccion);
		lstView.setAdapter(eleccionAdapter);
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Eleccion unaEleccion = (Eleccion) parent.getAdapter().getItem(
						position);
				String accionEliminar = getIntent().getExtras().getString(
						"accionEliminar");
				if (accionEliminar == null) {
					Bundle bundle = new Bundle();
					bundle.putParcelable("suEleccion", unaEleccion);
					Intent intentEleccion = new Intent(EleccionActivity.this,
							DetalleActivity.class);
					intentEleccion.putExtras(bundle);
					startActivityForResult(intentEleccion, 0);
				}
				else {
					JSONObject postItem = itemToDel(unaEleccion);
					realizarPeticionEleccion(unaEleccion.getInvokeURL(), postItem);
					
				}

			}

		});

	}

	private void realizarPeticionEleccion(String urlPeticion,
			JSONObject objectoPOST) {

		JsonObjectRequest jsonPost = new JsonObjectRequest(Request.Method.POST,
				urlPeticion, objectoPOST, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println("Entrooo");
						parseJSON(response);
						Bundle bundle = new Bundle();
						bundle.putParcelable("pedidoActualizado", unPedido);
						Intent intentRegreso = new Intent();
						intentRegreso.putExtras(bundle);
						setResult(RESULT_OK, intentRegreso);
						finish();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				return Conexion.createBasicAuthHeader();
			}
		};
		colaSolicitud.add(jsonPost);
	}

	private JSONObject itemToAdd(Eleccion unaEleccion) {

		JSONObject item = null;
		try {
			JSONObject href = new JSONObject();
			href.put("href", unaEleccion.getUrl());
			JSONObject value = new JSONObject();
			value.put("value", href);
			item = new JSONObject();
			item.put(unaEleccion.getTipo(), value);

			JSONObject valueCantidad = new JSONObject();
			valueCantidad.put("value", unaEleccion.getCantidad());
			item.put("cantidad", valueCantidad);

			JSONObject valueNota = new JSONObject();
			valueNota.put("value", unaEleccion.getNota());
			item.put("nota", valueNota);

			System.out.println(item.toString());
			return item;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return item;
	}

	private JSONObject itemToDel(Eleccion unaEleccion) {

		JSONObject item = null;
		try {
			JSONObject href = new JSONObject();
			href.put("href", unaEleccion.getUrl());
			JSONObject value = new JSONObject();
			value.put("value", href);
			item = new JSONObject();
			item.put(unaEleccion.getTipo(), value);

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

	private void parseJSON(JSONObject json) {
		JSONObject members = null;
		arregloProductos = new ArrayList<Producto>();
		try {
			members = json.getJSONObject("result").getJSONObject("members");
			if (unPedido.getTitle().contains("Vac")) {
				unPedido.setTitle(json.getJSONObject("result").optString(
						"title"));
			}
			unPedido.setEstadocomanda(members.getJSONObject("comanda")
					.getJSONObject("value").optString("title"));
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
			JSONObject ofertasComanda = members.getJSONObject("ofertasComanda");
			JSONArray valueOfertas = ofertasComanda.getJSONArray("value");
			for (int l = 0; l < valueOfertas.length(); l++) {
				JSONObject producto = valueOfertas.getJSONObject(l);
				Producto unProducto = new Producto();
				unProducto.setTitle(producto.optString("title"));
				unProducto.setUrlDetalle(producto.optString("href"));
				arregloProductos.add(unProducto);
			}
			unPedido.setListaProductos(arregloProductos);
			if (unPedido.getListaProductos().isEmpty()) {
				unPedido.setTitle(json.getJSONObject("result").optString(
						"title"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == RESULT_OK) {
			Eleccion suEleccion = data.getExtras().getParcelable("suEleccion");
			JSONObject postItem = itemToAdd(suEleccion);
			realizarPeticionEleccion(suEleccion.getInvokeURL(), postItem);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
