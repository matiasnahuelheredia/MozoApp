package actividades;

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
import com.example.volleytesting.R;
import networking.Conexion;
import representacion.Mesa;
import representacion.Pedido;
import adaptadores.MesaAdapter;
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
//		arregloPedidos = new ArrayList<Pedido>();
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
				final int posicionMesa= position;
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
								arregloMesas.get(posicionMesa).llenarLinks(response);
								Bundle bundle = new Bundle();
								bundle.putParcelable("unaMesa", arregloMesas.get(posicionMesa));
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
