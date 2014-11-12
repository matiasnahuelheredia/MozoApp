package representacion;

import android.os.Parcel;
import android.os.Parcelable;

public class Pedido implements Parcelable {
	private String title;
	private String urlDetalle;

	public Pedido() {
		super();
	}

	private Pedido(Parcel source) {

		this.title = source.readString();
		this.urlDetalle = source.readString();

	}

	public static final Parcelable.Creator<Pedido> CREATOR = new Parcelable.Creator<Pedido>() {
		public Pedido createFromParcel(Parcel in) {
			return new Pedido(in);
		}

		public Pedido[] newArray(int size) {
			return new Pedido[size];
		}
	};

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

	}

}
