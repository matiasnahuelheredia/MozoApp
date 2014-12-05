package actividades;

import java.util.ArrayList;
import java.util.Map;
import networking.Conexion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import representacion.Eleccion;
import representacion.Pedido;
import representacion.Producto;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.volleytesting.R;
import adaptadores.ProductoAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ProductosActivity extends Activity {
	
	private ListView lstView;
	private ArrayList<Producto> arregloProductos;
	private ArrayList<Eleccion> arregloElecciones;
	private ProductoAdapter adaptadorProductos;
	private Pedido unPedido;		
	private RequestQueue colaSolicitud;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		colaSolicitud = Conexion.getInstance(
				getApplicationContext()).getRequestQueue();
		arregloProductos = new ArrayList<Producto>();
//		arregloElecciones = new ArrayList<Eleccion>();
		arregloProductos = getIntent().getExtras().getParcelableArrayList(
				"listaProductos");
		adaptadorProductos = new ProductoAdapter(getApplicationContext(),
				arregloProductos);
		lstView = (ListView) findViewById(R.id.listProductos);
		lstView.setAdapter(adaptadorProductos);
		unPedido = getIntent().getExtras().getParcelable("elPedido");
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
		if (id == R.id.action_settings) {
			return true;
		}

		if (id == R.id.bebidas) {			
			JsonObjectRequest solicitudEleccion = new JsonObjectRequest(Request.Method.GET, 
					unPedido.getUrlPedirBebidas(), null, 
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							parseJSONChoices(response);	
							Bundle bundle = new Bundle();
							bundle.putParcelableArrayList("listaElecciones", arregloElecciones);
							bundle.putParcelableArrayList("listaProductos", arregloProductos);
							Intent intent = new Intent(ProductosActivity.this, EleccionActivity.class);
							intent.putExtras(bundle);
							System.out.println("entroooo");
							startActivityForResult(intent,0);
							
						}
					}, 
					new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							System.out.println("No entrooo");
						}
					}){

						@Override
						public Map<String, String> getHeaders()
								throws AuthFailureError {
							// TODO Auto-generated method stub
							return Conexion.createBasicAuthHeader();
						}
				
				
			};
			colaSolicitud.add(solicitudEleccion);
			
			return true;
		}
		if (id == R.id.menues) {
			
			

			return true;

		}

		if (id == R.id.entrada) {
			
			

			return true;

		}
		if (id == R.id.principal) {
			
			

			return true;

		}

		if (id == R.id.guarnicion) {
			
			

			return true;

		}

		if (id == R.id.postre) {
						return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case (0): {
			if (resultCode == Activity.RESULT_OK) {
				 JsonObjectRequest solicitudProductosUp = new JsonObjectRequest(Request.Method.GET, unPedido.getUrlDetalle(), null, 
						 new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								parseJSON(response);								
								adaptadorProductos.actualizar(arregloProductos);
								adaptadorProductos.notifyDataSetChanged();
								System.out.println("Result OK");								
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								System.out.println("Result Negative");
								
							}
						}){

							@Override
							public Map<String, String> getHeaders()
									throws AuthFailureError {
								// TODO Auto-generated method stub
								return Conexion.createBasicAuthHeader();
							}
					 
					 
				 };	
				 colaSolicitud.add(solicitudProductosUp);
			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void parseJSONChoices(JSONObject json) {
		try {
			
			JSONArray links = json.getJSONArray("links");
			String invokeURL = links.getJSONObject(2).getString("href").toString();
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
				tipo =  "platoPrincipal1";
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
	private void parseJSON(JSONObject json) {
		JSONObject members = null;
		try {
			arregloProductos = new ArrayList<Producto>();
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

	
	
}
