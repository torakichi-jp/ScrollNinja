package org.genshin.engine.system;

/**
 * 更新処理を持つインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface Updatable
{
	/**
	 * 更新処理を実行する。
	 * TODO 引数にdeltaTimeを追加したい。
	 * XXX 場合によってはprotected化するのもアリ？
	 */
	public void update();
}
