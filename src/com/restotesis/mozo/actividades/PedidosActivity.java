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

public class PedidosActivity extends Activity {

	private ListView lstView;
	private ArrayList<Pedido> arregloPedidos;
	private Mesa unaMesa;
	private PedidoAdapter adaptadorPedidos;
	private RequestQueue colaSolicitud;
	private static final int PEDIDO_MODIFICADO = 1;
	private static final int PEDIDO_MODIFICADO_ELIMINADO = 5;
	private static final int PEDIDO = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedidos);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
		unaMesa = new Mesa();
		unaMesa = getIntent().getExtras().getParcelable("unaMesa");
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
				final ProgressDialog pd = ProgressDialog.show(
						PedidosActivity.this, "Aguarde por favor...",
						"Aguarde por favor...");
				Bundle bundle = new Bundle();
				bundle.putParcelable("elPedido", arregloPedidos.get(position));
				bundle.putInt("posicion", position);
				Intent intent = new Intent(PedidosActivity.this,
						ProductosActivity.class);
				intent.putExtras(bundle);
				pd.dismiss();
				startActivityForResult(intent, PEDIDO);

			}
		});

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
			final Mesa unaMesa = getIntent().getExtras().getParcelable(
					"unaMesa");
			if (!(unaMesa.getTomarPedidoURL().contains("invoke"))) {
				// Obtengo el invoke
				System.out.println(unaMesa.getTomarPedidoURL());
				JsonObjectRequest solicitudMesa = new JsonObjectRequest(
						Request.Method.GET, unaMesa.getTomarPedidoURL(), null,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								try {
									System.out
											.println("Entro a obtener invoke");
									unaMesa.setTomarPedidoURL(response
											.getJSONArray("links")
											.getJSONObject(2).getString("href"));
									System.out.println(unaMesa
											.getTomarPedidoURL());
									// Realizo POST
									JsonObjectRequest pedidoPost = new JsonObjectRequest(
											Request.Method.POST,
											unaMesa.getTomarPedidoURL(),
											null,
											new Response.Listener<JSONObject>() {

												@Override
												public void onResponse(
														JSONObject response) {
													// TODO Auto-generated
													// method stub
													// Actualizo
													JsonObjectRequest actulizarPedidos = new JsonObjectRequest(
															Request.Method.GET,
															unaMesa.getUrlDetalle(),
															null,
															new Response.Listener<JSONObject>() {

																@Override
																public void onResponse(
																		JSONObject response) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	System.out
																			.println(response);
																	actualizarArregloPedidos(response);
																	adaptadorPedidos
																			.actualizar(arregloPedidos);
																	adaptadorPedidos
																			.notifyDataSetChanged();
																}
															},
															new Response.ErrorListener() {

																@Override
																public void onErrorResponse(
																		VolleyError error) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub

																}
															}) {

														@Override
														public Map<String, String> getHeaders()
																throws AuthFailureError {
															// TODO
															// Auto-generated
															// method stub
															return Conexion
																	.createBasicAuthHeader();
														}

													};
													colaSolicitud
															.add(actulizarPedidos);

												}
											}, new Response.ErrorListener() {

												@Override
												public void onErrorResponse(
														VolleyError error) {
													// TODO Auto-generated
													// method stub

												}
											}) {

										@Override
										public Map<String, String> getHeaders()
												throws AuthFailureError {
											// TODO Auto-generated method stub
											return Conexion
													.createBasicAuthHeader();
										}

									};
									colaSolicitud.add(pedidoPost);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								System.out.println("No entroo a refrescar!");

							}
						}) {

					@Override
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						return Conexion.createBasicAuthHeader();
					}

				};
				colaSolicitud.add(solicitudMesa);
			}

			return true;
		}
		if (id == R.id.borrarPedido) {
			JsonObjectRequest solcitudRemoverPedido = new JsonObjectRequest(Request.Method.GET, unaMesa.getBorrarPedidoURL(),
					null, 
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							String urlInvoke = null;
							try {
								urlInvoke = response.getJSONArray("links").getJSONObject(2).optString("href");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Bundle bundle = new Bundle();
							bundle.putString("urlInvoke", urlInvoke);
							bundle.putParcelableArrayList("listaPedidos", arregloPedidos);
							Intent intent = new Intent(getApplicationContext(),
									RemoverPedidoActivity.class);
							intent.putExtras(bundle);
							startActivityForResult(intent, PEDIDO);
							
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
			colaSolicitud.add(solcitudRemoverPedido);			
		}
		return super.onOptionsItemSelected(item);
	}

	private void actualizarArregloPedidos(JSONObject json) {
		try {
			arregloPedidos = new ArrayList<Pedido>();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == PEDIDO_MODIFICADO) {
			int posicion = data.getExtras().getInt("posicion");
			Pedido nuevoPedido = data.getExtras().getParcelable(
					"pedidoActualizado");
			arregloPedidos.set(posicion, nuevoPedido);
			adaptadorPedidos.actualizar(arregloPedidos);
			adaptadorPedidos.notifyDataSetChanged();
		}
		if (resultCode == PEDIDO_MODIFICADO_ELIMINADO) {
			
			arregloPedidos = new ArrayList<Pedido>();
			arregloPedidos = data.getExtras().getParcelableArrayList("arregloPedidos");
			adaptadorPedidos.actualizar(arregloPedidos);
			adaptadorPedidos.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
