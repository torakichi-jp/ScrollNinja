package org.genshin.scrollninja;

import java.util.ArrayList;
import java.util.Random;

import org.genshin.scrollninja.EnemyDataList.EnemyData;

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
	// 敵のタイプ
	// TODO 不要になるかも
	public final static int NORMAL		= 0;	// ノーマル
	public final static int RARE		= 1;	// レア
	public final static int AUTO		= 2;	// AI

	// 方向
	private final int RIGHT			=  1;
	private final int LEFT			= -1;

	// 手裏剣所持数
	private final int MAX_SYURIKEN	= 10;
	private final int INTERVAL		= 90;

	// 速度
	private final float WALK_SPEED	=  15f;		// 通常の歩く速度
	private final float JUMP_POWER	=  25f;		// ジャンプ加速度
	private final float CHASE_SPEED	=  25f;		// 追いかける時
	private final float GRAVITY		= -20f;		// 重力
	private Vector2 velocity;					// 移動用速度

	// 変数宣言
	private int					invincibleTime;	// 無敵時間

	private int					enemyID;		// 敵のタイプ
	private int					number;			// 管理番号
	private int					enemyMode;		// 敵のモード
	private int					direction;		// 向いてる方向
	private float				stateTime;		//
	private TextureRegion[]		frame;			// アニメーションのコマ
	private TextureRegion		nowFrame;		// 現在のコマ
	private Animation			animation;		// アニメーション
	private boolean				attackFlag;		// 攻撃可能フラグ
	private boolean 			jump;			// ジャンプフラグ
	private boolean				chase;			// 追いかけフラグ
	private boolean				deleteFlag;		// 削除フラグ
	private Player 				player;			// プレイヤー
	private ArrayList<Syuriken> syuriken;		// 手裏剣
	private WeaponBase			weapon;			// 刀
	private int					attackInterval;	// 攻撃間隔

	private Vector2				wanderingPosition;	// うろうろ場所用に出現位置を保存

	private Random 			rand;			// ランダム

	/**************************************************
	 * コンストラクタ
	 **************************************************/
	Enemy(int id, int num, Vector2 position) {
		enemyID				= id;
		number				= num;
		this.position		= position;
		direction			= RIGHT;

		EnemyData data = EnemyDataList.lead(id);
		MAX_HP				= data.maxHp;
		hp					= data.maxHp;
		speed				= data.speed;
		invincibleTime		= 0;
		attackInterval		= INTERVAL;
		velocity			= new Vector2(0, 0);
		wanderingPosition	= new Vector2(position);

		syuriken = null;
		weapon = null;
		// TODO とりあえず
		switch (data.haveWeapon.get(0)) {
			case 1:
				//weapon = new Katana(this, 0);
				break;
		}

		jump				= false;
		attackFlag			= false;
		chase				= false;
		deleteFlag			= false;

		enemyMode = data.enemyMode;

		Create();
	}

	/**************************************************
	* Create()
	* body、sensor、sprite、アニメーション作成
	**************************************************/
	public void Create() {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		// Body作成
		BodyDef bd	= new BodyDef();
		bd.type	= BodyType.DynamicBody;
		body = GameMain.world.createBody(bd);

		// fixture生成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(1.6f, 2.4f);

		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 0;
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
		//switch(enemyType) {
		//case NORMAL:
			// TODO エネミーによってアニメーション枚数が変わってくるなら変更必要あり
			// テクスチャの読み込み
			EnemyData data = EnemyDataList.lead(enemyID);
			Texture texture = new Texture(Gdx.files.internal(data.enemyFileName));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

			// スプライトに反映
			sprite.add(new Sprite(region));
			sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
			sprite.get(0).setScale(ScrollNinja.scale);

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

			//break;

		//case RARE:
			//break;
		//}
	}

	/**************************************************
	 * Update()
	 * 更新処理まとめ
	 **************************************************/
	public void Update() {
		if( deleteFlag ) {
			// TODO 落とす種類の選択と確率も必要か
			ItemManager.CreateItem(Item.ONIGIRI, position.x, position.y);
			EnemyManager.Deleteenemy(this);
			return;
		}

		if( invincibleTime > 0 ) invincibleTime --;		// 無敵時間の減少
		position = body.getPosition();					// 現在位置の更新

		Action();			// 行動
		Flashing();			// 点滅処理

		// 手裏剣更新
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

		//
		if (weapon != null)
			weapon.Update();

		// アニメーション更新
		nowFrame = animation.getKeyFrame(stateTime, true);
		stateTime ++;
		sprite.get(0).setRegion(nowFrame);
	}

	/**************************************************
	* Draw() ObjectBaseのDrawをオーバーライド
	* スプライトを描画する。
	**************************************************/
	@Override
	public void Draw()
	{
		Vector2 pos = body.getPosition();
		float rot = (float) Math.toDegrees(body.getAngle());

		int count = sprite.size();
		for (int i = 0; i < count; ++i)
		{
			Sprite current = sprite.get(i);
			// 座標・回転
			current.setPosition(pos.x - current.getOriginX(), pos.y - current.getOriginY());
			current.setRotation(rot);
			// 描画
			current.draw(GameMain.spriteBatch);
		}

		// 武器の描画
		if (syuriken != null) {
			for (int i = 0; i < syuriken.size(); i++)
				syuriken.get(i).Draw();
		}
	}

	/**************************************************
	* Action()
	* 行動の分岐
	**************************************************/
	public void Action() {
		switch(enemyMode) {
		case EnemyDataList.NON_ACTIVE :
			walk();
			/*
			int i = rand.nextInt(10);
			System.out.println(i);
			if (i == 1)
				jump();
			*/
			break;
		case EnemyDataList.ACTIVE :
			walk();
			chase();
			if (attackFlag) {
				if (attackInterval == 0)
					if (weapon != null)
						attackKatana();
					else
						attackSyuriken();
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
	 * walk()
	 * 指定範囲内をうろうろしているだけ
	 **************************************************/
	public void walk() {
		if(!chase) {
			// 右端まで到達
			if(position.x > wanderingPosition.x + 20) {
				direction = LEFT;
			}
			// 左端まで到達
			if(position.x < wanderingPosition.x - 20) {
				direction = RIGHT;
			}
			sprite.get(0).setScale(ScrollNinja.scale * -direction, ScrollNinja.scale);
			body.setLinearVelocity(WALK_SPEED * direction, GRAVITY);
		}
	}

	/**************************************************
	* chase()
	* プレイヤーを見つけたら追いかける
	**************************************************/
	public void chase() {
		// プレイヤーの位置を取得
		player = PlayerManager.GetPlayer(0);

		// 一定距離まで近づいたら
		if (Math.abs(player.body.getPosition().x - position.x) < 20) {
			// 追いかけフラグON
			chase = true;
		}
		// 距離が離れたら
		if (Math.abs(player.body.getPosition().x - position.x) > 30 && chase) {
			// フラグOFF
			chase = false;
			attackFlag = false;
			// 現在の位置をうろうろ位置に設定
			wanderingPosition = new Vector2(position);
		}

		if (chase) {
			// プレイヤーのX座標が敵のX座標より右にあるとき
			if(player.body.getPosition().x > position.x) {
				direction = RIGHT;
			}
			// 左にいる時
			if(player.body.getPosition().x < position.x) {
				direction = LEFT;
			}
			sprite.get(0).setScale(ScrollNinja.scale * -direction, ScrollNinja.scale);
			body.setLinearVelocity(CHASE_SPEED * direction, GRAVITY);
		}

		// すぐ近くにプレイヤーがきた時
		if(	player.body.getPosition().x > position.x - 10 && player.body.getPosition().x < position.x + 10 &&
			player.body.getPosition().y > position.y - 10 && player.body.getPosition().y < position.y + 10 ) {
			// 攻撃フラグON
			attackFlag = true;
		}
	}

	/**************************************************
	* attackKatana()
	* 手裏剣での攻撃
	**************************************************/
	public void attackKatana() {
		weapon.SetUseFlag(true);

		attackInterval = INTERVAL;
	}
	/**************************************************
	* attackSyuriken()
	* 手裏剣での攻撃
	**************************************************/
	public void attackSyuriken() {
		if (syuriken == null) {
			syuriken = new ArrayList<Syuriken>(MAX_SYURIKEN);
			syuriken.add(new Syuriken(this, 0));
			syuriken.get(0).SetUseFlag(true);
		}
		// 空じゃないかつ最大数以下より
		else if (syuriken != null && syuriken.size() < MAX_SYURIKEN) {
			syuriken.add(new Syuriken(this, syuriken.size() - 1));
			syuriken.get(syuriken.size() - 1).SetUseFlag(true);
		}

		// 最大数になったら空に戻す
		if (syuriken.size() == MAX_SYURIKEN) {
			for (int i = 0; i < syuriken.size(); i++) {
				syuriken.get(i).Release();
			}
			syuriken = null;
		}

		attackInterval = INTERVAL;
	}

	/**************************************************
	* jump()
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
	 * 点滅処理
	 **************************************************/
	public void Flashing() {
		// 高速点滅
		if( invincibleTime != 0 ) {
			if( invincibleTime % 10 > 5 ) {
				for(int i = 0; i < sprite.size(); i ++) {
					sprite.get(i).setColor( 0, 0, 0, 0);
				}
			}
			else {
				for(int i = 0; i < sprite.size(); i ++) {
					sprite.get(i).setColor(1, 1, 1, 1);
				}
			}
		}
	}

	/**************************************************
	 * エディタ用
	 **************************************************/
	public void Edit() {
		body.setTransform(position,0);
		for( int i = 0; i < sprite.size(); i ++ ) {
			sprite.get(i).setPosition(position.x, position.y);
		}
	}

	/**************************************************
	 * 当たり判定取得
	**************************************************/
	// TODO ジャンプの接地判定要検証？

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
			enemyMode = EnemyDataList.ACTIVE;	// ノンアクティブをアクティブに
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
	public void collisionNotify(WeaponBase obj, Contact contact){}

	/**************************************************
	* Release　ObjectBaseのReleaseをオーバーライド
	* 解放処理まとめ
	**************************************************/
	@Override
	public void Release(){
		if (syuriken != null) {
			for (int i = 0; i < syuriken.size(); i++)  {
				syuriken.get(i).Release();
			}
		}
		GameMain.world.destroyBody(body);
		body = null;
		sprite.clear();
		sensor.clear();
	}

	/**************************************************
	* Get
	* ゲッターまとめ
	**************************************************/
	public Enemy GetEnemy() { return this; }
	public int GetType(){ return enemyID; }
	public int GetNum(){ return number; }
	public int GetDirection() { return direction; }

	/**************************************************
	* Set
	* セッターまとめ
	**************************************************/
	public void SetNum(int num){ number = num; }

}