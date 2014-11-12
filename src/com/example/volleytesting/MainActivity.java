package com.example.volleytesting;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import representacion.Mesa;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.volleytesting.R;
import com.example.volleytesting.R.id;
import com.example.volleytesting.R.layout;
import com.example.volleytesting.R.menu;

import actividades.PedidosActivity;
import adaptadores.MesaAdapter;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private ArrayList<Mesa> mesasAsignadas;
	private MesaAdapter adapterMesa;
	private ProgressDialog dialogoProgreso;
	private JSONArray listaMesas;
	private ProgressDialog pd;
	String uri;
	RequestQueue solicitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// uri = getIntent().getExtras().getString("url");
		// mesasAsignadas =
		// getIntent().getParcelableArrayListExtra("mesasAsignadas");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			solicitud = Volley.newRequestQueue(getApplicationContext());
			pd = ProgressDialog.show(this, "Aguarde por favor...",
					"Aguarde por favor...");
			String url = "http://192.168.1.117:8080/resto-webapp/restful/services/dom.mesa.MesaServicio/actions/listarMesasAsignadas/invoke";
			AuthRequest jr = new AuthRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, response.toString());
							parseJSON(response);
							// va.notifyDataSetChanged();
							pd.dismiss();
							;
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i(TAG, error.getMessage());
						}
					});
			solicitud.add(jr);
			adapterMesa = new MesaAdapter(getApplicationContext(),
					mesasAsignadas);
			lstView = (ListView) findViewById(R.id.listMesas);
			lstView.setAdapter(adapterMesa);
			pd.dismiss();
			lstView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// String urlMesa = mesasAsignadas.get(position).get;
					Bundle bundle = new Bundle();
					// bundle.putString("urlMesa", urlMesa);
					Intent intent = new Intent(MainActivity.this,
							PedidosActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void parseJSON(JSONObject json) {

		try {

			// Con esto obtengo result donde en value
			// tengo la
			// lista d mesas
			JSONObject result = json.getJSONObject("result");
			// Con esto obtengo los title de las mesas
			JSONArray list = result.getJSONArray("value");
			for (int i = 0; i < list.length(); i++) {
				JSONObject mesa = list.getJSONObject(i);
				Mesa unaMesa = new Mesa();
				unaMesa.setTitle(mesa.optString("title"));
				// unaMesa.setUrlDetalle(mesa.optString("href"));
				mesasAsignadas.add(unaMesa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
