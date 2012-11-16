package org.genshin.engine.factory;

/**
 * オブジェクトを生成する基本クラス。<br>
 * オブジェクトを取得する際、使い回せるものがあればそれを使い回す。
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <ReturnType>		生成するオブジェクトの型
 * @param <KeyType>			オブジェクトを判別するための識別子
 */
public abstract class AbstractFlyweightFactory<ReturnType, KeyType> implements FactoryInterface<ReturnType, KeyType>
{
	@Override
	public final ReturnType get(KeyType key)
	{
		// TODO ここにオブジェクトを使い回す仕組みを実装する。
		return create(key);
	}

	/**
	 * 新しいオブジェクトを生成する。
	 * @param key	オブジェクトの識別子
	 * @return		新しいオブジェクト
	 */
	protected abstract ReturnType create(KeyType key);
}
