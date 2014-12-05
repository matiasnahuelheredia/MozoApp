package actividades;

import java.util.ArrayList;
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
import com.example.volleytesting.R;
import adaptadores.PedidoAdapter;
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
	private ArrayList<Producto> arregloProductos;
	private Pedido unPedido;
	private PedidoAdapter adaptadorPedidos;
	private RequestQueue colaSolicitud;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedidos);
		colaSolicitud = Conexion.getInstance(
				getApplicationContext()).getRequestQueue();
//		arregloProductos = new ArrayList<Producto>();
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
				unPedido = (Pedido) parent.getAdapter().getItem(position);
				final int posicionPedido = position;
				final ProgressDialog pd = ProgressDialog.show(
						PedidosActivity.this, "Aguarde por favor...",
						"Aguarde por favor...");
				JsonObjectRequest solicitudProductos = new JsonObjectRequest(
						Request.Method.GET, unPedido.getUrlDetalle(), null,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								System.out.println("Entrooooooo");
								arregloProductos = new ArrayList<Producto>();
								parseJSON(response);	
								arregloPedidos.get(posicionPedido).llenarLinks(response);
								Bundle bundle = new Bundle();
								bundle.putParcelableArrayList("listaProductos",
										arregloProductos);
								bundle.putParcelable("elPedido", arregloPedidos.get(posicionPedido));
								Intent intent = new Intent(
										PedidosActivity.this,
										ProductosActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
								pd.dismiss();

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								System.out.println("Noo Entrooooooo");
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

				colaSolicitud.add(solicitudProductos);

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
			final Mesa unaMesa = getIntent().getExtras().getParcelable("unaMesa");
			if (!(unaMesa.getTomarPedidoURL().contains("invoke"))){
				//Obtengo el invoke
				JsonObjectRequest solicitudMesa = new JsonObjectRequest(Request.Method.GET, unaMesa.getTomarPedidoURL(),null, 
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								try {
									System.out.println(response.getJSONArray("links").getJSONObject(2).getString("href"));									
									unaMesa.setTomarPedidoURL(response.getJSONArray("links").getJSONObject(2).getString("href"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, 
						new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								System.out.println("No entroo a refrescar!");
								
							}
						}){

							@Override
							public Map<String, String> getHeaders()
									throws AuthFailureError {
								// TODO Auto-generated method stub
								return Conexion.createBasicAuthHeader();
							}
					
				};				
				colaSolicitud.add(solicitudMesa);
			}
			//Realizo el POST
			System.out.println(unaMesa.getTomarPedidoURL());
			JsonObjectRequest pedidoPost = new JsonObjectRequest(Request.Method.POST, unaMesa.getTomarPedidoURL(), null, 
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							
						}
					}, 
					new Response.ErrorListener() {

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
			colaSolicitud.add(pedidoPost);
			
			JsonObjectRequest actulizarPedidos = new JsonObjectRequest(Request.Method.GET, unaMesa.getUrlDetalle(), null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub							
							actualizarArregloPedidos(response);
							adaptadorPedidos.actualizar(arregloPedidos);
							adaptadorPedidos.notifyDataSetChanged();
						}
					},
					new Response.ErrorListener() {

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
			colaSolicitud.add(actulizarPedidos);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void actualizarArregloPedidos(JSONObject json) {
		try {
			arregloPedidos = new ArrayList<Pedido>();
			JSONObject members = json.getJSONObject("members");
			JSONObject pedidos = members.getJSONObject("pedidos");
			JSONArray value = pedidos.getJSONArray("value");
			for (int i = 0; i < value.length(); i++) {
				JSONObject pedido = value.getJSONObject(i);
				Pedido unPedido = new Pedido();
				unPedido.setTitle(pedido.optString("title"));
				unPedido.setUrlDetalle(pedido.optString("href"));
				arregloPedidos.add(unPedido);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
