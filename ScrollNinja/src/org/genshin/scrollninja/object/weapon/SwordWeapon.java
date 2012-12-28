package org.genshin.scrollninja.object.weapon;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.attack.AttackInterface;
import org.genshin.scrollninja.object.attack.SlashAttack;

/**
 * 刀
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class SwordWeapon extends AbstractWeapon
{
	/**
	 * コンストラクタ
	 * @param owner		所有者の位置情報
	 */
	public SwordWeapon(PostureInterface owner)
	{
		super(owner);
	}

	@Override
	public void attack(float degrees, boolean flip)
	{
		//---- 攻撃を実行する。
		attack.fire(degrees, flip);
	}
	
	/** 攻撃オブジェクト */
	private final AttackInterface attack = new SlashAttack(this);
}
