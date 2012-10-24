/**
 * 
 */
package org.genshin.scrollninja.object.weapon;

import org.genshin.scrollninja.Background;
import org.genshin.scrollninja.CharacterBase;
import org.genshin.scrollninja.GameMain;
import org.genshin.scrollninja.ObJectBase;
import org.genshin.scrollninja.ScrollNinja;
import org.genshin.scrollninja.WeaponBase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 鍵縄クラス
 * @author kou
 */
public class Kaginawa extends WeaponBase
{
	private static final float VEL = 30.0f;		// 鍵縄の飛ぶ速度
	
	private static final class COLLISION
	{
		private static final float RADIUS = 1.0f;	// 衝突オブジェクトの半径
	}
	
	private static final class SPRITE
	{
		private static final String PATH = "data/shuriken.png";
	}
	
	private Vector2 direction = new Vector2();		// 向き
	
	/**
	 * コンストラクタ
	 */
	public Kaginawa(CharacterBase owner)
	{
		World world = GameMain.world;
		
		// body生成
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;		// 移動するオブジェクト
		bd.active = false;					// 最初は活動しない
		bd.bullet = true;					// すり抜けない
		bd.gravityScale = 0.0f;				// 重力の影響を受けない
		body = world.createBody(bd);
		
		// 衝突オブジェクト生成
		CircleShape cs = new CircleShape();
		cs.setRadius(COLLISION.RADIUS);
		
		FixtureDef fd = new FixtureDef();
		fd.density		= 0.0f;	// 密度
		fd.friction	= 0.0f;	// 摩擦
		fd.isSensor	= true;	// センサーフラグ
		fd.shape		= cs;		// 形状
		
		Fixture fixture = body.createFixture(fd);
		fixture.setUserData(this);
		sensor.add(fixture);
		cs.dispose();
		
		// スプライト生成
		Texture texture = new Texture(Gdx.files.internal(SPRITE.PATH));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Sprite sprite = new Sprite(texture);
		sprite.setOrigin(texture.getWidth()*0.5f, texture.getHeight()*0.5f);
		sprite.setScale(ScrollNinja.scale);
		
		this.sprite.add(sprite);
		
		// フィールド初期化
		this.owner = owner;
	}
	
	/**
	 * 攻撃する。
	 */
	public void attack()
	{
		body.setTransform(owner.getPosition(), 0);
		body.setActive(true);
		
		// FIXME 鍵縄の向き設定（仮）
		Vector2 mousePos = new Vector2(
			Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f,
			Gdx.graphics.getHeight()*0.5f - Gdx.input.getY() 
		);
		Vector2 ownerPos = owner.getPosition();
		
		mousePos.mul(ScrollNinja.scale);
		mousePos.x += GameMain.camera.position.x;
		mousePos.y += GameMain.camera.position.y;
		
		direction.x = mousePos.x - ownerPos.x;
		direction.y = mousePos.y - ownerPos.y;
		direction.nor();

		body.setLinearVelocity(direction.x*VEL, direction.y*VEL);
	}

	@Override
	public void Update()
	{
		// TODO 鍵縄の飛ぶ処理とか。
	}
	
	@Override
	public void Draw()
	{
		if(body.isActive())
			super.Draw();
	}

	@Override
	protected void collisionDispatch(ObJectBase obj, Contact contact)
	{
		//obj.collisionNotify(this, contact);
	}

	@Override
	protected void collisionNotify(Background obj, Contact contact)
	{
		// TODO 鍵縄の衝突処理とか。
		body.setLinearVelocity(0.0f, 0.0f);
	}

}
