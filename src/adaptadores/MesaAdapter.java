package adaptadores;

import java.util.ArrayList;

import representacion.Mesa;

import com.example.volleytesting.R;

import com.example.volleytesting.R.id;
import com.example.volleytesting.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MesaAdapter extends BaseAdapter {

	static class ViewHolder {
		TextView tvNumeroMesa;

	}

	private static final String TAG = "CustomAdapter";
	private static int convertViewCounter = 0;
	private ArrayList<Mesa> datos;
	private LayoutInflater inflater = null;

	public MesaAdapter(Context contexto, ArrayList<Mesa> datos) {
		this.datos = datos;
		inflater = LayoutInflater.from(contexto);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.linea_mesaview, null);
			holder = new ViewHolder();
			holder.tvNumeroMesa = (TextView) convertView
					.findViewById(R.id.txtNroMesa);
			holder.tvNumeroMesa.setTextColor(Color.BLACK);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.tvNumeroMesa.setText(datos.get(position).getTitle());
		return convertView;

	}

}
