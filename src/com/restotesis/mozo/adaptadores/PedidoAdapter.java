package com.restotesis.mozo.adaptadores;

import java.util.ArrayList;
import com.restotesis.mozo.R;
import com.restotesis.mozo.representacion.Pedido;
import com.restotesis.mozo.representacion.Producto;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PedidoAdapter extends BaseAdapter {

	static class ViewHolder {
		TextView tvNumeroPedido;
		TextView tvEstado;
	}

	private static final String TAG = "CustomAdapter";
	private static int convertViewCounter = 0;
	private ArrayList<Pedido> datos;
	private LayoutInflater inflater = null;

	public PedidoAdapter(Context contexto, ArrayList<Pedido> datos) {

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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.linea_pedidosview, null);
			holder = new ViewHolder();
			holder.tvNumeroPedido = (TextView) convertView
					.findViewById(R.id.tvNumeroPedido);
			holder.tvEstado = (TextView) convertView
					.findViewById(R.id.tvEstado);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.tvNumeroPedido.setText(datos.get(position).getTitle());
		return convertView;

	}
	public void actualizar(ArrayList<Pedido> datos){
		this.datos = datos;
	}

}
