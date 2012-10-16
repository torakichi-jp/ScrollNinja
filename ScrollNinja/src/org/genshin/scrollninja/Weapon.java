//******************************
//	Weapon.java
//******************************

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;



public class Weapon extends ObJectBase{

	private String	name;			// 名前
	private Sprite	sprite;		// スプライト
	private Vector2 	position;		// 武器座標
	private float 	attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private Boolean 	use;			// 使用フラグ
	private Body		body;			// ボディ
	private boolean	ShootFlag;		// シュートフラグ(手裏剣)
	private int 		deleteTime;	// 手裏剣消去時間
	private Vector2 	velocity;		// 移動用速度
	private boolean 	FlyingFlag;
	
	private Player player;
	private Enemy enemy;

	private static final float FIRST_SPEED	=  30f;		// 初速度
	private static final float GRAVITY		= -20f;		// 重力
	private static final int   MAX_WIDTH	= 100;

	//コンストラクタ
	public Weapon(String Name) {
		//enemy = EnemyManager.GetEnemy("1");
		name			 = new String(Name);
		this.position    = new Vector2(0,-20);		// 初期位置は画面外
		this.attackNum   = 0;
		this.weaponLevel = 0;
		this.use         = true;
		this.velocity	   = new Vector2(0,0);


		FlyingFlag = false;

		create();
	}

	// 武器生成
	public void create() {

		// テクスチャー読み込み
		Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 448, 64, 64);

		// スプライト反映
		sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
		sprite.setScale(0.05f);

		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;		// 動く物体
		body = GameMain.world.createBody(def);

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.0f, 1.0f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 50;
		fd.friction		= 10.0f;
		fd.restitution	= 0;
		fd.shape		= poly;

		body.createFixture(fd);
		sensor = new ArrayList<Fixture>();
		sensor.add(body.createFixture(poly, 0));
		body.setBullet(true);			// すり抜け防止
		body.setFixedRotation(true);	// シミュレーションでの自動回転をしない
		body.setTransform(position, 0);		// 初期位置
		
		deleteTime = 120;		// 120fで消える

	}

	// 更新
	public void Update() {
		player = PlayerManager.GetPlayer("プレイヤー");
		enemy = EnemyManager.GetEnemy("1");

		position = body.getPosition();
		body.setTransform(position ,0);

		//if(ShootFlag)
		//body.setTransform(position, body.getAngle());
		//body.setLinearVelocity(position.x , 0);

		shuriken();
		
		System.out.println(player.position.y);

	}

	// 手裏剣の動き
	public void shuriken() {
		
		sprite.setPosition(position.x - 32, position.y - 32);
		sprite.setRotation((float) (body.getAngle()*180/Math.PI));
		
		// 手裏剣表示時間
		deleteTime += 1;
		
		if (Gdx.input.isKeyPressed(Keys.S)) {
			FlyingFlag = true;
			ShootFlag = true;
			// 押されたら手裏剣の座標を敵の位置へ移動
			position = enemy.position;
		}

		// debug
		if(FlyingFlag) {
			//position.x += 1.0f;
			// 向きで飛ぶ方向決定
			if(enemy.GetDirection() == 1) {
				// 加速度加算
				velocity.x += 1.0f;
			}
			else {
				// 加速度減算
				velocity.x -= 1.0f;
			}
			
			// 敵とプレイヤーの高低チェック
			if(enemy.position.y > player.position.y) {
				// 敵の方が上にいる場合
				velocity.y += 0.1f;
			}
			else if(enemy.position.y < player.position.y) {
				// プレイヤーのほうが上にいる場合
				velocity.y -= 0.1f;	
			}
		}
		
		if(deleteTime >= 120 && FlyingFlag) {
			// 120fたったら手裏剣を画面外へ
			sprite.setPosition(0, -60);
			FlyingFlag = false;
			ShootFlag = false;
		}
	}

	// 描画
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}



	//武器座標ゲット
	public Vector2 GetWeaponPosition() {
		return position;
	}

	//武器威力ゲット
	public float GetAttackNum() {
		return attackNum;
	}


	//武器レベルゲット
	public int GetWeaponLv() {
		return GetWeaponLv();
	}

	// フラグゲット
	public boolean GetUseFlag() {
		return use;
	}

	// 武器モーション
	public void WeaponMove() {

		//------------------
		// 武器動作
		//------------------
	}

	// 武器のレベルアップ(仮)
	public int WeaponLvUp(int chakra) {

		return this.weaponLevel;
	}

	// 武器のボックスあたり判定(仮)
	public int HitCheck() {
		return -1;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Weapon GetWeapon() { return this; }
	public String GetName(){ return name; }



}
