package ryf.demo.parceltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityPacelTest1 extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button btn = new Button(this);
		btn.setText("click");
		btn.setOnClickListener(this);
		setContentView(btn);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ActivityPacelTest2.class);
		Bundle extras = new Bundle();
		extras.putParcelable("parcelBean", new ParcelBean("lalallalala", 123, 456));
		extras.putParcelable("parcelEnum", ParcelEnum.BANANA);
		intent.putExtras(extras);
		startActivity(intent);
	}
}
