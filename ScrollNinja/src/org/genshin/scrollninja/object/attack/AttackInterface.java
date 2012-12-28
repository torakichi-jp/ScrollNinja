package org.genshin.scrollninja.object.attack;

/**
 * 攻撃のインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface AttackInterface
{
	/**
	 * 攻撃を実行する。
	 * @param degrees	角度（度）
	 * @param flip		反転フラグ
	 */
	public void fire(float degrees, boolean flip);
	
	/**
	 * 待機状態か調べる。
	 * @return		待機状態ならtrue
	 */
	public boolean isSleep();
}
