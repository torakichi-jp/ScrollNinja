package org.genshin.scrollninja.object.weapon;

import org.genshin.scrollninja.object.attack.SlashAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;

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
	public SwordWeapon(World world, AbstractCharacter owner)
	{
		super(owner);
		attack = new SlashAttack(world, owner);
	}

	@Override
	public AttackResult attack()
	{
		//---- 攻撃を実行する。
		if(attack.isSleep())
		{
			attack.fire();
			return AttackResult.Success;
		}
		return AttackResult.Failed;
	}
	
	/**
	 * 忍者の上半身アニメーションの名前を取得する。
	 * @return		忍者の上半身アニメーションの名前
	 */
	public String getNinjaBodyAnimationName()
	{
		return "Slash";
	}
	
	/** 攻撃オブジェクト */
	private final SlashAttack attack;
}
