package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// 制作メモ
// 10/9	座標は動いてるけど絵が付いてってない。
//		当たり判定と同期するから先に当たり判定作る。
// 10/11 ジャンプ、移動、強制追跡(Xが越えると)
//		 マネージャ、アップデートの引数をworldに
//		 二段ジャンプ制御
// 10/18	色々微修正
// 吉田		うろうろと追いかけ調整
//			手裏剣での自動攻撃
//
// 敵はプレイヤーを見つけたら剣や手裏剣などで攻撃
// 範囲内にいない場合は左右に移動

public class Enemy extends CharacterBase {
	// 定数宣言
	// 敵のモード
	private final int NON_ACTIVE	= 100;	// 攻撃が当たるまでうろうろしてるだけ
	private final int ACTIVE		= 101;	// 近づいたら攻撃してくる
	// 敵のタイプ
	public final static int NORMAL			= 0;	// ノーマル
	public final static int RARE			= 1;	// レア
	public final static int AUTO			= 2;	// AI

	// 方向
	private final int RIGHT			=  1;
	private final int LEFT			= -1;

	// 手裏剣所持数
	private final int MAX_SYURIKEN	= 10;
	private final int INTERVAL		= 60;

	// 速度
	private final float WALK_SPEED	=  15f;		// 通常の歩く速度
	private final float JUMP_POWER	=  25f;		// ジャンプ加速度
	private final float CHASE_SPEED	=  25f;		// 追いかける時
	private final float GRAVITY		= -20f;		// 重力
	private Vector2 velocity;					// 移動用速度

	// 変数宣言
	private int				invincibleTime;	// 無敵時間

	private int				enemyType;		// 敵のタイプ
	private int				number;			// 管理番号
	private int				enemyMode;		// 敵のモード
	private int				direction;		// 向いてる方向
	private float			stateTime;		//
	private TextureRegion[]	frame;			// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	private Animation		animation;		// アニメーション
	private boolean			attackFlag;		// 攻撃可能フラグ
	private boolean 		jump;			// ジャンプフラグ
	private boolean			chase;			// 追いかけフラグ
	private boolean			deleteFlag;		// 削除フラグ
	private Player 			player;			// プレイヤー
	private ArrayList<Weapon>	syuriken;		// 手裏剣
	private Weapon			blade;			// 刀
	private int				attackInterval;	// 攻撃間隔		TODO 未実装

	private Vector2			wanderingPosition;	// うろうろ場所用に出現位置を保存

	private Random 			rand;			// ランダム

	/**************************************************
	 * コンストラクタ
	 **************************************************/
	Enemy(int type, int num, float x, float y) {
		enemyType			= type;
		number				= num;
		position.x			= x;
		position.y			= y;
		direction			= LEFT;
		hp					= 100;
		speed				= 0;
		invincibleTime		= 0;
		attackInterval		= 0;
		velocity			= new Vector2(0, 0);
		wanderingPosition	= new Vector2(x, y);

		syuriken = null;

		jump				= false;
		attackFlag			= false;
		chase				= false;
		deleteFlag			= false;

		// TODO 色々いじり中
		// ランダムでモードを設定してみる
		rand = new Random();
		int i = rand.nextInt(10);
		//if (i == 0)
			enemyMode = ACTIVE;
		//else
			//enemyMode = NON_ACTIVE;

		Create();
	}

	/**************************************************
	 * update
	 * 更新処理まとめ
	 **************************************************/
	public void Update() {
		if( deleteFlag ) {
			ItemManager.CreateItem(Item.ONIGIRI, position.x, position.y);
			EnemyManager.Deleteenemy(this);
			return;
		}

		if( invincibleTime > 0 ) invincibleTime --;		// 無敵時間の減少
		position = body.getPosition();					// 現在位置の更新

		Action();							// 行動

		if (syuriken != null) {
			for (int i = 0; i < syuriken.size(); i++) {
				if (syuriken.get(i).GetUseFlag())
					syuriken.get(i).Update();
				else {
					syuriken.get(i).Release();
					syuriken.remove(i);
				}
			}
		}

		// アニメーション更新
		nowFrame = animation.getKeyFrame(stateTime, true);
		stateTime ++;
		sprite.get(0).setRegion(nowFrame);
	}

	/**************************************************
	* Create
	* body、sensor、sprite、アニメーション作成
	**************************************************/
	public void Create() {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		BodyDef bd	= new BodyDef();
		bd.type	= BodyType.DynamicBody;			// 動く物体
		body = GameMain.world.createBody(bd);

		// fixture生成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.6f, 2.4f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 10;
		fd.friction		= 0;
		fd.restitution	= 0;
		fd.shape		= poly;

		sensor.add(body.createFixture(fd));		// センサーに追加
		sensor.get(0).setUserData(this);		// 当たり判定要にUserDataセット
		body.setTransform(position, 0);			// 最初の位置
		body.setBullet(true);					// すり抜けない
		body.setFixedRotation(true);			// 回転しない

		poly.dispose();

		// エネミータイプによってテクスチャ変更
		switch(enemyType) {
		case NORMAL:
			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(0.1f, 0.1f);

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

		case RARE:
			break;
		}
	}

	/**************************************************
	* Action
	* 行動の分岐
	**************************************************/
	public void Action() {
		switch(enemyMode) {
		case NON_ACTIVE :
			walk();
			/*
			int i = rand.nextInt(10);
			System.out.println(i);
			if (i == 1)
				jump();
			*/
			break;
		case ACTIVE :
			walk();
			chase();
			if (attackFlag) {
				if (attackInterval == 0)
					attack();
				else
					attackInterval -= 1;
			}
			break;
		case AUTO :
			// 範囲内にプレイヤーがいるかを検知し
			// 範囲内にプレイヤーがいると襲撃 or 定点攻撃
			switch(1) {
			case 1:
				break;
			}
			break;

		}
	}

	/**************************************************
	 * walk
	 * 指定範囲内をうろうろしているだけ
	 **************************************************/
	public void walk() {
		if(!chase) {
			// 右端まで到達
			if(position.x > wanderingPosition.x + 20) {
				sprite.get(0).setScale(0.1f, 0.1f);
				direction = LEFT;
			}
			// 左端まで到達
			if(position.x < wanderingPosition.x - 20) {
				sprite.get(0).setScale(-0.1f, 0.1f);
				direction = RIGHT;
			}
			body.setLinearVelocity(WALK_SPEED * direction, GRAVITY);
		}
	}

	/**************************************************
	* chase
	* プレイヤーを見つけたら追いかける
	**************************************************/
	public void chase() {
		// プレイヤーの位置を取得
		player = PlayerManager.GetPlayer("プレイヤー");

		// 一定距離まで近づいたら
		if (Math.abs(player.body.getPosition().x - position.x) < 20) {
			// 追いかけフラグON
			chase = true;
		}
		// 距離が離れたら
		if (Math.abs(player.body.getPosition().x - position.x) > 30 && chase) {
			// 追いかけフラグOFF
			chase = false;
			// 現在の位置をうろうろ位置に設定
			wanderingPosition = new Vector2(position);
		}

		if (chase) {
			// プレイヤーのX座標が敵のX座標より右にあるとき
			if(player.body.getPosition().x > position.x) {
				sprite.get(0).setScale(-0.1f, 0.1f);
				direction = RIGHT;
				body.setLinearVelocity(CHASE_SPEED * direction, GRAVITY);
			}
			// 左にいる時
			if(player.body.getPosition().x < position.x) {
				sprite.get(0).setScale(0.1f, 0.1f);
				direction = LEFT;
				body.setLinearVelocity(CHASE_SPEED * direction, GRAVITY);
			}
		}

		// すぐ近くにプレイヤーがきた時
		if(	player.body.getPosition().x > position.x - 10 && player.body.getPosition().x < position.x + 10 &&
			player.body.getPosition().y > position.y - 10 && player.body.getPosition().y < position.y + 10 ) {
			// 攻撃フラグON
			attackFlag = true;
		}
	}

	/**************************************************
	* attack
	* 手裏剣での攻撃と刀での攻撃
	**************************************************/
	public void attack() {
		// 配列が空の時
		if (syuriken == null) {
			syuriken = new ArrayList<Weapon>(MAX_SYURIKEN);
			syuriken.add(new Weapon("手裏剣", this, 0));		// 0は手裏剣
			syuriken.get(0).SetUseFlag(true);
		}
		// 空じゃないかつ最大数以下より
		else if (syuriken != null && syuriken.size() < MAX_SYURIKEN) {
			syuriken.add(new Weapon("手裏剣", this, 0));		// 0は手裏剣
			syuriken.get(syuriken.size() - 1).SetUseFlag(true);
		}

		// 最大数になったら空に戻す
		if (syuriken.size() == MAX_SYURIKEN)
			syuriken = null;

		attackInterval = INTERVAL;
		/*
		// TODO WeaponManager要調整　刀での攻撃も後で追加
		if ( WeaponManager.enemyWeaponList.size() == 0 && attackInterval == 0)
				WeaponManager.CreateWeapon("手裏剣", this, 0);		// 0は手裏剣
		*/
	}

	/**************************************************
	* jump
	**************************************************/
	// TODO 要調整
	public void jump() {
		if(!jump) {
			jump = true;
			velocity.y = JUMP_POWER;
		}
		if(jump) {
			body.setLinearVelocity(body.getLinearVelocity().x, velocity.y);
			velocity.y -= 1f;
		}
	}

	/**************************************************
	// Auto
	// Artificial Intelligence
	**************************************************/
	public void AutoEnemy() {
	}

	/**************************************************
	 * 当たり判定取得
	**************************************************/
	// TODO ジャンプの接地判定と、ダメージくらったらノンアクティブをアクティブに変更

	public void collisionDispatch(ObJectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	@Override
	public void collisionNotify(Background obj, Contact contact){
		jump = false;
		body.setLinearVelocity(body.getLinearVelocity().x, GRAVITY);
	}

	@Override
	public void collisionNotify(Player obj, Contact contact){}

	@Override
	public void collisionNotify(Enemy obj, Contact contact){}

	@Override
	public void collisionNotify(Effect obj, Contact contact){
		// 無敵じゃない時はダメージ
		if( invincibleTime == 0 ) {
			invincibleTime = 120;		// 無敵時間付与
			hp -= obj.GetAttackNum();
		}
		if( hp <= 0 ) {
			deleteFlag = true;
		}
	}

	@Override
	public void collisionNotify(Item obj, Contact contact){}

	@Override
	public void collisionNotify(StageObject obj, Contact contact){}

	@Override
	public void collisionNotify(Weapon obj, Contact contact){}

	/**************************************************
	* Get
	* ゲッターまとめ
	**************************************************/
	public Enemy GetEnemy() { return this; }
	public int GetType(){ return enemyType; }
	public int GetNum(){ return number; }
	public int GetDirection() { return direction; }

	/**************************************************
	* Set
	* セッターまとめ
	**************************************************/
	public void SetNum(int num){ number = num; }

}