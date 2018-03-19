package com.sven.media.iplayer.player;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.sven.media.iplayer.R;
import com.sven.media.iplayer.player.widget.IPlayerVideoView;
import com.sven.media.iplayer.player.widget.MediaController;

import timber.log.Timber;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private String mVideoPath;
    private Uri mVideoUri;

    private IPlayerVideoView mVideoView;

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoTitle", videoTitle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);

        mVideoView = findViewById(R.id.video_view);

        mVideoView.setMediaController(new MediaController(this));

        mVideoPath = getIntent().getStringExtra("videoPath");

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)) {
            if (intentAction.equals(Intent.ACTION_VIEW)) {
                mVideoPath = intent.getDataString();
            } else if (intentAction.equals(Intent.ACTION_SEND)) {
                mVideoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    String scheme = mVideoUri.getScheme();
                    if (TextUtils.isEmpty(scheme)) {
                        Timber.e("Null unknown scheme\n");
                        finish();
                        return;
                    }
                    if (scheme.equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
                        mVideoPath = mVideoUri.getPath();
                    } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                        Timber.e("Can not resolve content below Android-ICS\n");
                        finish();
                        return;
                    } else {
                        Timber.e("Unknown scheme " + scheme + "\n");
                        finish();
                        return;
                    }
                }
            }
        }

        // auto start playback when video prepared
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mVideoView.start();

                setFrameAtTime((IjkMediaPlayer) mp);
            }
        });

        // mVideoView.setHudView(mHudView);
        // prefer mVideoPath
        if (mVideoPath != null) {
            mVideoView.setVideoPath(mVideoPath);
        } else if (mVideoUri != null) {
            mVideoView.setVideoURI(mVideoUri);
        } else {
            Timber.e("Null Data Source\n");
            finish();
            return;
        }

        // mVideoView.addSubtitleSource("/storage/emulated/0/Movies/Game.of.Thrones.S07E01.Dragonstone.720p.AMZN.WEB-DL.DDP5.1.H.264-GoT.ass");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setFrameAtTime(IjkMediaPlayer mediaPlayer) {
        mediaPlayer.setFrameAtTime("/storage/emulated/0/Movies/get_frame.jpg", 5000, 6000, 1, 2);
    }
}
