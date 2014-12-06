package com.restotesis.mozo.actividades;

import java.util.ArrayList;
import java.util.Map;
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EleccionActivity extends Activity {

	private ListView lstView;
	private ArrayList<Eleccion> arregloElecciones;
	private EleccionAdapter eleccionAdapter;
	private RequestQueue colaSolicitud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eleccion);
		colaSolicitud = Conexion.getInstance(getApplicationContext())
				.getRequestQueue();
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
				JSONObject postItem = itemToAdd(unaEleccion);
				JsonObjectRequest jsonPost = new JsonObjectRequest(
						Request.Method.POST, unaEleccion.getInvokeURL(),
						postItem, new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								System.out.println("Entrooo");
								Intent intentRegreso = new Intent();
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
					public Map<String, String> getHeaders()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						return Conexion.createBasicAuthHeader();
					}
				};
				colaSolicitud.add(jsonPost);
			}

		});

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

}
