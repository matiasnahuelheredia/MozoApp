package forTesting;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.volleytesting.AuthRequest;
import com.example.volleytesting.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Auxiliar extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private ListView lstView;
	private RequestQueue mRequestQueue;
	private ArrayList<Mesa> arrNews;
	private LayoutInflater lf;
	private MesaAdapter va;
	private ProgressDialog pd;
	private JSONArray listaMesas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auxiliar);
		lf = LayoutInflater.from(this);
		arrNews = new ArrayList<Mesa>();
		va = new MesaAdapter();
		lstView = (ListView) findViewById(R.id.listaMesas);
		lstView.setAdapter(va);
		mRequestQueue = Volley.newRequestQueue(this);

	}

	private void parseJSON(JSONObject json) {
		try {

			// Con esto obtengo result donde en value tengo la lista d mesas
			JSONObject result = json.getJSONObject("result");
			// Con esto obtengo los title de las mesas
			listaMesas = result.getJSONArray("value");
			parsearMesas(listaMesas);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void parsearMesas(JSONArray jsonList) {
		try {
			for (int i = 0; i < jsonList.length(); i++) {
				JSONObject mesa = jsonList.getJSONObject(i);
				Mesa unaMesa = new Mesa();
				unaMesa.setTitle(mesa.optString("title"));
				unaMesa.setUrlDetalle(mesa.optString("href"));
				arrNews.add(unaMesa);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.auxiliar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			String url = "http://192.168.1.117:8080/resto-webapp/restful/services/dom.mesa.MesaServicio/actions/listarMesasAsignadas/invoke";
			pd = ProgressDialog.show(this, "Aguarde por favor...",
					"Aguarde por favor...");
			AuthRequest jr = new AuthRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, response.toString());
							parseJSON(response);
							va.notifyDataSetChanged();
							pd.dismiss();
							;
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.i(TAG, error.getMessage());
						}
					});
			mRequestQueue.add(jr);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MesaAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = lf.inflate(R.layout.linea_mesaview, null);
				vh.tvTitle = (TextView) convertView
						.findViewById(R.id.txtNroMesa);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			Mesa unaMesa = arrNews.get(position);
			vh.tvTitle.setText(unaMesa.getTitle());

			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
		}

	}
}
