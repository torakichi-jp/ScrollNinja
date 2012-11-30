package org.genshin.scrollninja.object.effect;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractObject;

/**
 * エフェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractEffect extends AbstractObject
{
	/**
	 * コンストラクタ
	 */
	public AbstractEffect()
	{
		//---- 更新処理を管理するオブジェクトに追加する。
		GlobalParam.INSTANCE.currentUpdatableManager.add(this, getPriority());
	}

	@Override
	public void dispose()
	{
		//---- 更新処理を管理するオブジェクトから削除する。
		GlobalParam.INSTANCE.currentUpdatableManager.remove(this, getPriority());
		
		//---- 基本クラスの破棄もしておく。
		super.dispose();
	}
	
	/**
	 * 処理優先度を取得する。
	 * @return		処理優先度
	 */
	protected int getPriority()
	{
		return 0;
	}
}
