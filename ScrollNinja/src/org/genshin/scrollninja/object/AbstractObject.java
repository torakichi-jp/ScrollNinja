package org.genshin.scrollninja.object;

import org.genshin.engine.system.Disposable;
import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;

/**
 * オブジェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractObject implements Updatable, Disposable, PostureInterface
{
	public AbstractObject()
	{
		//---- 更新管理オブジェクトに自身を追加する。
		Global.updatableManager.add(this, getUpdatePriority());
	}

	@Override
	public void dispose()
	{
		//---- 更新管理オブジェクトから自身を削除する。
		Global.updatableManager.remove(this, getUpdatePriority());
	}
	
	/**
	 * 更新処理の優先順位を取得する。
	 * （処理は値の昇順に実行される）
	 * @return		更新処理の優先順位
	 */
	protected int getUpdatePriority()
	{
		return GlobalDefine.UpdatePriority.DEFAULT;
	}
}
