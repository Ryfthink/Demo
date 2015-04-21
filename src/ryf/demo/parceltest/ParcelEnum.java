package ryf.demo.parceltest;

import android.os.Parcel;
import android.os.Parcelable;

public enum ParcelEnum implements Parcelable {
	APPLE, BANANA, ORANGE;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
	}

	public static final Parcelable.Creator<ParcelEnum> CREATOR = new Creator<ParcelEnum>() {

		@Override
		public ParcelEnum[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ParcelEnum[size];
		}

		@Override
		public ParcelEnum createFromParcel(Parcel source) {
			int ordinal = source.readInt();
			for (ParcelEnum parcel : ParcelEnum.values()) {
				if (ordinal == parcel.ordinal()) {
					return parcel;
				}
			}
			return null;
		}
	};

}
