package pl.revo.helperutils.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class MediaPlayerOnSubscribe implements OnSubscribe<MediaPlayerAccessWrapper> {

	private final Context context;
	private final String url;
	MediaPlayerAccessWrapper mediaPlayer;

	public MediaPlayerOnSubscribe(Context context,String url){
		this.context = context;
		this.url = url;
	}
	@Override
	public void call(Subscriber<? super MediaPlayerAccessWrapper> subscriber) {

		mediaPlayer = new RadioAudioPlayer() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				super.onPrepared(mp);
				subscriber.onNext(mediaPlayer);
			}

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				subscriber.onError(new MediaPlayerException(what, extra));
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
