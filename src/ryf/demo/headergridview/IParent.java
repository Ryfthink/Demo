package ryf.demo.headergridview;

import android.graphics.Rect;

public interface IParent {
	
	public void notifyFocusChange(boolean focused, IFocusable item, Rect rect);
	
	public Rect getCurrentRect();

}
