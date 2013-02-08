package org.genshin.scrollninja.work.collision;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.genshin.engine.system.Disposable;
import org.genshin.scrollninja.work.collision.CollisionDef.BodyEditorFixtureDef;
import org.genshin.scrollninja.work.collision.CollisionDef.FixtureDefPair;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 衝突判定オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionObject implements Disposable
{
	/**
	 * コンストラクタ
	 * @param collisionFilePath		衝突判定の定義ファイルのパス
	 * @param world					所属する世界オブジェクト
	 * @param collisionCallback		衝突処理の呼び出しに使用するコールバックオブジェクト
	 */
	public CollisionObject(String collisionFilePath, World world, AbstractCollisionCallback collisionCallback)
	{
		final CollisionDef collisionDef = CollisionDefFactory.getInstance().get(collisionFilePath);
		
		//---- Body生成
		body = world.createBody(collisionDef.bodyDef);
		body.setUserData(collisionCallback);
		
		//---- Fixture生成
		// 通常
		for(Entry<String, FixtureDef> entry : collisionDef.fixtureDefs.entrySet())
		{
			fixtures.put(entry.getKey(), body.createFixture(entry.getValue()));
		}
		
		// BodyEditor
		for(BodyEditorFixtureDef def : collisionDef.bodyEditorFixtureDefs)
		{
			final BodyEditorLoader loader = new BodyEditorLoader(def.jsonString);
			for(FixtureDefPair pair : def.fixtureDefPairs)
			{
				loader.attachFixture(body, pair.name, pair.fixtureDef, pair.scale);
			}
		}
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
			for(Fixture fixture : body.getFixtureList())
			{
				body.destroyFixture(fixture);
			}
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
