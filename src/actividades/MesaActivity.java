package actividades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import networking.Conexion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import representacion.Mesa;

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

import adaptadores.MesaAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Mesa unaMesa = (Mesa) parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putParcelable("mesa", unaMesa);
				Intent intent = new Intent(MesaActivity.this,
						PedidosActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

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

}
