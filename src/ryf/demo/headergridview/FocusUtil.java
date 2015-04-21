package ryf.demo.headergridview;

import java.util.ArrayList;
import java.util.List;

import ryf.demo.R;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewParent;

public class FocusUtil {
	
	public static Drawable getDefaultFocusedBackground(Resources res) {
		return res.getDrawable(R.drawable.shafa_setting_focus);
	}
	
	public static IParent getParent(View v) {
		IParent parent = null;
		
		if (v != null) {
			for (ViewParent p = v.getParent(); p instanceof View; p = p.getParent()) {
				if (p instanceof IParent) {
					parent = (IParent) p;
					break;
				}
			}
		}
		
		return parent;
	}
	
	public static Rect getFocusedRectByView(View v) {
		Rect retVal = null;
		
		if (v != null) {
			int left = v.getLeft() - v.getScrollX();
			int top = v.getTop() - v.getScrollY();
			
			for (ViewParent p = v.getParent(); (!(p instanceof IParent)); p = p.getParent()) {
				if(p == null) break;
				
				left += ((View) p).getLeft() - ((View) p).getScrollX();
				top  += ((View) p).getTop() - ((View) p).getScrollY();
				
				if (p instanceof IParent) {
					break;
				}
			}
			
			retVal = new Rect(left, top, left + v.getWidth(), top + v.getHeight());
		}
		
		return retVal;
	}
	
	public static Rect scaleRect(Rect rect, float scale) {
		if (rect != null && !rect.isEmpty()) {
			int dx = (int) (rect.width() * (scale - 1) / 2);
			int dy = (int) (rect.height() * (scale - 1) / 2);
			
			rect.left -= dx;
			rect.top -= dy;
			rect.right += dx;
			rect.bottom += dy;
		}
		
		return rect;
	}
	
	public static void initNextFocusLeftAndRightIDs(List<View> views, boolean isRecyle) {
		if(views == null || views.size() == 0) return;
		
		List<View> list = new ArrayList<View>();
		for(View view : views){
			if(view != null && view.isEnabled()) list.add(view);
		}
		View[] enableViews = list.toArray(new View[list.size()]);
		
		for(int i=0;i<enableViews.length;i++){
			View thisview = enableViews[i];
			View nextView = null;
			if(i < enableViews.length - 1) nextView = enableViews[i+1];
			View preView = null;
			if(i > 0) preView = enableViews[i-1];
			
			thisview.setNextFocusLeftId(preView==null?thisview.getId():preView.getId());
			thisview.setNextFocusRightId(nextView==null?thisview.getId():nextView.getId());
		}
		
		if(isRecyle){
			if(enableViews[0] != null && enableViews[enableViews.length-1] != null) {
				enableViews[0].setNextFocusLeftId(enableViews[enableViews.length-1].getId());
				enableViews[enableViews.length-1].setNextFocusRightId(enableViews[0].getId());
			}
		}
	}

}
