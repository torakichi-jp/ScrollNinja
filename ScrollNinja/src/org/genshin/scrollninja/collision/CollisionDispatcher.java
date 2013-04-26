package org.genshin.scrollninja.collision;

import org.genshin.engine.system.Disposable;
import org.genshin.engine.system.Updatable;
import org.genshin.scrollninja.Global;
import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.debug.DebugTool;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 衝突判定の処理を振り分ける。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionDispatcher implements Updatable, Disposable
{
	/**
	 * コンストラクタ
	 * @param world		世界オブジェクト
	 */
	public CollisionDispatcher(World world)
	{
		this.world = world;
		
		Global.updatableManager.add(this, GlobalDefine.UpdatePriority.COLLISION_DISPATCH);
	}
	
	@Override
	public void dispose()
	{
		Global.updatableManager.remove(this, GlobalDefine.UpdatePriority.COLLISION_DISPATCH);
	}

	@Override
	public void update(float deltaTime)
	{
		DebugTool.logToScreen("[ " + world.getContactCount() + " contact ]\n");
		
		for(Contact contact : world.getContactList())
		{
			if(contact.isTouching())
			{
				final AbstractCollisionCallback callbackA = (AbstractCollisionCallback)contact.getFixtureA().getBody().getUserData();
				final AbstractCollisionCallback callbackB = (AbstractCollisionCallback)contact.getFixtureB().getBody().getUserData();
		
				callbackA.dispatch(callbackB, contact);
				callbackB.dispatch(callbackA, contact);
			}
		}
	}
	
	
	/** 世界オブジェクト */
	private final World world;
}
