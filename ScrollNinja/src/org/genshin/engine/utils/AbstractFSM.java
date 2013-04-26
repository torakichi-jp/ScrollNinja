package org.genshin.engine.utils;

/**
 * FSM(有限状態機械)
 * @author kou
 * @since		1.0
 * @version	1.0
 * @param <V>	引数
 */
public abstract class AbstractFSM<V>
{
	/**
	 * 状態を更新する。
	 * @param arg		引数
	 * @return			次の状態
	 */
	public final AbstractFSM<V> update(V arg)
	{
		//---- 初期化処理
		if(!initialized)
		{
			entryAction(arg);
			initialized = true;
		}
		
		//---- 更新処理
		final AbstractFSM<V> next = inputAction(arg);
		
		//---- 終了処理
		if(next != this)
		{
			exitAction(arg);
			initialized = false;
		}
		
		return next;
	}
	
	/**
	 * 状態の開始時
	 * @param arg		引数
	 */
	protected void entryAction(V arg)
	{
		/* 何もしない */
	}
	
	/**
	 * 状態の更新
	 * @param arg		引数
	 * @return			次の状態（現在の状態を継続する場合はthisを返す）
	 */
	protected abstract AbstractFSM<V> inputAction(V arg);
	
	/**
	 * 状態の終了時
	 * @param arg
	 */
	protected void exitAction(V arg)
	{
		/* 何もしない */
	}
	
	
	/** 初期化済フラグ */
	private boolean initialized = false;
}
