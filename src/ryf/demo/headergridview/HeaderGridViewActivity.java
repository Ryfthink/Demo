package ryf.demo.headergridview;

import ryf.demo.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HeaderGridViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_headergridview);

		HeaderGridView gridView = (HeaderGridView) findViewById(R.id.headerView);
		gridView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.activity_headergridview_header, null));
		gridView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return LayoutInflater.from(HeaderGridViewActivity.this).inflate(R.layout.activity_headergridview_item, null);
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
				return 62;
			}
		});

	}
}
