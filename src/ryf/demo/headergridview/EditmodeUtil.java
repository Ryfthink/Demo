package ryf.demo.headergridview;

import java.util.Random;

import ryf.demo.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EditmodeUtil {

	public static void testSFGridView(final SFGridView mGridView) {
		mGridView.setHorizontalSpacing(40);
		mGridView.setVerticalSpacing(40);
		mGridView.setColumnWidth(404);
		mGridView.setRowHeight(236);
		mGridView.setNumColumns(4);
		mGridView.setOffset(300, 0);
		mGridView.setFocusOffest(0);
		mGridView.setIncludeAnimScale(false);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setOverScrollMode(SFGridView.OVER_SCROLL_BOTTOM);

		BaseAdapter mAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = new View(mGridView.getContext());
				view.setBackgroundColor(0xff << 24 | new Random().nextInt(0xffffff));
				view.setBackgroundResource(R.drawable.item);
				return view;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 30;
			}
		};

		mGridView.setAdapter(mAdapter);
	}

}
