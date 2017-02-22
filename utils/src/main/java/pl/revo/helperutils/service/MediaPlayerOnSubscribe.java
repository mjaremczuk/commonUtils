package pl.revo.helperutils.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class MediaPlayerOnSubscribe implements ObservableOnSubscribe<MediaPlayerAccessWrapper> {

	private final Context context;
	private final String url;
	MediaPlayerAccessWrapper mediaPlayer;

	public MediaPlayerOnSubscribe(Context context,String url){
		this.context = context;
		this.url = url;
	}

	@Override
	public void subscribe(ObservableEmitter<MediaPlayerAccessWrapper> e) throws Exception {
		mediaPlayer = new RadioAudioPlayer() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				super.onPrepared(mp);
				e.onNext(mediaPlayer);
			}

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				e.onError(new MediaPlayerException(what, extra));
				return super.onError(mp, what, extra);
			}
		};
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
		mediaPlayer.setDataSource(url);
		mediaPlayer.prepareAsync();
	}

	public class MediaPlayerException extends Throwable {

		public final int what;
		public final int extra;

		public MediaPlayerException(int what, int extra) {
			this.what = what;
			this.extra = extra;
		}
	}
}
