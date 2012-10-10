package org.genshin.scrollninja;

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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/2 制作開始
// 10/3 変数と空の関数を実装
// 		ジャンプと移動だけ先に明日実装！
// 10/4 ジャンプと移動は実装完了だけど実行してない
//		明日アニメーション関連進行。今週までに表示までいきたい
// 10/8 移動だけ動作確認。段差のところで空中移行になってるのを直そう
//		重力弱いから要調整。ジャンプできてねー＾ｑ＾

// *メモ*
// 攻撃はダッシュしながら攻撃可能（足は止まらない）
// 右クリック押しっぱなしで伸び続ける
// 壁とかに付いた後も押しっぱでそっちに移動
// 壁とかに付いた状態で離すとブラーン
// もう一回右クリックで離す

//========================================
// クラス宣言
//========================================
public class Player extends CharacterBase {

	// 定数宣言
	private static final float FIRSTSPEED	=  15.0f;		// 初速度
	private static final float GRAVITY		= -0.98f;		// 重力

	private static final int RIGHT			=  5;
	private static final int LEFT			= -5;
	private static final int STAND			=  0;
	private static final int WALK			=  1;
	private static final int DASH			=  2;
	private static final int JUMP			=  3;
	private static final int ATTACK			=  4;

	// 変数宣言
	private int				charge;					// チャージゲージ
	private int				money;					// お金
	private int				direction;				// 向いてる方向
	private int				currentState;			// 現在の状態
	private float			fall;					// 落下量
	private float			prevAngle;				// 前回角度
	private float			stateTime;
	private Weapon			weapon;					// 武器のポインタ
	private boolean			jump;					// ジャンプフラグ
	private Animation		standAnimation;			// 立ちアニメーション
	private Animation		walkAnimation;			// 歩きアニメーション
	private Animation		dashAnimation;			// ダッシュアニメーション
	private Animation		jumpAnimation;			// ジャンプアニメーション
	private Animation		attackAnimation;		// 攻撃アニメーション
	private Animation		footWalkAnimation;		// 下半身・歩きアニメーション
	private Sprite			footSprite;				// 下半身用のスプライト
	private TextureRegion[]	frame;					// アニメーションのコマ
	private TextureRegion	nowFrame;				// 現在のコマ
	private TextureRegion	nowFootFrame;			// 下半身用の現在のコマ

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Vector2 GetPosition() { return position; }
	public Sprite GetSprite(String type) {
		if (type.equals("BODY"))
			return sprite;
		else
			return footSprite;
	}

	// コンストラクタ
	public Player() {
		// テクスチャの読み込み
		Texture texture = new Texture(Gdx.files.internal("data/player.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

		// スプライトに反映
		//sprite = new Sprite(region);
		//sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

		// アニメーション
		//Texture dash = new Texture(Gdx.files.internal("data/dash_test.png"));
		TextureRegion[][] tmp = TextureRegion.split(texture, 64, 64);

		// 下半身・歩き １行目６フレーム
		frame = new TextureRegion[6];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		footWalkAnimation = new Animation(3.0f, frame);

		// 上半身・歩き　２行目６フレーム
		frame = new TextureRegion[6];
		index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[1][i];
		walkAnimation = new Animation(3.0f, frame);

		// スプライトに反映 最初は立ちの第１フレーム
		// （※現在は用意されていないので歩きの第１フレームで代用）
		sprite = new Sprite(walkAnimation.getKeyFrame(0, true));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		footSprite = new Sprite(footWalkAnimation.getKeyFrame(0, true));
		footSprite.setOrigin(footSprite.getWidth() / 2, footSprite.getHeight() / 2);

		// 一番最初の表示　現在は歩きで代用
		nowFrame = walkAnimation.getKeyFrame(0, true);
		nowFootFrame = footWalkAnimation.getKeyFrame(0, true);

		charge		 = 0;
		money		 = 0;
		direction	 = 1;
		currentState = STAND;
		fall		 = 0;
		prevAngle	 = 0;
//		weapon		 = WeaponManager.GetInstace().GetWeapon("");
		jump		 = false;
	}

	//************************************************************
	// Update
	// 更新処理はここにまとめる
	//************************************************************
	public void Update(World world) {
		position = body.getPosition();
		body.setTransform(position ,0);
		sprite.setRegion(nowFrame);
		footSprite.setRegion(nowFootFrame);

		Stand(world);		// 立ち処理
		Jump(world);		// ジャンプ処理
		Move(world);		// 移動処理
		Gravity(world);		// 重力計算処理
		animation();		// アニメーション処理

	/*	if( prevAngle != body.getAngle() ) {
			body.setTransform(position ,0);
		}
	*/
		body.setTransform(position, body.getAngle());
	//	prevAngle = body.getAngle();
	}

	//************************************************************
	// Draw
	// 描画処理はここでまとめる
	//************************************************************
	public void Draw(SpriteBatch batch) {
		// 下半身から描画
		footSprite.draw(batch);
		sprite.draw(batch);
	}

	//************************************************************
	// Stand
	// 立ち処理。
	//************************************************************
	private void Stand(World world) {
		if( GetGroundJudge(world) ) {
			currentState = STAND;
		}
	}
	//************************************************************
	// Jump
	// ジャンプ処理。上押すとジャンプ！
	//************************************************************
	private void Jump(World world) {

		// 地面に接触しているならジャンプ可能
		if( /*GetGroundJudge(world)*/ !jump ) {
			// 上押したらジャンプ！
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				jump = true;
				currentState = JUMP;
				fall = 15;
				//System.out.println(fall);
			}
		}

		// ジャンプ中の処理
		if( jump ) {
			position.y += fall;
		}
	}

	//************************************************************
	// Gravity
	// 重力計算処理。常にやってます
	//************************************************************
	private void Gravity(World world) {
		// 空中にいる時は落下移動
		if(!GetGroundJudge(world)) {
			fall -= 0.25;
			position.y -= 5;
			if( fall < -5 ) {
				fall = -5;
			}
		}
	}

	//************************************************************
	// Move
	// 移動処理。左右押すと移動します
	// 状態遷移は空中にいなければ歩きに！
	//************************************************************
	private void Move(World world) {
		// 右が押された
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			direction = RIGHT;				// プレイヤーの向きを変更。
			position.x += direction;		// プレイヤーの移動
			sprite.setScale(-1, 1);
			footSprite.setScale(-1, 1);

			if( GetGroundJudge(world) ) {	// もし地面なら歩くモーションにするので現在の状態を歩きに。
				currentState = WALK;
			}
		}
		// 左が押された
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = LEFT;
			position.x += direction;
			sprite.setScale(1, 1);
			footSprite.setScale(1, 1);

			if( GetGroundJudge(world) ) {
				currentState = WALK;
			}
		}
	}

	//************************************************************
	// Attack
	// 攻撃処理。左クリックで攻撃
	//************************************************************
	private void Attack() {}

	// カギ縄
	private void Kaginawa(){}

	//************************************************************
	// animation
	// 現在の状態を参照して画像を更新
	//************************************************************
	private void animation() {
		switch(currentState) {
		case STAND:		// 立ち
			break;
		case WALK:		// 歩き
			nowFrame = walkAnimation.getKeyFrame(stateTime, true);
			nowFootFrame = footWalkAnimation.getKeyFrame(stateTime, true);
			stateTime ++;
			break;
		case DASH:		// 走り
			break;
		case JUMP:		// ジャンプ
			break;
		}
	}

	// 武器変更
	private void changeWeapon() {
	}

	//************************************************************
	// GetGroundJudge
	// 戻り値： true:地面接地		false:空中
	// 接触判定。長いのでここで関数化
	//************************************************************
	private boolean GetGroundJudge(World world) {
		List<Contact> contactList = world.getContactList();

		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);

			// 地面に当たったよ
			if(contact.isTouching() && ( contact.getFixtureA() == sensor || contact.getFixtureB() == sensor )) {
				jump = false;
				fall = 0;
				return true;
			}
		}
		return false;
	}
}
