package org.genshin.scrollninja.work.collision;

import java.util.HashMap;
import java.util.Map;

import org.genshin.engine.system.Disposable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;

/**
 * 衝突判定オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionObject implements Disposable
{
	public CollisionObject(String collisionName, World world, AbstractCollisionCallback collisionCallback)
	{
		CollisionDef collisionDef = new CollisionDef();
		Json json = new Json();
		String buf = json.toJson(collisionDef);
		System.out.println(buf);
		
		
//		BodyDef bodyDef = new BodyDef();
//		bodyDef.type = BodyType.DynamicBody;
//		FixtureDef fixtureDef = new FixtureDef();
//		fixtureDef.shape = new CircleShape();
//		
//		body = world.createBody(bodyDef);
//		fixture = body.createFixture(fixtureDef);
//		this.collisionCallback = collisionCallback;
//		
//		body.setUserData(this.collisionCallback);
	}
	
	@Override
	public void dispose()
	{
		//---- Fixtureオブジェクトを破棄する。
		if( !fixtures.isEmpty() )
		{
			for(Fixture fixture : fixtures.values())
			{
				fixture.getBody().destroyFixture(fixture);
			}
			fixtures.clear();
		}
		
		//---- Bodyオブジェクトを破棄する。
		if(body != null)
		{
			body.getWorld().destroyBody(body);
			body = null;
		}
	}
	
	@Override
	public boolean isDisposed()
	{
		return body == null;
	}
	
	/**
	 * Bodyオブジェクトを取得する。
	 * @return		Bodyオブジェクト
	 */
	public Body getBody()
	{
		return body;
	}
	
	/**
	 * Fixtureオブジェクトを取得する。
	 * @param name		取得するFixtureオブジェクトの名前
	 * @return			Fixtureオブジェクト（指定された名前のFixtureオブジェクトを所持していない場合はnull）
	 */
	public Fixture getFixture(String name)
	{
		return fixtures.get(name);
	}
	
	
	/** Bodyオブジェクト */
	private Body body;
	
	/** Fixtureオブジェクトのマップ */
	private final Map<String, Fixture> fixtures = new HashMap<String, Fixture>();
}
