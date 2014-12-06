package com.restotesis.mozo.representacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

public class Mesa implements Parcelable {
	
	private String title;
	private String urlDetalle;
	private String facturarURL;
	private String tomarPedidoURL;
	private String borrarPedidoURL;

	public Mesa() {

	}

	private Mesa(Parcel source) {

		this.title = source.readString();
		this.urlDetalle = source.readString();
		this.facturarURL = source.readString();
		this.tomarPedidoURL = source.readString();
		this.borrarPedidoURL = source.readString();

	}

	public static final Parcelable.Creator<Mesa> CREATOR = new Parcelable.Creator<Mesa>() {
		public Mesa createFromParcel(Parcel in) {
			return new Mesa(in);
		}

		public Mesa[] newArray(int size) {
			return new Mesa[size];
		}
	};

	public void llenarLinks(JSONObject jsonResult){		
		try {
			JSONObject members = jsonResult.getJSONObject("members");
			JSONObject facturar = members.getJSONObject("facturar");
			JSONArray links = facturar.getJSONArray("links");	
			this.setFacturarURL(links.getJSONObject(0).getString("href"));
			JSONObject tomarPedido = members.getJSONObject("tomarPedido");
			links = tomarPedido.getJSONArray("links");
			this.setTomarPedidoURL(links.getJSONObject(0).getString("href"));
			JSONObject borrarPedido = members.getJSONObject("borrarPedido");
			links = borrarPedido.getJSONArray("links");
			this.setBorrarPedidoURL(links.getJSONObject(0).getString("href"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlDetalle() {
		return urlDetalle;
	}

	public void setUrlDetalle(String urlDetalle) {
		this.urlDetalle = urlDetalle;
	}
	

	public String getFacturarURL() {
		return facturarURL;
	}

	public void setFacturarURL(String facturarURL) {
		this.facturarURL = facturarURL;
	}

	public String getTomarPedidoURL() {
		return tomarPedidoURL;
	}

	public void setTomarPedidoURL(String tomarPedidoURL) {
		this.tomarPedidoURL = tomarPedidoURL;
	}

	public String getBorrarPedidoURL() {
		return borrarPedidoURL;
	}

	public void setBorrarPedidoURL(String borrarPedidoURL) {
		this.borrarPedidoURL = borrarPedidoURL;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(title);
		dest.writeString(urlDetalle);
		dest.writeString(facturarURL);
		dest.writeString(tomarPedidoURL);
		dest.writeString(borrarPedidoURL);

	}

}
