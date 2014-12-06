package com.restotesis.mozo.adaptadores;

import java.util.ArrayList;
import com.restotesis.mozo.R;
import com.restotesis.mozo.representacion.Eleccion;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EleccionAdapter extends BaseAdapter {
	
	static class ViewHolder {
		TextView tvChoice;

	}

	private static final String TAG = "CustomAdapter";
	private static int convertViewCounter = 0;
	private ArrayList<Eleccion> datos;
	private LayoutInflater inflater = null;

	public EleccionAdapter(Context contexto, ArrayList<Eleccion> datos) {
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
			convertView = inflater.inflate(R.layout.linea_eleccionview, null);
			holder = new ViewHolder();
			holder.tvChoice = (TextView) convertView
					.findViewById(R.id.txtUnaEleccion);
			holder.tvChoice.setTextColor(Color.BLACK);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.tvChoice.setText(datos.get(position).getTitle());
		return convertView;
	}

}
