package ryf.demo.headergridview;

import java.util.Random;

import ryf.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SFGridViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sfgridview);
		test();

	}

	private void test() {
		final SFGridView mGridView = (SFGridView) findViewById(R.id.sfGridView);
		mGridView.setHorizontalSpacing(40);
		mGridView.setVerticalSpacing(40);
		mGridView.setColumnWidth(404);
		mGridView.setRowHeight(236);
		mGridView.setNumColumns(4);
		mGridView.setOffset(300, 300);
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
		// mGridView.setOnOutOfEdgeListenerr(mOnOutOfEdgeListener);
		// mGridView.setOnItemLongClickListener(mOnItemLongClickListener);
		// mGridView.setOnItemClickListener(mOnItemClickListener);
		// mGridView.setOnItemMenuClickListener(mOnItemMenuClickListener);
		// mGridView.setFocusDrawable(super.getThemeNinePatchDrawable("theme_simple_focus.9.png"));
	}

}
