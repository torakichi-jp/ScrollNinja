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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;



public class Weapon extends ObJectBase{

	private String	name;			// 名前
	private Vector2 	position;		// 武器座標
	private float 	attackNum;		// 武器威力
	private int 		weaponLevel;	// 武器レベル
	private Boolean 	use;			// 使用フラグ
	private boolean	ShootFlag;		// シュートフラグ(手裏剣)
	private int 		deleteTime;	// 手裏剣消去時間
	private Vector2 	velocity;		// 移動用速度
	private boolean 	FlyingFlag;	// 手裏剣を動かすフラグ
	private float	rotate;			// 手裏剣回転要
	private double	random;		// 手裏剣をランダムで複数出す用

	private Player player;
	private Enemy enemy;

	private static final float FIRST_SPEED	=  30f;		// 初速度
	private static final float GRAVITY		= -20f;		// 重力
	private static final int   MAX_WIDTH	= 100;

	//コンストラクタ
	public Weapon(String Name) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();
		enemy = EnemyManager.GetEnemy("1");
		name			 = new String(Name);
		this.position    = new Vector2(0,-20);		// 初期位置は画面外
		this.attackNum   = 0;
		this.weaponLevel = 0;
		this.use         = true;
		this.velocity	   = new Vector2(0,0);

		FlyingFlag = false;
		rotate = 0;

		create();
	}

	// 武器生成
	public void create() {
		// テクスチャー読み込み
		// TODO 手裏剣テクスチャの位置はとりあえずなので後で要調整
		Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 448, 64, 64);

		// スプライト反映
		sprite.add(new Sprite(region));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(0.05f);

		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;		// 動く物体
		body = GameMain.world.createBody(def);

		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.0f, 1.0f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 0;
		fd.friction		= 0.0f;
		fd.restitution	= 0;
		fd.shape		= poly;

		sensor.add(body.createFixture(fd));
		sensor.get(0).setUserData(this);
		body.setBullet(true);			// すり抜け防止
		body.setFixedRotation(true);	// シミュレーションでの自動回転をしない
		body.setTransform(position, 0);		// 初期位置
		body.setLinearVelocity(1, 0);

		deleteTime = 120;		// 120fで消える

		System.out.println(body.getPosition());
	}

	// 更新
	public void Update() {
		player = PlayerManager.GetPlayer("プレイヤー");
		enemy = EnemyManager.GetEnemy("1");

		position = body.getPosition();
		//body.setTransform(position ,0);

		//if(ShootFlag)
		//body.setTransform(position, body.getAngle());
		//body.setLinearVelocity(position.x , 0);

		shuriken();
	}

	// 手裏剣の動き
	public void shuriken() {

		// 手裏剣表示時間
		deleteTime += 1;
		rotate++;

		//if (Gdx.input.isKeyPressed(Keys.F)) {
		if (!FlyingFlag) {
			FlyingFlag = true;
			ShootFlag = true;
			// 押されたら手裏剣の座標を敵の位置へ移動
			body.setTransform(enemy.position.x + 5, enemy.position.y, rotate);
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
			body.setLinearVelocity(velocity.x + 1, 0);

			/*
			// 敵とプレイヤーの高低チェック
			if(enemy.position.y > player.position.y) {
				// 敵の方が上にいる場合
				velocity.y += 0.1f;
			}
			else if(enemy.position.y < player.position.y) {
				// プレイヤーのほうが上にいる場合
				velocity.y -= 0.1f;
			}
			*/
		}

		if(deleteTime >= 120 && FlyingFlag) {
			// 120fたったら手裏剣を画面外へ
			sprite.get(0).setPosition(0, -60);
			FlyingFlag = false;
			ShootFlag = false;
		}
	}

	// 描画
	public void Draw(SpriteBatch batch) {
		sprite.get(0).draw(batch);
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

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Weapon GetWeapon() { return this; }
	public String GetName(){ return name; }



}
