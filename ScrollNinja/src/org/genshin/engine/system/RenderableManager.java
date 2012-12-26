package org.genshin.engine.system;



/**
 * 描画オブジェクトを一括管理する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class RenderableManager extends AbstractProcessManager<Renderable>
{
	/**
	 * 描画処理を実行する。
	 */
	public void render()
	{
		process();
	}

	@Override
	protected void processOne(Renderable object)
	{
		object.render();
	}
}
