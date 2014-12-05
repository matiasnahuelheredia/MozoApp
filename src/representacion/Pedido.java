package representacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	public void llenarLinks(JSONObject jsonResult){
		JSONObject members;
		try {
			members = jsonResult.getJSONObject("members");
			this.setUrlPedirBebidas(setearURL("pedirBebidas", members));
			this.setUrlRemoveFromBebidas(setearURL("removeFromBebidas", members));
			this.setUrlEnviar(setearURL("enviar", members));
			this.setUrlTomarMenues(setearURL("tomarMenues", members));
			this.setUrlRemoveFromMenues(setearURL("removeFromMenues", members));
			this.setUrlPedirPlatosEntrada(setearURL("pedirPlatosEntrada",
					members));
			this.setUrlPedirPlatosPrincipales(setearURL(
					"pedirPlatosPrincipales", members));
			this.setUrlPedirGuarniciones(setearURL("pedirGuarniciones", members));
			this.setUrlPedirPostres(setearURL("pedirPostres", members));
			this.setUrlRemoveFromComanda(setearURL("removeFromComanda", members));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String setearURL(String accion, JSONObject unJSONObject) {

		try {
			JSONObject accionDo = unJSONObject.getJSONObject(accion);
			JSONArray linkDo = accionDo.getJSONArray("links");
			JSONObject arregloDo = linkDo.getJSONObject(0);
			return arregloDo.optString("href");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
