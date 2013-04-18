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
public class KatanaWeapon extends AbstractWeapon
{
	/**
	 * コンストラクタ
	 * @param world		所属する世界オブジェクト
	 * @param owner		所有者の位置情報
	 */
	public KatanaWeapon(World world, AbstractCharacter owner)
	{
		super(owner);
		attack = new SlashAttack("data/jsons/attack/katana.json", world, owner);
	}

	@Override
	public void dispose()
	{
		//---- このクラスを破棄する。
		attack.dispose();
		
		//---- 基本クラスを破棄する。
		super.dispose();
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
	
	
	/** 攻撃オブジェクト */
	private final SlashAttack attack;
}
