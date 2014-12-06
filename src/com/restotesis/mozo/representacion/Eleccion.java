package com.restotesis.mozo.representacion;

import android.os.Parcel;
import android.os.Parcelable;

public class Eleccion implements Parcelable{
	private String title;
	private String url;
	private int cantidad;
	private String nota;
	private String tipo;
	private String invokeURL;
	
	
	public Eleccion() {

	}

	private Eleccion(Parcel source) {

		this.title = source.readString();
		this.url = source.readString();
		this.cantidad = source.readInt();
		this.nota = source.readString();
		this.tipo = source.readString();
		this.invokeURL = source.readString();

	}

	public static final Parcelable.Creator<Eleccion> CREATOR = new Parcelable.Creator<Eleccion>() {
		public Eleccion createFromParcel(Parcel in) {
			return new Eleccion(in);
		}

		public Eleccion[] newArray(int size) {
			return new Eleccion[size];
		}
	};
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
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
		dest.writeString(url);
		dest.writeInt(cantidad);
		dest.writeString(nota);
		dest.writeString(tipo);
		dest.writeString(invokeURL);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getInvokeURL() {
		return invokeURL;
	}

	public void setInvokeURL(String invokeURL) {
		this.invokeURL = invokeURL;
	}

}
