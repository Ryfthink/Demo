package ryf.demo.memoryleak;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class MemoryLeakView extends View {

	private byte[] mArrays;

	public MemoryLeakView(Context context) {
		super(context);
		mArrays = new byte[300];
		for (int i = 0; i < 300; i++) {
			mArrays[i] = (byte) new Random().nextInt(127);
		}
		setBackgroundColor(Color.GRAY);
	}

}
