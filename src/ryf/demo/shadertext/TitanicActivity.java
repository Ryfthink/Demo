package ryf.demo.shadertext;

import android.app.Activity;
import android.os.Bundle;

import ryf.demo.R;

public class TitanicActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_titanic);

        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

        // start animation
        new Titanic().start(tv);
	}
}
