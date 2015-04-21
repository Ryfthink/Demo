package ryf.demo.customattr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import ryf.demo.R;

public class CustomView extends TextView {

	public CustomView(final Context context) {
		this(context, null);

	}

	public CustomView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public CustomView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.style1, defStyle, defStyle);

		Drawable bg = typedArray.getDrawable(R.styleable.style1_attr_x);
		typedArray.recycle();
		setBackgroundDrawable(bg);

		final TypedArray typedArray3 = context.obtainStyledAttributes(attrs, R.styleable.style3, defStyle, defStyle);
		String str = typedArray3.getString(R.styleable.style3_attr_str);
		typedArray3.recycle();

		setText(str);

	}

}
