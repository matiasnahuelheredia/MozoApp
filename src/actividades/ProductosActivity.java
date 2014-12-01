package actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import representacion.Eleccion;
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

import adaptadores.EleccionAdapter;
import adaptadores.PedidoAdapter;
import adaptadores.ProductoAdapter;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ProductosActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private ArrayList<Producto> arregloProductos;
	private ArrayList<Eleccion> arregloElecciones;
	private ProductoAdapter adaptadorProductos;
	private ProgressDialog pd;
	private Pedido unPedido;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productos);
		arregloProductos = new ArrayList<Producto>();
		arregloProductos = getIntent().getExtras().getParcelableArrayList(
				"listaProductos");
		adaptadorProductos = new ProductoAdapter(getApplicationContext(),
				arregloProductos);
		lstView = (ListView) findViewById(R.id.listProductos);
		lstView.setAdapter(adaptadorProductos);
		unPedido = getIntent().getExtras().getParcelable("elPedido");

	}

	private void cargarLista() {

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
		getMenuInflater().inflate(R.menu.productos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if (id == R.id.bebidas) {

			url = unPedido.getUrlPedirBebidas();
			
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

			return true;
		}
		if (id == R.id.menues) {
			url = unPedido.getUrlTomarMenues();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

			return true;

		}

		if (id == R.id.entrada) {
			url = unPedido.getUrlPedirPlatosEntrada();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

			return true;

		}
		if (id == R.id.principal) {
			url = unPedido.getUrlPedirPlatosPrincipales();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

			return true;

		}

		if (id == R.id.guarnicion) {
			url = unPedido.getUrlPedirGuarniciones();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

			return true;

		}

		if (id == R.id.postre) {
			url = unPedido.getUrlPedirPostres();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			Intent intent = new Intent(ProductosActivity.this,
					EleccionActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);

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

				Producto unProducto = new Producto();
				unProducto = data.getExtras().getParcelable("productoNew");
				String temp = data.getExtras().getString("urlPedido");

			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void parseJSONChoices(JSONObject json) {
		try {
			JSONArray links = json.getJSONArray("links");
			urlInvoke = links.getJSONObject(2).getString("href").toString();
			JSONObject parameters = json.getJSONObject("parameters");
			JSONArray temp = parameters.names();
			JSONObject item = null;
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
				tipo = "guarnici�";
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
				arregloElecciones.add(unaEleccion);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
