package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// 制作メモ
// 10/9	座標は動いてるけど絵が付いてってない。
//		当たり判定と同期するから先に当たり判定作る。
// 10/11 ジャンプ、移動、強制追跡(Xが越えると)
//		 マネージャ、アップデートの引数をworldに
//		 二段ジャンプ制御
// 10/12


// 敵はプレイヤーを見つけたら剣や手裏剣などで攻撃
// 範囲内にいない場合は左右に移動

public class Enemy extends CharacterBase {
	//========================================
	// 定数宣言
	// 敵の種類
	//========================================
	private final static int WALKENEMY		=  0;			// 歩兵?
	private final static int ATTACKENEMY	=  1;			// 攻撃
	private final static int AUTOENEMY		=  2;			// AI

	private final static int CHECK			=  0;

	private final static float CLIMBUP		=  0.5f;
	private final static float CLIMBDOWN	=  0.5f;

	private final static int RIGHT			=  1;
	private final static int LEFT			= -1;

	private static final float WARK_SPEED	=  10f;		// 通常の歩く速度
	private static final float JUMP_POWER	=  25f;		// ジャンプ加速度
	private static final float CHASE_SPEED	=  20f;		// 追いかける時
	private static final float GRAVITY		= -20f;		// 重力
	private Vector2 velocity;							// 移動用速度

	// 変数宣言

	private int				invincibleTime;	// 無敵時間

	private String			name;				// 呼び出す時の名前
	private int				enemyType;			// 敵の種類
	private int				direction;			// 向いてる方向
	private float			stateTime;			//
	private TextureRegion[]	frame;			// アニメーションのコマ
	private TextureRegion	nowFrame;			// 現在のコマ
	private Animation		animation;			// アニメーション
	private boolean			attackFlag;		// 攻撃可能フラグ
	private boolean 		jump;				// ジャンプフラグ
	private boolean			hangingAround;	// うろうろフラグ
	private float 			fall;				// 落下量
	private Player 			player;			// プレイヤー
	private Weapon			shuri;

	// 敵スピード(仮)
	private float enemyWalkSpeed = 0.5f;

	// コンストラクタ
	Enemy(String Name, int type, Vector2 pos) {
		name		= new String(Name);
		enemyType	= type;
		position	= pos;
		hp			= 100;
		speed		= 0;
		velocity = new Vector2(0, 0);

		Create();
	}

	// コンストラクタ
	Enemy(String Name, int type, float x, float y) {
		name			= new String(Name);
		enemyType		= type;
		position.x		= x;
		position.y		= y;
		hp				= 100;
		speed			= 0;
		invincibleTime	= 0;
		velocity = new Vector2(0, 0);

		jump = false;
		attackFlag = false;
		fall = 0.0f;

		Create();
		sprite.get(0).setScale(-0.1f, 0.1f);
	}

	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update() {
		sprite.get(0).setPosition(position.x - 32, position.y - 32);
		sprite.get(0).setRotation((float) (body.getAngle()*180/Math.PI));

		position = body.getPosition();
		nowFrame = animation.getKeyFrame(stateTime, true);
		stateTime ++;

		// 敵移動
		Move();
		//重力
		//Gravity(world);

		sprite.get(0).setRegion(nowFrame);
		body.setTransform(position, body.getAngle());
	}

	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw() {
		sprite.get(0).draw(GameMain.spriteBatch);
	}

	//************************************************************
	// Create
	// テクスチャの読み込みとかスプライトのセットとかやる
	//************************************************************
	public void Create() {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		BodyDef bd	= new BodyDef();
		bd.type	= BodyType.DynamicBody;		// 動く物体
		body = GameMain.world.createBody(bd);
		body.setBullet(true);					// すり抜けない
		body.setFixedRotation(true);			// 回転しない

		// fixture生成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.6f, 2.4f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 10;
		fd.friction		= 0;
		fd.restitution	= 0;
		fd.shape		= poly;

		sensor.add(body.createFixture(fd));
		body.setTransform(0, 30, 0);	// 初期位置
		sensor.get(0).setUserData(this);

		poly.dispose();

		// エネミータイプによってテクスチャ変更
		switch(enemyType) {
		case WALKENEMY:
			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);

			// アニメーション
			TextureRegion[][] tmp = TextureRegion.split(texture, 64, 64);
			frame = new TextureRegion[4];
			int index = 0;
			for (int i = 1; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					if(index < 4)
						frame[index++] = tmp[i][j];
				}
			}
			animation = new Animation(20.0f, frame);

			break;
		}
	}

	//************************************************************
	// Move
	// 移動処理。歩りたりジャンプしたり
	//************************************************************
	public void Move() {
		switch(enemyType) {
		case WALKENEMY :
			WalkEnemy();
			//ChaseEnemy();
			JumpEnemy();
			break;
		case ATTACKENEMY :
			// 手裏剣
			break;
		case AUTOENEMY :
			// 範囲内にプレイヤーがいるかを検知し
			// 範囲内にプレイヤーがいると襲撃 or 定点攻撃
			switch(1) {
			case 1:
				break;
			}
			break;

		}
	}

	//************************************************************
	// walk
	// 基本はうろうろしているだけ、
	// 範囲内にプレイヤーを見つけると攻撃をする。
	// 範囲外に入ると何もして来ず、またうろうろし始める
	//************************************************************
	public void WalkEnemy() {

		player = PlayerManager.GetPlayer("プレイヤー");

		/*System.out.print("teki");
		System.out.println(position.x);
		System.out.print("player");
		System.out.println(player.position.x);*/

		// 範囲
		if(	player.body.getPosition().x > position.x - 25 &&
			player.body.getPosition().x < position.x + 25 &&
			player.body.getPosition().y > position.y - 20 &&
			player.body.getPosition().y < position.y + 20 ) {

			// ここで攻撃フラグON
			attackFlag = true;
		}
		if(!hangingAround) {
			sprite.get(0).setScale(0.1f, 0.1f);
			direction = LEFT;
			body.setLinearVelocity(WARK_SPEED * direction, GRAVITY);
			//position.x -= CLIMBUP;
			if(position.x <= 40) {
				hangingAround = true;
			}
		}
		if(hangingAround) {
			sprite.get(0).setScale(-0.1f, 0.1f);
			direction = RIGHT;
			body.setLinearVelocity(WARK_SPEED * direction, GRAVITY);
			//position.x += CLIMBUP;
			if(position.x >= 70) {
				hangingAround = false;
			}
		}

		// デバッグ用超加速(突撃 or ダッシュ)
		if (Gdx.input.isKeyPressed(Keys.C)) {
			enemyWalkSpeed = 0.3f;
		}
		if (Gdx.input.isKeyPressed(Keys.V)) {
			enemyWalkSpeed = 0.3f;
		}
	}
	//************************************************************
	// chase
	// プレイヤーを見つけたら追いかける
	//**********************************************************
	public void ChaseEnemy() {

		player = PlayerManager.GetPlayer("プレイヤー");

		// 追いかける
		// プレイヤーのX座標が敵のX座標より右にあるとき
		if(player.body.getPosition().x > position.x ) {

			sprite.get(0).setScale(-0.1f, 0.1f);
			direction = RIGHT;
			body.setLinearVelocity(CHASE_SPEED * direction, GRAVITY);
			//position.x += enemyWalkSpeed;

		}
		else if(player.body.getPosition().x < position.x) {
			sprite.get(0).setScale(0.1f, 0.1f);
			direction = LEFT;
			body.setLinearVelocity(CHASE_SPEED * direction, GRAVITY);
			//position.x -= enemyWalkSpeed;
		}
	}
	//************************************************************
	// jump
	//************************************************************
	public void JumpEnemy() {
		if(!jump) {
			// 上押したらジャンプ！
			if (Gdx.input.isKeyPressed(Keys.G)) {
				jump = true;
				velocity.y = JUMP_POWER;
			}
		}
		if(jump) {
			body.setLinearVelocity(body.getLinearVelocity().x, velocity.y);
			velocity.y -= 1f;
		}
	}

	//************************************************************
	// Auto
	// Artificial Intelligence
	//************************************************************
	public void AutoEnemy() {
	}

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
	public Enemy GetEnemy() { return this; }
	public String GetName(){ return name; }
	public int GetDirection() { return direction; }



	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************

}
