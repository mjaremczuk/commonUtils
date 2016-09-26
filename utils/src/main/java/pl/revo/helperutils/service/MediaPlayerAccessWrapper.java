package pl.revo.helperutils.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public abstract class MediaPlayerAccessWrapper {

	private MediaPlayer mPlayer;
	private State currentState;
	private MediaPlayerAccessWrapper mWrapper;
	boolean shouldCancel = false;

	public MediaPlayerAccessWrapper() {
		mWrapper = this;
		mPlayer = new MediaPlayer();
		currentState = State.IDLE;
		MediaPlayer.OnPreparedListener mOnPreparedListener = mp -> {
			currentState = State.PREPARED;
			mWrapper.onPrepared(mp);
			mPlayer.start();
			currentState = State.STARTED;
		};
		mPlayer.setOnPreparedListener(mOnPreparedListener);
		MediaPlayer.OnCompletionListener mOnCompletionListener = mp -> {
			currentState = State.PLAYBACK_COMPLETE;
			mWrapper.onCompletion(mp);
		};
		mPlayer.setOnCompletionListener(mOnCompletionListener);
		MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = (mp, percent) -> {
//				Log.d("on buffering update " + percent);
			mWrapper.onBufferingUpdate(mp, percent);
		};
		mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
		MediaPlayer.OnErrorListener mOnErrorListener = (mp, what, extra) -> {
			currentState = State.ERROR;
			mWrapper.onError(mp, what, extra);
			Log.d("wrapper", "MediaPlayerAccessWrapper: error"+what );
			return false;
		};
		mPlayer.setOnErrorListener(mOnErrorListener);
		MediaPlayer.OnInfoListener mOnInfoListener = (mp, what, extra) -> {
			mWrapper.onInfo(mp, what, extra);
			return false;
		};
		mPlayer.setOnInfoListener(mOnInfoListener);

	}

	/**
	 * Enum describing current state of media player
	 */
	public static enum State {
		IDLE, ERROR, INITIALIZED, PREPARING, PREPARED, STARTED, STOPPED, PLAYBACK_COMPLETE, PAUSED
	}

	@SuppressWarnings("unused")
	public void setAudioStreamType(int streamType) {
		if (currentState == State.IDLE) {
			try {
				mPlayer.setAudioStreamType(streamType);
			}
			catch (IllegalArgumentException | IllegalStateException e) {
			}
		}
		else {
//			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unused")
	public void setWakeMode(Context context, int wakeMode) {
		if (currentState == State.IDLE) {
			try {
				mPlayer.setWakeMode(context, wakeMode);
			}
			catch (IllegalArgumentException | IllegalStateException e) {
			}
		}
	}

	@SuppressWarnings("unused")
	public void setDataSource(String path) {
		if (currentState == State.IDLE) {
			try {
				Map<String,String> map = new HashMap<>();
				map.put("Icy-MetaData", "1");
				mPlayer.setDataSource(path);
				currentState = State.INITIALIZED;
			}
			catch (IllegalArgumentException | IllegalStateException | IOException e) {
			}
		}
	}

	public void prepareAsync() {
		if (EnumSet.of(State.INITIALIZED, State.STOPPED).contains(currentState)) {
			mPlayer.prepareAsync();
			currentState = State.PREPARING;
		}
	}

	@SuppressWarnings("unused")
	public boolean isPlaying() {
		if (currentState != State.ERROR) {
			return mPlayer.isPlaying();
		}
		return false;
	}

	@SuppressWarnings("unused")
	public void seekTo(int msec) {
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.seekTo(msec);
			getState();
		}
	}

	/**
	 * Pauses the player
	 */
	@SuppressWarnings("unused")
	public void pause() {
		if (EnumSet.of(State.STARTED, State.PAUSED).contains(currentState)) {
			mPlayer.pause();
			currentState = State.PAUSED;
			getState();
		}
	}

	/**
	 * Starts the player
	 */
	@SuppressWarnings("unused")
	public void start() {
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.start();
			currentState = State.STARTED;
			getState();
		}
	}

	/**
	 * Stops the player
	 */
	@SuppressWarnings("unused")
	public void stop() {
		shouldCancel = true;
		if (EnumSet.of(
				State.PREPARED, State.STARTED, State.STOPPED, State.PAUSED, State.PLAYBACK_COMPLETE).contains(currentState)) {
			mPlayer.stop();
			currentState = State.STOPPED;
			getState();
		}
	}

	/**
	 * Resets the player
	 */
	@SuppressWarnings("unused")
	public void reset() {
		mPlayer.reset();
		currentState = State.IDLE;
		getState();
	}

	/**
	 * @return The current state of the mediaplayer state machine.
	 */
	@SuppressWarnings("unused")
	public State getState() {
		return currentState;
	}

	/**
	 * Releases resources of the player
	 */
	@SuppressWarnings("unused")
	public void release() {
		mPlayer.release();
		getState();
	}

	/**
	 * Called when mediaplayer is prepared
	 *
	 * @param mp MediaPlayer object
	 */
	@SuppressWarnings("unused")
	public abstract void onPrepared(MediaPlayer mp);

	/**
	 * Called when mediaplayer goes to completed state
	 *
	 * @param mp MediaPlayer object
	 */
	@SuppressWarnings("unused")
	public abstract void onCompletion(MediaPlayer mp);

	/**
	 * Called when buffering updates
	 *
	 * @param mp MediaPlayer object
	 * @param percent percent buffered (useless for streams)
	 */
	@SuppressWarnings("unused")
	public abstract void onBufferingUpdate(MediaPlayer mp, int percent);

	/**
	 * Called on error during an asynchronous operation (other errors will throw exceptions at method call time).
	 *
	 * @param mp MediaPlayer object
	 * @param what the type of error that has occurred {@see android.media.MediaPlayer.OnErrorListener#onError(android.media.MediaPlayer,
	 *int, int)}
	 * @param extra extra code
	 *
	 * @return True if the method handled the error, false if it didn't.
	 */
	@SuppressWarnings("unused")
	public abstract boolean onError(MediaPlayer mp, int what, int extra);

	/**
	 * Invoked to communicate some info and/or warning about the media or its playback.
	 * {@see android.media.MediaPlayer.OnInfoListener#onInfo(android.media.MediaPlayer, int, int)}
	 *
	 * @param mp MediaPlayer object
	 * @param what the type of info or warning
	 * @param extra an extra code, specific to the info. Typically implementation dependent.
	 *
	 * @return True if the method handled the info, false if it didn't.
	 */
	@SuppressWarnings("unused")
	public abstract boolean onInfo(MediaPlayer mp, int what, int extra);

	/* OTHER STUFF */
	@SuppressWarnings("unused")
	public int getCurrentPosition() {
		if (currentState != State.ERROR) {
//            Log.d("media player current POSITION: "+ mPlayer.getCurrentPosition() + " TOTAL LENGTH: "+ mPlayer.getDuration());
			return mPlayer.getCurrentPosition();
		}
		else {
			return 0;
		}
	}

	@SuppressWarnings("unused")
	public int getDuration() {
// Prepared, Started, Paused, Stopped, PlaybackCompleted
		if (EnumSet.of(State.PREPARED, State.STARTED, State.PAUSED, State.STOPPED, State.PLAYBACK_COMPLETE).contains(
				currentState)) {
			return mPlayer.getDuration();
		}
		else {
			return 100;
		}
	}
}
