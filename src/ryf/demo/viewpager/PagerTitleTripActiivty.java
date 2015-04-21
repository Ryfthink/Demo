package ryf.demo.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ryf.demo.R;

public class PagerTitleTripActiivty extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagertitltetrip);

		ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new MyPagerAdapter());

		ViewPager mViewPager2 = (ViewPager) findViewById(R.id.viewpager2);
		mViewPager2.setAdapter(new MyPagerAdapter());
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView txt = new TextView(PagerTitleTripActiivty.this);
			txt.setTextSize(100);
			txt.setGravity(Gravity.CENTER);
			txt.setText("Page" + position);
			container.addView(txt);
			return txt;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		};

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Title " + position;
		};
	};
}
