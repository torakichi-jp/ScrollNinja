package org.genshin.scrollninja.object.attack;

import org.genshin.scrollninja.object.AbstractCollisionObject;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * 攻撃の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractAttack extends AbstractCollisionObject implements AttackInterface
{
	@Override
	public void dispatchCollision(AbstractCollisionObject object, Contact contact)
	{
		object.notifyCollision(this, contact);
	}
}
