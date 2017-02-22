package pl.revo.helperutils.utils;

import android.content.Context;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;


public class RxCustom {

	public static Observable<?> prepare(Context context){
		return Observable.create(new CustomObservable(context)).delay(2, TimeUnit.SECONDS);
	}
}
