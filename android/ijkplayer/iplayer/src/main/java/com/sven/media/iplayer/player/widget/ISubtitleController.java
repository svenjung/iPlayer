package com.sven.media.iplayer.player.widget;

import com.sven.media.iplayer.player.subtitles.TimedTextObject;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Sven.J on 18-3-12.
 */

public interface ISubtitleController {

    TimedTextObject getTimedText();

    void onTimedText(CharSequence text);

    IMediaPlayer getMediaPlayer();
}
