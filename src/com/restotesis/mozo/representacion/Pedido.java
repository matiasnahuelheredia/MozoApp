package com.restotesis.mozo.representacion;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class Pedido implements Parcelable {
	private String title;
	private String urlDetalle;
	private String urlPedirBebidas;
	private String urlRemoveFromBebidas;
	private String urlEnviar;
	private String urlTomarMenues;
	private String urlRemoveFromMenues;
	private String urlPedirPlatosEntrada;
	private String urlPedirPlatosPrincipales;
	private String urlPedirGuarniciones;
	private String urlPedirPostres;
	private String urlRemoveFromComanda;
	private String urlPedirOferta;
	private String urlRemoveFromOferta;
	private ArrayList<Producto> listaProductos = new ArrayList<Producto>();

	public Pedido() {
		super();
	}

	private Pedido(Parcel source) {

		this.title = source.readString();
		this.urlDetalle = source.readString();
		this.urlPedirBebidas = source.readString();
		this.urlRemoveFromBebidas = source.readString();
		this.urlEnviar = source.readString();
		this.urlTomarMenues = source.readString();
		this.urlRemoveFromMenues = source.readString();
		this.urlPedirPlatosEntrada = source.readString();
		this.urlPedirPlatosPrincipales = source.readString();
		this.urlPedirGuarniciones = source.readString();
		this.urlPedirPostres = source.readString();
		this.urlRemoveFromComanda = source.readString();
		this.urlPedirOferta = source.readString();
		this.urlRemoveFromOferta = source.readString();
		listaProductos = new ArrayList<Producto>();
		source.readTypedList(listaProductos, Producto.CREATOR);

	}

	public static final Parcelable.Creator<Pedido> CREATOR = new Parcelable.Creator<Pedido>() {
		public Pedido createFromParcel(Parcel in) {
			return new Pedido(in);
		}

		public Pedido[] newArray(int size) {
			return new Pedido[size];
		}
	};

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
		dest.writeString(urlPedirBebidas);
		dest.writeString(urlRemoveFromBebidas);
		dest.writeString(urlEnviar);
		dest.writeString(urlTomarMenues);
		dest.writeString(urlRemoveFromMenues);
		dest.writeString(urlPedirPlatosEntrada);
		dest.writeString(urlPedirPlatosPrincipales);
		dest.writeString(urlPedirGuarniciones);
		dest.writeString(urlPedirPostres);
		dest.writeString(urlRemoveFromComanda);
		dest.writeString(urlPedirOferta);
		dest.writeString(urlRemoveFromOferta);
		dest.writeTypedList(listaProductos);

	}

	public ArrayList<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(ArrayList<Producto> listaProductos) {
		this.listaProductos = listaProductos;
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

	public String getUrlPedirBebidas() {
		return urlPedirBebidas;
	}

	public void setUrlPedirBebidas(String urlPedirBebidas) {
		this.urlPedirBebidas = urlPedirBebidas;
	}

	public String getUrlRemoveFromBebidas() {
		return urlRemoveFromBebidas;
	}

	public void setUrlRemoveFromBebidas(String urlRemoveFromBebidas) {
		this.urlRemoveFromBebidas = urlRemoveFromBebidas;
	}

	public String getUrlEnviar() {
		return urlEnviar;
	}

	public void setUrlEnviar(String urlEnviar) {
		this.urlEnviar = urlEnviar;
	}

	public String getUrlTomarMenues() {
		return urlTomarMenues;
	}

	public void setUrlTomarMenues(String urlTomarMenues) {
		this.urlTomarMenues = urlTomarMenues;
	}

	public String getUrlRemoveFromMenues() {
		return urlRemoveFromMenues;
	}

	public void setUrlRemoveFromMenues(String urlRemoveFromMenues) {
		this.urlRemoveFromMenues = urlRemoveFromMenues;
	}

	public String getUrlPedirPlatosEntrada() {
		return urlPedirPlatosEntrada;
	}

	public void setUrlPedirPlatosEntrada(String urlPedirPlatosEntrada) {
		this.urlPedirPlatosEntrada = urlPedirPlatosEntrada;
	}

	public String getUrlPedirPlatosPrincipales() {
		return urlPedirPlatosPrincipales;
	}

	public void setUrlPedirPlatosPrincipales(String urlPedirPlatosPrincipales) {
		this.urlPedirPlatosPrincipales = urlPedirPlatosPrincipales;
	}

	public String getUrlPedirGuarniciones() {
		return urlPedirGuarniciones;
	}

	public void setUrlPedirGuarniciones(String urlPedirGuarniciones) {
		this.urlPedirGuarniciones = urlPedirGuarniciones;
	}

	public String getUrlPedirPostres() {
		return urlPedirPostres;
	}

	public void setUrlPedirPostres(String urlPedirPostres) {
		this.urlPedirPostres = urlPedirPostres;
	}

	public String getUrlRemoveFromComanda() {
		return urlRemoveFromComanda;
	}

	public void setUrlRemoveFromComanda(String urlRemoveFromComanda) {
		this.urlRemoveFromComanda = urlRemoveFromComanda;
	}

	public String getUrlPedirOferta() {
		return urlPedirOferta;
	}

	public void setUrlPedirOferta(String urlPedirOferta) {
		this.urlPedirOferta = urlPedirOferta;
	}

	public String getUrlRemoveFromOferta() {
		return urlRemoveFromOferta;
	}

	public void setUrlRemoveFromOferta(String urlRemoveFromOferta) {
		this.urlRemoveFromOferta = urlRemoveFromOferta;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
