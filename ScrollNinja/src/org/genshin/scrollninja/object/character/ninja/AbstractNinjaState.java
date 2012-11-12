package org.genshin.scrollninja.object.character.ninja;

/**
 * 忍者の状態の基本クラス。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractNinjaState
{
	/**
	 * コンストラクタ
	 * @param me	自身となる忍者オブジェクト
	 */
	AbstractNinjaState(PlayerNinja me)
	{
		this.me = me;
	}
	
	/**
	 * 自身となる忍者オブジェクトを取得する。
	 * @return		自身となる忍者オブジェクト
	 */
	protected PlayerNinja getMe()
	{
		return me;
	}
	
	/**
	 * 状態を更新し、必要であれば次の状態に遷移する。
	 * @return		次の状態。状態を変更しない場合はthisを返す。
	 */
	abstract AbstractNinjaState update();
	
	/** 自身となる忍者オブジェクト */
	final private PlayerNinja me;
}
