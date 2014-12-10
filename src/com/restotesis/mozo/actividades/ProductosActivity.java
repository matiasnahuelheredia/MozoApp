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
import com.restotesis.mozo.adaptadores.ProductoAdapter;
import com.restotesis.mozo.networking.Conexion;
import com.restotesis.mozo.representacion.Eleccion;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProductosActivity extends Activity {

	private ListView lstView;
	private ArrayList<Producto> arregloProductos;
	private ArrayList<Eleccion> arregloElecciones;
	private ProductoAdapter adaptadorProductos;
	private Pedido unPedido;
	private RequestQueue colaSolicitud;
	private int PEDIDO_MODIFICADO = 1;
	private int PEDIDO_SINMODIFICAION = 0;
	private String enviar = null;
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private String[] agregar;
	private String[] eliminar;
	private int codigoSolicitud = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
		arregloProductos = new ArrayList<Producto>();
		unPedido = getIntent().getExtras().getParcelable("elPedido");
		arregloProductos = unPedido.getListaProductos();
		adaptadorProductos = new ProductoAdapter(getApplicationContext(),
				arregloProductos);
		lstView = (ListView) findViewById(R.id.listProductos);
		lstView.setAdapter(adaptadorProductos);
		/* Probando Drawer Navigation */
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		agregar = getResources().getStringArray(R.array.agregar_aProductos);
		eliminar = getResources().getStringArray(R.array.eliminar_deProductos);
		mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerOptions.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				agregar));
		mDrawerOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String eleccion = (String) parent.getAdapter()
						.getItem(position);
				if (eleccion.contains("Agregar")) {
					switch (eleccion) {
					case "Agregar Bebidas": {
						realizarPeticion(unPedido.getUrlPedirBebidas(),
								eleccion);
					}
						break;
					case "Agregar Ofertas": {
						realizarPeticion(unPedido.getUrlPedirOferta(), eleccion);
					}
						break;
					case "Agregar Menues": {
						realizarPeticion(unPedido.getUrlTomarMenues(), eleccion);
					}
						break;
					case "Agregar Entrada": {
						realizarPeticion(unPedido.getUrlPedirPlatosEntrada(),
								eleccion);
					}
						break;
					case "Agregar Principal": {
						realizarPeticion(
								unPedido.getUrlPedirPlatosPrincipales(),
								eleccion);
					}
						break;
					case "Agregar Guarnicion": {
						realizarPeticion(unPedido.getUrlPedirGuarniciones(),
								eleccion);
					}
						break;
					case "Agregar Postre": {
						realizarPeticion(unPedido.getUrlPedirPostres(),
								eleccion);
					}
						break;

					default:
						break;
					}
				} else {
					switch (eleccion) {
					case "Eliminar Bebidas": {
						realizarPeticion(unPedido.getUrlRemoveFromBebidas(),
								eleccion);
					}
						break;
					case "Eliminar Ofertas": {
						realizarPeticion(unPedido.getUrlRemoveFromOferta(),
								eleccion);
					}
						break;
					case "Eliminar Menues": {
						realizarPeticion(unPedido.getUrlRemoveFromMenues(),
								eleccion);
					}
						break;
					case "Eliminar de Comanda": {
						realizarPeticion(unPedido.getUrlRemoveFromComanda(),
								eleccion);
					}
						break;

					default:
						break;
					}

				}
				mDrawer.closeDrawers();
			}
		});
		/* Fin Drawer Navigation */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.productos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		arregloElecciones = new ArrayList<Eleccion>();
		int id = item.getItemId();
		/* Opcion de Abrir el Drawer Navigation */
		if (id == R.id.agregar) {
			mDrawerOptions.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					agregar));
			if (mDrawer.isDrawerOpen(mDrawerOptions)) {
				mDrawer.closeDrawers();
			} else {
				mDrawer.openDrawer(mDrawerOptions);
			}
		}
		if (id == R.id.eliminar) {
			mDrawerOptions.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					eliminar));
			if (mDrawer.isDrawerOpen(mDrawerOptions)) {
				mDrawer.closeDrawers();
			} else {
				mDrawer.openDrawer(mDrawerOptions);
			}
		}
		/* Fin opcion abrir Drawer Navigation */
		if (id == R.id.enviarPedido) {

			realizarPeticion(unPedido.getUrlEnviar(), "Enviar");

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			unPedido = data.getExtras().getParcelable("pedidoActualizado");
			adaptadorProductos.actualizar(unPedido.getListaProductos());
			adaptadorProductos.notifyDataSetChanged();
			Bundle bundle = new Bundle();
			bundle.putParcelable("pedidoActualizado", unPedido);
			bundle.putInt("posicion", getIntent().getExtras()
					.getInt("posicion"));
			Intent intentRegreso = new Intent();
			intentRegreso.putExtras(bundle);
			setResult(PEDIDO_MODIFICADO, intentRegreso);
			System.out.println("Result OK");
			if (requestCode == 1) {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void parseJSONChoices(JSONObject json) {
		try {

			JSONArray links = json.getJSONArray("links");
			String invokeURL = links.getJSONObject(2).getString("href")
					.toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
			String tipo = null;
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
				tipo = "guarnición1";
			}
			if (temp.toString().contains("menu1")) {
				item = parameters.getJSONObject("menu1");
				tipo = "menu1";
			}

			if (temp.toString().contains("oferta1")) {
				item = parameters.getJSONObject("oferta1");
				tipo = "oferta1";
			}

			JSONArray choices = item.getJSONArray("choices");
			for (int i = 0; i < choices.length(); i++) {
				JSONObject unaOpcion = choices.getJSONObject(i);
				Eleccion unaEleccion = new Eleccion();
				unaEleccion.setTitle(unaOpcion.optString("title"));
				unaEleccion.setUrl(unaOpcion.optString("href"));
				unaEleccion.setTipo(tipo);
				unaEleccion.setInvokeURL(invokeURL);
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseJSONRemoveChoices(JSONObject json) {
		try {

			JSONArray links = json.getJSONArray("links");
			String invokeURL = links.getJSONObject(2).getString("href")
					.toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
			String tipo = null;
			if (temp.toString().contains("bebida")) {
				item = parameters.getJSONObject("bebida");
				tipo = "bebida";
			}
			if (temp.toString().contains("producto")) {
				item = parameters.getJSONObject("producto");
				tipo = "producto";
			}
			if (temp.toString().contains("menu")) {
				item = parameters.getJSONObject("menu");
				tipo = "menu";
			}

			if (temp.toString().contains("oferta")) {
				item = parameters.getJSONObject("oferta");
				tipo = "oferta";
			}

			JSONArray choices = item.getJSONArray("choices");
			for (int i = 0; i < choices.length(); i++) {
				JSONObject unaOpcion = choices.getJSONObject(i);
				Eleccion unaEleccion = new Eleccion();
				unaEleccion.setTitle(unaOpcion.optString("title"));
				unaEleccion.setUrl(unaOpcion.optString("href"));
				unaEleccion.setTipo(tipo);
				unaEleccion.setInvokeURL(invokeURL);
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void realizarPeticion(String urlPeticion, final String tipo) {
		JsonObjectRequest solicitudEleccion = new JsonObjectRequest(
				Request.Method.GET, urlPeticion, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						if (tipo.contains("Eliminar")) {
							parseJSONRemoveChoices(response);
						}
						if (tipo.contains("Agregar")) {
							parseJSONChoices(response);
						}
						if (tipo.contains("Enviar")) {
							try {
								enviar = response.getJSONArray("links")
										.getJSONObject(2).optString("href");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							/*
							 * Para notificar al onActivityResult sobre que se
							 * trato de enviar una Comanda/Pedido
							 */
							codigoSolicitud = 1;
						}
						Bundle bundle = new Bundle();
						bundle.putParcelableArrayList("listaElecciones",
								arregloElecciones);
						bundle.putParcelable("elPedido", unPedido);
						bundle.putString("enviar", enviar);
						Intent intent = new Intent(ProductosActivity.this,
								EleccionActivity.class);
						intent.putExtras(bundle);
						System.out.println("entroooo");
						startActivityForResult(intent, codigoSolicitud);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						System.out.println("No entrooo");
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				return Conexion.createBasicAuthHeader();
			}

		};
		colaSolicitud.add(solicitudEleccion);
	}

}
