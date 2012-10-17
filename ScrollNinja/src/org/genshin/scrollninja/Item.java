
package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// *メモ*
// アイテム番号はマネージャで自動に割り振る

public class Item extends ObJectBase {
	
	//========================================
	// 定数宣言
	// アイテムの種類
	//========================================
	public final static int		ONIGIRI		= 0;
	public final static int		OKANE		= 1;
	
	private final static float JUMP_POWER	=  30.0f;	// ジャンプ加速度
	
	// 変数宣言
	private int 		number;				// アイテム番号
	private int 		type;				// アイテムの種類
	private boolean		appear;				// 出現フラグ
	private Vector2		position;			// 座標
	private Vector2 	velocity;			// 移動用速度
	
	// コンストラクタ
	public Item(int Type, int num, Vector2 pos) {
		sprite		= new ArrayList<Sprite>();
		sensor		= new ArrayList<Fixture>();
		type		= Type;
		number		= num;
		position	= new Vector2(pos);
		velocity	= new Vector2(0.0f, 0.0f);
		appear		= true;
		
		Create();
		sensor.get(0).setUserData(this);
	}
	
	//************************************************************
	// Create
	// アイテム生成
	//************************************************************
	private void Create() {
		switch(type) {
		case ONIGIRI:
			Texture texture = new Texture(Gdx.files.internal("data/item.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 32, 32);
			sprite.add(new Sprite(tmpRegion));
			sprite.get(0).setPosition(-sprite.get(0).getWidth() * 0.5f, -sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(0.1f);
			
			BodyDef def	= new BodyDef();
			def.type	= BodyType.DynamicBody;		// 動く物体
			body = GameMain.world.createBody(def);

			// 当たり判定の作成
			PolygonShape poly		= new PolygonShape();
			poly.setAsBox(1.0f, 1.0f);

			// ボディ設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 50;
			fd.friction		= 100.0f;
			fd.restitution	= 0;
			fd.shape		= poly;

			body.createFixture(fd);
			sensor.add(body.createFixture(poly, 0));
			body.setBullet(true);			// すり抜け防止
			body.setFixedRotation(true);	// シミュレーションでの自動回転をしない
			body.setTransform(position, 0);		// 初期位置
			break;
		}
	}
	
	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {

		position = body.getPosition();
		body.setTransform(position ,0);
		
		sprite.get(0).setPosition(position.x - 16, position.y - 16);
		sprite.get(0).setRotation((float) (body.getAngle()*180/Math.PI));
		
		Appear();
	}
	
	//************************************************************
	// Appear
	// アイテム出現時の動き
	//************************************************************
	public void Appear() {
		if(appear) {
			velocity.y = JUMP_POWER;
			appear = false;
		}
		else {
			body.setLinearVelocity(velocity.x, velocity.y);
			velocity.y -= 1.0f;
		}
	}
	
	//************************************************************
	// GetEffect
	// プレイヤーがアイテムを取った時のエフェクト処理
	//************************************************************
	public void GetEffect() {
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Item GetItem() { return this; }
	public int GetType(){ return type; }
	public int GetNum(){ return number; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetNum(int num){ number = num; }
	
}
