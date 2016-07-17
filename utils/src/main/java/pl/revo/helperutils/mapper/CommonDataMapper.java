package pl.revo.helperutils.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Common data mapper
 * @param <T> object class into which we map
 * @param <Q> object class from which we map
 */
public abstract class CommonDataMapper<T,Q> {


	public T transform(Q q){
		T t = instantiateObject();
		if(t != null ){
			if(q != null) {
				t = mapValues(t, q);
			}else{
				t = null;
			}
		}
		return t;
	}

	public List<T> transform(List<Q> q){
		List<T> list = new ArrayList<>();
		if(q != null){
			for(Q qq : q){
				list.add(transform(qq));
			}
		}else{
			list = Collections.emptyList();
		}
		return list;
	}


	public abstract T instantiateObject();
	public abstract T mapValues(T t, Q q);
}
