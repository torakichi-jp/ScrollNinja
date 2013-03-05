package org.genshin.scrollninja.object.weapon;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.attack.AttackInterface;
import org.genshin.scrollninja.object.attack.SlashAttack;

import com.badlogic.gdx.physics.box2d.World;

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
	 * @param world		所属する世界オブジェクト
	 * @param owner		所有者の位置情報
	 */
	public SwordWeapon(World world, PostureInterface owner)
	{
		super(owner);
		attack = new SlashAttack(world, owner);
	}

	@Override
	public void attack()
	{
		//---- 攻撃を実行する。
		if(attack.isSleep())
			attack.fire();
	}
	
	/** 攻撃オブジェクト */
	private final AttackInterface attack;
}
