package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class WeaponBase extends ObJectBase {
	
	// 迷ってるもの
	protected int			level;			// レベル
	
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
}