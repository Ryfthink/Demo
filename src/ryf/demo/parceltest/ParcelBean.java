package ryf.demo.parceltest;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelBean implements Parcelable {
	String name;
	int age;
	int color;

	public ParcelBean(String name, int age, int color) {
		this.name = name;
		this.age = age;
		this.color = color;
	}

	@Override
	public String toString() {
		return name + "  " + age + "   " + color;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(age);
		dest.writeInt(color);
	}

	public static final Parcelable.Creator<ParcelBean> CREATOR = new Creator<ParcelBean>() {

		@Override
		public ParcelBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ParcelBean[size];
		}

		@Override
		public ParcelBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ParcelBean(source.readString(), source.readInt(), source.readInt());
		}
	};

}
