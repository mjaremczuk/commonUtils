package pl.revo.helperutils.service;

import android.media.MediaPlayer;

public class RadioAudioPlayer extends MediaPlayerAccessWrapper {

	@Override
	public void onPrepared(MediaPlayer mp) {

	}

	@Override
	public void onCompletion(MediaPlayer mp) {

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}
}
