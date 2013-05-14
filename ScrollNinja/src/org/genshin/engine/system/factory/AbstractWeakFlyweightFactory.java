package org.genshin.engine.system.factory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * オブジェクトを生成する基本クラス。<br>
 * オブジェクトを取得する際、使い回せるものがあればそれを使い回す。<br>
 * オブジェクトを弱参照で管理しているため、他からの強参照がなくなったオブジェクトはGCで自動的に削除される。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <K>	オブジェクトを判別するための識別子
 * @param <V>	生成するオブジェクトの型
 */
public abstract class AbstractWeakFlyweightFactory<K, V> implements FactoryInterface<K, V>
{
	@Override
	public final V get(K key)
	{
		final WeakReference<V> ref = objects.get(key);
		V result;
		
		//---- まだ管理されていないオブジェクト
		if(ref == null || (result = ref.get()) == null)
		{
			result = create(key);
			objects.put(key, new WeakReference<V>(result));
		}
		
		return result;
	}
	
	/**
	 * 管理オブジェクトを全て削除する。
	 */
	public final void clear()
	{
		objects.clear();
	}
	
	/**
	 * 管理オブジェクトの数を取得する。
	 * @return		管理オブジェクトの数
	 */
	public final int size()
	{
		return objects.size();
	}
	
	/**
	 * 新しいオブジェクトを生成する。
	 * @param key	オブジェクトの識別子
	 * @return		新しいオブジェクト
	 */
	protected abstract V create(K key);
	
	
	/** 管理オブジェクトのマップ */
	private final Map<K, WeakReference<V>> objects = new HashMap<K, WeakReference<V>>();
}
