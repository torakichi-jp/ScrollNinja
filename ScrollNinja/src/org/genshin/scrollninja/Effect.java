//******************************
//	Effect.java
//******************************

package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

//========================================
// クラス宣言
//========================================
public class Effect extends ObJectBase {

	//========================================
	// 定数宣言
	// エフェクトの種類
	//========================================
	public final static int FIRE_1		= 0;
	public final static int FIRE_2		= 1;
	public final static int FIRE_3		= 2;
	public final static int WATER_1		= 3;
	public final static int WATER_2		= 4;
	public final static int WATER_3		= 5;
	public final static int WIND_1		= 6;
	public final static int WIND_2		= 7;
	public final static int WIND_3		= 8;

	//変数宣言
	private int				effectType;		// エフェクトの種類
	private int 			effectTime;		// 効果時間
	private float 			attackNum;		// 攻撃力
	private Vector2			position;		// 座標
	private float			stateTime;		//
	private boolean			useFlag;		// 使用フラグ
	private TextureRegion[]	frame;			// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	private Animation		animation;		// アニメーション
	private CharacterBase	myOwner;		// エフェクトを発生させたキャラクター

	//コンストラクタ
	public Effect(int type, CharacterBase owner) {
		sprite		= new ArrayList<Sprite>();
		sensor		= new ArrayList<Fixture>();
		effectType	= type;
		effectTime	= 0;
		stateTime	= 0;
		attackNum	= 0;
		position	= new Vector2(0.0f, 0.0f);
		useFlag		= false;
		myOwner		= owner;

		Create();
		sensor.get(0).setUserData(this);
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public int GetType() { return effectType; }
	public int GetEffectTime() { return effectTime; }
	public float GetAttackNum(){ return attackNum; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetUseFlag(boolean use) { useFlag = use; }
	public void SetAttackNum(int num){ attackNum = num; }
//	public void SetOwner(CharacterBase owner) { myOwner = owner; }

	//************************************************************
	// Create
	// エフェクトの生成
	//************************************************************
	public void Create() {
		switch(effectType) {
		case FIRE_1:
			break;
		case FIRE_2:
			BodyDef def	= new BodyDef();
			def.type	= BodyType.DynamicBody;		// 動く物体
			body		= GameMain.world.createBody(def);

			// 当たり判定の作成
			PolygonShape poly		= new PolygonShape();
			poly.setAsBox(2.4f, 1.6f);

			// ボディ設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 50;
			fd.friction		= 0;
			fd.restitution	= 0;
			fd.shape		= poly;

			//
//			body.createFixture(fd);
			sensor.add(body.createFixture(poly, 0));
			sensor.get(0).setSensor(true);
			body.setBullet(true);			// すり抜け防止

			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 128, 128);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(0.1f);

			// アニメーション
			TextureRegion[][] tmp = TextureRegion.split(texture, 128, 128);
			frame = new TextureRegion[6];
			int index = 0;
			for (int i = 1; i < 2; i++) {
				for (int j = 0; j < 6; j++) {
					if(index < 6)
						frame[index++] = tmp[i][j];
				}
			}

			animation = new Animation(3.0f, frame);
			break;
		case FIRE_3:
			break;
		case WATER_1:
			break;
		case WATER_2:
			break;
		case WATER_3:
			break;
		}
	}

	//************************************************************
	// Update
	// 更新関数まとめ
	//************************************************************
	public void Update() {
		if( useFlag ) {

			nowFrame = animation.getKeyFrame(stateTime, true);
			stateTime ++;

			body.setTransform(PlayerManager.GetPlayer("プレイヤー").GetPosition().x +
								(PlayerManager.GetPlayer("プレイヤー").GetDirection() * 5),
									PlayerManager.GetPlayer("プレイヤー").GetPosition().y, 0);
			position = body.getPosition();
			// 64はTextureRegionの幅÷２。後は微調整
			sprite.get(0).setPosition(position.x - 64 - (1 * PlayerManager.GetPlayer("プレイヤー").GetDirection()),
								position.y - 64 + 1);
			sprite.get(0).setScale(-PlayerManager.GetPlayer("プレイヤー").GetDirection() * 0.1f, 0.1f);
			sprite.get(0).setRegion(nowFrame);

			animation();

			body.setTransform(PlayerManager.GetPlayer("プレイヤー").GetPosition().x +
								(PlayerManager.GetPlayer("プレイヤー").GetDirection() * 5),
									PlayerManager.GetPlayer("プレイヤー").GetPosition().y, 0);

			if( stateTime >= 18 ) {
				useFlag = false;
			}
		}

		// 画面外へ
		else {
			stateTime = 0;
			body.setTransform( -100.0f, -100.0f, 0.0f);
			position = body.getPosition();
			sprite.get(0).setPosition(position.x - 100, position.y);
		}
	}

	//************************************************************
	// Colision
	// 当たり判定処理
	//************************************************************
/*	private boolean Colision(CharacterBase chara) {
		List<Contact> contactList = GameMain.world.getContactList();

		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);

			// 当たったよ！
			for( int j = 0; j < Background.GetBody().getFixtureList().size(); j ++) {
				if( chara == myOwner ) {
					continue;
				}

				if(contact.isTouching() &&
						(( contact.getFixtureA() == sensor && contact.getFixtureB() == chara.GetSensor() ) ||
						( contact.getFixtureA() == chara.GetSensor() && contact.getFixtureB() == sensor ))) {
						return true;
				}
			}
		}
		return false;
	}*/

	//************************************************************
	// animation
	// アニメーション処理
	//************************************************************
	private void animation() {
		switch(effectType) {
		case FIRE_1:
			break;
		case FIRE_2:
			break;
		case FIRE_3:
			break;
		case WATER_1:
			break;
		case WATER_2:
			break;
		case WATER_3:
			break;
		}
	}
	
	@Override
	public void collisionDispatch(ObJectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}
	
	@Override
	public void collisionNotify(Background obj, Contact contact){}	
	
	@Override
	public void collisionNotify(Player obj, Contact contact){}
	
	@Override
	public void collisionNotify(Enemy obj, Contact contact){}
	
	@Override
	public void collisionNotify(Effect obj, Contact contact){}
	
	@Override
	public void collisionNotify(Item obj, Contact contact){}
	
	@Override
	public void collisionNotify(StageObject obj, Contact contact){}
	
	@Override
	public void collisionNotify(Weapon obj, Contact contact){}
}