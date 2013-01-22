package org.genshin.scrollninja.work.object;

import org.genshin.engine.system.NewAbstractProcessManager;



/**
 * 更新オブジェクトを一括管理する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class UpdateManager extends NewAbstractProcessManager<UpdateObjectInterface>
{
	/**
	 * 処理優先度順に更新処理を実行する。
	 * @param deltaTime		経過時間
	 */
	public void update(float deltaTime)
	{
		this.deltaTime = deltaTime;
		process();
	}

	@Override
	protected ProcessResult processOne(UpdateObjectInterface object)
	{
		if(object.isDisposed())
			return ProcessResult.REMOVE;
		object.update(deltaTime);
		return ProcessResult.SURVIVE;
	}
	
	/** 経過時間 */
	float deltaTime;
}
