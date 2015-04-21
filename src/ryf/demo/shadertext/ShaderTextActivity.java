package ryf.demo.shadertext;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import ryf.demo.R;

public class ShaderTextActivity extends Activity {
	TextViewEx txt;
	BitmapShader textShader;
	private Matrix shaderMatrix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		txt = new TextViewEx(this);

		txt.setPadding(50, 0, 50, 0);

		// textShader = new LinearGradient(0, 0, 100, 100, new int[] {
		// Color.RED, Color.RED, Color.GREEN }, null, TileMode.CLAMP);

		Drawable wave = getResources().getDrawable(R.drawable.e);

		int waveW = wave.getIntrinsicWidth();
		int waveH = wave.getIntrinsicHeight();

		Bitmap b = Bitmap.createBitmap(waveW, waveH, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);

		c.drawColor(Color.MAGENTA);

		wave.setBounds(0, 0, waveW, waveH);
		wave.draw(c);

		textShader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

		// textShader = new
		shaderMatrix = new Matrix();

		txt.getPaint().setShader(textShader);
		txt.setText("See I never thought that I could walk through fire " + "\nI never thought that I could take the burn" + "\nI never had the strength to take it higher"
				+ "\nUntil I reached the point of no return" + "\nAnd there's just no turning back" + "\nWhen your hearts under attack" + "\nGonna give everything I have" + "\nIt's my destiny"
				+ "\nI will never say never! (I will fight)");
		txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);
		txt.setTextColor(Color.BLUE);
		txt.setGravity(Gravity.CENTER);
		txt.setBackgroundColor(Color.DKGRAY);
		txt.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Satisfy-Regular.ttf"));
		setContentView(txt);

		ObjectAnimator animator = ObjectAnimator.ofInt(txt, "Y", 700, -150);
		animator.setDuration(15000);
		animator.setRepeatMode(ObjectAnimator.REVERSE);
		animator.setInterpolator(new LinearInterpolator());
		animator.setRepeatCount(ObjectAnimator.INFINITE);

		ObjectAnimator maskXAnimator = ObjectAnimator.ofInt(txt, "X", 0, 200);
		maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
		maskXAnimator.setDuration(1500);
		maskXAnimator.setInterpolator(new LinearInterpolator());

		AnimatorSet set = new AnimatorSet();
		set.playTogether(animator, maskXAnimator);
		set.start();
	}

	class TextViewEx extends TextView {

		public TextViewEx(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			shaderMatrix.setTranslate(maskX, maskY);
			textShader.setLocalMatrix(shaderMatrix);
			super.onDraw(canvas);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			super.onLayout(changed, left, top, right, bottom);
		}

		int maskX = 0;

		int maskY = 0;

		void setY(int factor) {
			maskY = factor;

			// invalidate();
		}

		void setX(int factor) {
			maskX = factor;

			// shaderMatrix.setTranslate(maskX, 400 + 0);
			//
			// // assign matrix to invalidate the shader
			// textShader.setLocalMatrix(shaderMatrix);

			invalidate();
		}

	}
}
