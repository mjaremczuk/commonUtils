package pl.revo.helperutils.presenter;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Handler;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.net.MalformedURLException;
import javax.inject.Inject;
import pl.revo.helperutils.service.MediaPlayerAccessWrapper;
import pl.revo.helperutils.service.MediaPlayerOnSubscribe;
import pl.revo.helperutils.utils.StreamDataRetriever;


public class RadioPresenter implements OnAudioFocusChangeListener {

	public static final int AUDIOFOCUS_GAIN = 1;
	public static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
	public static final int AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4;
	public static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
	public static final int AUDIOFOCUS_LOSS = -1;
	public static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
	public static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
	public static final int AUDIOFOCUS_REQUEST_FAILED = 0;

	MediaPlayerAccessWrapper mediaPlayer;
	AudioManager mAudioManager;
	String url;
	int defaultVolume;
	Observable<String> dataFetcher;
	Handler metaDataRetrieverHandler;


	@Inject
	public RadioPresenter() {

	}

	private boolean initAudioManager(Context context) {
		if (mAudioManager == null) {
			mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) ==
					AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
		}
		else {
			return true;
		}
	}

	private void startPlayback(Context context, String url) {
		if (this.url == null || !this.url.equalsIgnoreCase(url)) {
			this.url = url;
			if(metaDataRetrieverHandler != null){
				metaDataRetrieverHandler.removeCallbacks(this::runnableMetadataRetriever);
			}
			metaDataRetrieverHandler = new Handler();
			Observable.create(new MediaPlayerOnSubscribe(context, url))
					.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
					.subscribe(this::handleSuccessMediaPlayerInitialize, this::handleError);
			metaDataRetrieverHandler.post(this::runnableMetadataRetriever);
//			fetch stream data
		}
		else {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				pause();
			}
			else {
				resume();
			}
		}
	}

	private void runnableMetadataRetriever() {
		StreamDataRetriever retriever = new StreamDataRetriever();
		try {
			dataFetcher = retriever.fetchStreamData(url);
			dataFetcher.subscribe(result ->
					Log.d("Stream", "fetchStreamData: " + result));
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		metaDataRetrieverHandler.postDelayed(this::runnableMetadataRetriever, 5000);
	}


	public void startRadioPlayer(Context context, String url) {
		boolean granted = initAudioManager(context);
		if (granted) {
			startPlayback(context, url);
		}
	}

	public void handleSuccessMediaPlayerInitialize(MediaPlayerAccessWrapper mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		resume();
	}

	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void resume() {
		if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
			case AUDIOFOCUS_LOSS_TRANSIENT:
				if (mediaPlayer != null) {
					mediaPlayer.pause();
				}
				break;
			case AUDIOFOCUS_LOSS:
				mAudioManager.abandonAudioFocus(this);
				break;
			case AUDIOFOCUS_GAIN:
				if (mediaPlayer != null) {
					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}
				}
				mAudioManager
						.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolume, AudioManager.FLAG_PLAY_SOUND);
				break;
			case AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				defaultVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				int duckVolume = (int) (mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 0.3);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, duckVolume, AudioManager.FLAG_PLAY_SOUND);
				break;
		}
	}

	public void handleError(Throwable throwable) {

	}
}
