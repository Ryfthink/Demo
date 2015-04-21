package ryf.demo.animator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setPadding(int padding) {
		super.setPadding(padding, padding, padding, padding);
	}
}
