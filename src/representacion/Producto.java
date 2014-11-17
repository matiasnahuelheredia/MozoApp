package representacion;

import android.os.Parcel;
import android.os.Parcelable;

public class Producto implements Parcelable {

	String title;
	int cantidad;
	String urlDetalle;

	public Producto() {

	}

	private Producto(Parcel source) {

		this.title = source.readString();
		this.cantidad = source.readInt();
		this.urlDetalle = source.readString();

	}

	public static final Parcelable.Creator<Producto> CREATOR = new Parcelable.Creator<Producto>() {
		public Producto createFromParcel(Parcel in) {
			return new Producto(in);
		}

		public Producto[] newArray(int size) {
			return new Producto[size];
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
		dest.writeInt(cantidad);
		dest.writeString(urlDetalle);
		

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getUrlDetalle() {
		return urlDetalle;
	}

	public void setUrlDetalle(String urlDetalle) {
		this.urlDetalle = urlDetalle;
	}

}
