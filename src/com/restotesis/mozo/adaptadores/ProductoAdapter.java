package com.restotesis.mozo.adaptadores;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.restotesis.mozo.R;
import com.restotesis.mozo.adaptadores.PedidoAdapter.ViewHolder;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;

public class ProductoAdapter extends BaseAdapter {

	static class ViewHolder {
		TextView tvNombreProducto;
		// TextView tvCantidad;
	}

	private static final String TAG = "CustomAdapter";
	private static int convertViewCounter = 0;
	private ArrayList<Producto> datos;
	private LayoutInflater inflater = null;

	public ProductoAdapter(Context contexto, ArrayList<Producto> datos) {

		this.datos = datos;
		inflater = LayoutInflater.from(contexto);
	}
	public void actualizar(ArrayList<Producto> datos){
		this.datos = datos;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.linea_productosview, null);
			holder = new ViewHolder();
			holder.tvNombreProducto = (TextView) convertView
					.findViewById(R.id.txtUnProducto);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.tvNombreProducto.setText(datos.get(position).getTitle());
		return convertView;

	}

}
