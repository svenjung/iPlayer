package com.sven.media.iplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.sven.media.iplayer.player.PlayerActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.d("Test log tag");

        String videoPath = "/storage/emulated/0/Download/Bluetooth/S07E01.720p.mkv";
        Intent intent = PlayerActivity.newIntent(this, videoPath, "S07E01.720p.mkv");
        // startActivity(intent);

//        InputStream stream = getResources().openRawResource(
//                R.raw.s07e01);
//
//        SsaParser ssaParser = new SsaParser();
//        try {
//            ssaParser.parseStream(stream, "UTF-8");
//        } catch (ParserException e) {
//            e.printStackTrace();
//        }

        String text = "战争结束了  凛冬已至\\N{\\fn微软雅黑}{\\b0}{\\fs14}{\\3c&H202020&}{\\shad1}The war is over. Winter has come.";
        Timber.d("text : %s", text.replaceAll("\\{.*?\\}", ""));

        // SrtParser srtParser = new SrtParser();
//        try {
//            // srtParser.parseStream(getResources().openRawResource(R.raw.s7e1), "UTF-8");
//            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.srt)));
//            String line = null;
//            do {
//                line = br.readLine();
//                Timber.d("ISReader, line=%s", line);
//            } while (line != null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        hasSoftKey(getWindowManager());

        isNormalScreen(getWindowManager());

        Timber.tag("WindowSize").w("value of property : %s", Reflect.on("android.os.SystemProperties").call("get", "qemu.hw.mainkeys", "0").get());
    }

    public synchronized static boolean hasSoftKey(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        Timber.tag("WindowSize").w("realWidth:%d, realHeight:%d, displayWidth:%d, displayHeight:%d", realWidth, realHeight, displayWidth, displayHeight);

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static boolean isNormalScreen(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        Timber.tag("WindowSize").w("realWidth / realHeight = %f", 9/16F);

        Timber.tag("WindowSize").w("realWidth / realHeight = %f", (float) realWidth / (float) realHeight);
        return realWidth / (float) realHeight == 9 / 16F;
    }
}
