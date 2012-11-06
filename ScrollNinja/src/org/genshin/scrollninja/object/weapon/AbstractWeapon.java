package org.genshin.scrollninja.object.weapon;



import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.CharacterBase;
import org.genshin.scrollninja.object.Effect;

import com.badlogic.gdx.math.Vector2;

/**
 * 武器の基本クラス。
 * @author	インターン生
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractWeapon extends AbstractObject {
	// 迷ってるもの
	protected int			level;			// レベル
	protected int 			stateTime;		// 武器とエフェクトは同期する？のでここに

	// おそらく必要なもの
	protected int			attackNum;		// 攻撃力
	protected int			number;			// 管理番号
	protected Vector2		position;		// 座標
	protected CharacterBase	owner;			// 使用者
	protected Effect		myEffect;
	protected boolean		use;			// 使用フラグ

	/**
	 * 使用・停止
	 * @param flag		使用フラグ
	 */
	public void SetUseFlag( boolean flag ) { use = flag; }

	/**
	 * とりあえず
	 * @return
	 */
	public boolean GetUseFlag() { return use; }
	public Effect GetMyEffect(){ return myEffect; }
	public int GetStateTime(){ return stateTime; }
	public float GetAttackNum() { return attackNum; }
	public CharacterBase GetOwner() { return owner; }
}