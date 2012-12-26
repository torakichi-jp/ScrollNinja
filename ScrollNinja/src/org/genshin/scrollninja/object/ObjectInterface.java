package org.genshin.scrollninja.object;

import org.genshin.engine.system.PostureInterface;
import org.genshin.engine.system.Renderable;
import org.genshin.engine.system.Updatable;

/**
 * オブジェクトのインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface ObjectInterface extends Updatable, Renderable, PostureInterface
{
	/**
	 * 解放すべきものを全て解放する。
	 */
	public abstract void dispose();
}
