//******************************
//	Effect.java 
//******************************

package org.genshin.scrollninja;

//========================================
// インポート
//========================================
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
	private Vector2			position;		// 座標
	private float			stateTime;		// 
	private boolean			useFlag;		// 使用フラグ
	private TextureRegion	frame[];		// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	private Animation		animation;		// アニメーション

	//コンストラクタ
	public Effect(int type) {
		effectType	= type;
		effectTime	= 0;
		stateTime	= 0;
		position	= new Vector2(0.0f, 0.0f);
		useFlag		= false;
		
		Create();
	}
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public int GetType() { return effectType; }
	public int GetEffectTime() { return effectTime; }
	
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
	public void SetUseFlag(boolean use) { useFlag = use; }
	
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
			body = GameMain.world.createBody(def);

			// 当たり判定の作成
			PolygonShape poly		= new PolygonShape();
			poly.setAsBox(24, 16);

			// ボディ設定
			FixtureDef fd	= new FixtureDef();
			fd.density		= 50;
			fd.friction		= 100.0f;
			fd.restitution	= 0;
			fd.shape		= poly;

			//
//			body.createFixture(fd);
			sensor = body.createFixture(poly, 0);
			sensor.setSensor(true);
			body.setBullet(true);			// すり抜け防止
			//body.setTransform(0, 300, 0);	// 初期位置
			
			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/effect_fire.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 120, 120);

			// スプライトに反映
			sprite = new Sprite(region);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

			// アニメーション
			TextureRegion[][] tmp = TextureRegion.split(texture, 120, 120);
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
	// Draw
	// 描画関数まとめ
	//************************************************************
	public void Draw(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}
	
	//************************************************************
	// Update
	// 更新関数まとめ
	//************************************************************
	public void Update() {
		if( useFlag ) {
			
			nowFrame = animation.getKeyFrame(stateTime, true);
			stateTime ++;
			
			position = body.getPosition();
			sprite.setPosition(position.x - 96.0f,position.y);
			body.setTransform(PlayerManager.GetPlayer("プレイヤー").GetPosition().x + (PlayerManager.GetPlayer("プレイヤー").GetDirection() * 5),
					PlayerManager.GetPlayer("プレイヤー").GetPosition().y, 0);
			sprite.setRegion(nowFrame);
			
			animation();
			body.setTransform(PlayerManager.GetPlayer("プレイヤー").GetPosition().x + (PlayerManager.GetPlayer("プレイヤー").GetDirection() * 5),
					PlayerManager.GetPlayer("プレイヤー").GetPosition().y, 0);
			
			if( stateTime >= 18 ) {
				useFlag = false;
			}
		}
		
/*		if( PlayerManager.GetPlayer("プレイヤー").GetDirection() > 0 ) {
			sprite.setScale(-1, 1);
		}*/
		// 画面外へ
		else {
			stateTime = 0;
			body.setTransform( -1000.0f, -1000.0f, 0.0f);
			position = body.getPosition();
			sprite.setPosition(position.x - 96.0f,position.y);
		}
	}

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
}

