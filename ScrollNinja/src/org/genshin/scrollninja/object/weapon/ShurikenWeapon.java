package org.genshin.scrollninja.object.weapon;

import java.util.ArrayList;

import org.genshin.scrollninja.object.attack.AttackDef;
import org.genshin.scrollninja.object.attack.AttackDefFactory;
import org.genshin.scrollninja.object.attack.BulletAttack;
import org.genshin.scrollninja.object.character.AbstractCharacter;

import com.badlogic.gdx.physics.box2d.World;

/**
 * 手裏剣
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class ShurikenWeapon extends AbstractWeapon
{
	/**
	 * コンストラクタ
	 * @param world		所属する世界
	 * @param owner		所有者の位置情報
	 */
	public ShurikenWeapon(World world, AbstractCharacter owner)
	{
		super(owner);
		
		final int count = 1;
		final AttackDef attackDef = AttackDefFactory.getInstance().get("data/jsons/attack/shuriken.json");
		attacks.ensureCapacity(count);
		for(int i = 0;  i < count; ++i)
		{
			attacks.add(new BulletAttack(attackDef, world, owner));
		}
	}

	@Override
	public void dispose()
	{
		for(BulletAttack attack : attacks)
		{
			attack.dispose();
		}
		attacks.clear();
		
		super.dispose();
	}

	@Override
	public AttackResult attack()
	{
		for(BulletAttack attack : attacks)
		{
			if(attack.isSleep())
			{
				attack.fire();
				return AttackResult.Success;
			}
		}
		
		return AttackResult.Failed;
	}
	
	
	/** 攻撃オブジェクトの配列 */
	private final ArrayList<BulletAttack> attacks = new ArrayList<BulletAttack>();
}
