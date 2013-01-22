package org.genshin.scrollninja.work.render;

import org.genshin.engine.system.NewAbstractProcessManager;



/**
 * 描画オブジェクトを一括管理する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class RenderManager extends NewAbstractProcessManager<RenderObjectInterface>
{
	/**
	 * 描画処理を実行する。
	 */
	public void render()
	{
		process();
	}

	@Override
	protected ProcessResult processOne(RenderObjectInterface object)
	{
		if(object.isDisposed())
			return ProcessResult.REMOVE;
		object.render();
		return ProcessResult.SURVIVE;
	}
}
