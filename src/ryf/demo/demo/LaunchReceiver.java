package ryf.demo.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ryf.demo.log.LogService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LaunchReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogService.log(context, "Demo监听到开机启动! " + action);
		File dir = new File(context.getFilesDir(), "log.txt");
		if (!dir.exists()) {
			try {
				dir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		PrintWriter out = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH时mm分ss秒");
			String txt = sdf.format(new Date()) + "   action:" + action + "\n";
			out = new PrintWriter(new BufferedWriter(new FileWriter(dir, true)));
			out.println(txt);
			out.flush();
		} catch (IOException e) {

		} finally {
			if (out != null) {
				out.close();
			}
		}

		if (Intent.ACTION_BOOT_COMPLETED.equals(action) || "com.duokan.duokanplayer.BOOT_COMPLETED".equals(action) || "android.intent.action.LETV_SCREENON".equals(action)) {
		}
	}
}
