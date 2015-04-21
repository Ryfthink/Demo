package ryf.demo.headergridview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public interface IFocusable {
	
	public void onFocusChanged(boolean focused, int dx, int dy);
	
	public Rect getSelectedRect();
	
	public Drawable getFocusedBackground();
	
}
