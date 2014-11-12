package representacion;

import android.os.Parcel;
import android.os.Parcelable;

public class Mesa implements Parcelable {

	public Mesa() {

	}

	private Mesa(Parcel source) {

		this.title = source.readString();
		this.urlDetalle = source.readString();

	}

	public static final Parcelable.Creator<Mesa> CREATOR = new Parcelable.Creator<Mesa>() {
		public Mesa createFromParcel(Parcel in) {
			return new Mesa(in);
		}

		public Mesa[] newArray(int size) {
			return new Mesa[size];
		}
	};

	private String title;
	private String urlDetalle;

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
