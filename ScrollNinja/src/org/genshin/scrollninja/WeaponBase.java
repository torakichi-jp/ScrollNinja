package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class WeaponBase extends ObJectBase{
	
	// 迷ってるもの
	protected int			level;			// レベル
	
	// おそらく必要なもの
	protected int			attackNum;		// 攻撃力
	protected int			number;			// 管理番号
	protected Vector2		position;		// 座標
	protected CharacterBase	owner;			// 使用者
}
