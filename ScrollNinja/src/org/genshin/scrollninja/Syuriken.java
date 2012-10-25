package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

// TODO エネミー用しか作っていない
public class Syuriken extends WeaponBase {
	private final int SYURIKEN_SPEED = 30;
	private final int EXIST_TIME = 240;
	private int timeCount;

	private float rotate = 0;
	private Player player;
	private Enemy enemy;

	/**
	 * コンストラクタ
	 * @param player	使用プレイヤー
	 * @param i			管理番号
	 */
	public Syuriken(Player player, int i) {
		owner		= (CharacterBase)player;	// 使用者
		this.player	= player;
		this.enemy 	= null;
		number		= i;						// 管理番号
		level		= 2;						// レベル
		attackNum	= (level * 5);				// 攻撃力 TODO （てきとー）
		position 	= new Vector2(0.0f, 0.0f);
		use			= true;

		Create(this.player, this.enemy);
	}

	/**
	 * コンストラクタ
	 * @param enemy		使用エネミー
	 * @param i			管理番号
	 */
	public Syuriken(Enemy enemy, int i) {
		owner		= (CharacterBase)enemy;		// 使用者
		this.player	= null;
		this.enemy	= enemy;					//
		number		= i;						// 管理番号
		level		= 2;						// レベル
		attackNum	= (level * 5);				// 攻撃力 TODO （てきとー）
		position 	= new Vector2(0.0f, 0.0f);
		use			= true;

		Create(this.player, this.enemy);
	}

	/**************************************************
	* create
	* 武器生成
	**************************************************/
	public void Create(Player player, Enemy enemy) {
		sprite = new ArrayList<Sprite>();
		sensor = new ArrayList<Fixture>();

		// テクスチャー読み込み
		// 手裏剣テクスチャの位置はとりあえずなので後で要調整
		Texture	texture = new Texture(Gdx.files.internal("data/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 448, 64, 64);

		// スプライト反映
		sprite.add(new Sprite(region));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(ScrollNinja.scale * 0.5f);

		// Body作成
		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;
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

		sensor.add(body.createFixture(fd));	// センサーに追加
		sensor.get(0).setUserData(this);	// 当たり判定用にUserDataセット
		sensor.get(0).setSensor(true);		// 他の当たり判定に影響しない
		body.setBullet(true);				// すり抜け防止
		body.setFixedRotation(true);		// シミュレーションでの自動回転をしない
		body.setGravityScale(0);			// 重力の影響を受けない

		// プレイヤーの武器の設定
		if (player != null) {

		}

		// エネミーの武器の設定
		if (enemy != null) {
			timeCount = EXIST_TIME;				// 240で消える

			// 出現位置
			Vector2 current = new Vector2(enemy.body.getPosition());
			body.setTransform(current.x + 3 * enemy.GetDirection(), current.y, 0);
			// 角度を求める
			// TODO 現在操作中のプレイヤー情報を求められるように変更必要あり
			Vector2 terget = new Vector2(PlayerManager.GetPlayer(0).body.getPosition());
			float rad = (float) Math.atan2(terget.y - current.y, terget.x - current.x);
			// 移動速度を求める
			Vector2 vel = new Vector2(0, 0);
			vel.x = (float) (Math.cos(rad) * SYURIKEN_SPEED);
			vel.y = (float) (Math.sin(rad) * SYURIKEN_SPEED);

			body.setLinearVelocity(vel.x, vel.y);
		}
	}

	/**
	 * 更新
	 */
	public void Update() {
		// 手裏剣表示時間
		timeCount -= 1;
		// 回転
		rotate++;

		position = body.getPosition();
		body.setTransform(position, rotate);

		// 消滅
		if(timeCount < 0) {
			use = false;
			timeCount = EXIST_TIME;
		}
	}

	/**
	 * 当たり判定
	 */
	@Override
	public void collisionDispatch(ObJectBase obj, Contact contact) {
		obj.collisionNotify(this, contact);
	}

	@Override
	public void collisionNotify(Background obj, Contact contact){}

	@Override
	public void collisionNotify(Player obj, Contact contact){
		use = false;
	}

	@Override
	public void collisionNotify(Enemy obj, Contact contact){}

	@Override
	public void collisionNotify(Effect obj, Contact contact){}

	@Override
	public void collisionNotify(Item obj, Contact contact){}

	@Override
	public void collisionNotify(StageObject obj, Contact contact){}

	@Override
	public void collisionNotify(WeaponBase obj, Contact contact){}
}