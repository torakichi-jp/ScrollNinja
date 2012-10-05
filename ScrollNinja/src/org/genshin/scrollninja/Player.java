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
	private static final float FIRSTSPEED	=  5.0f;		// 初速度
	private static final float GRAVITY		= -0.98f;		// 重力
	
	private static final int RIGHT			= -1;
	private static final int LEFT			=  1;
	private static final int STAND			=  0;
	private static final int DASH			=  1;
	private static final int JUMP			=  2;
	private static final int ATTACK			=  3;
	
/*	enum State {
		STAND,
		WALK
	};*/
	
	// 変数宣言
	private int				charge;					// チャージゲージ
	private int				money;					// お金
	private int				direction;				// 向いてる方向
	private int				currentState;			// 現在の状態
	private int				jumpCount;				// ジャンプカウント
	private float			velocity;				// 移動量
	private float			fall;					// 落下量
	private Weapon			weapon;					// 武器のポインタ
	private boolean 		jump;					// ジャンプフラグ
	private Animation 		standAnimation;			// 立ちアニメーション
	private Animation 		dashAnimation;			// ダッシュアニメーション
	private Animation 		jumpAnimation;			// ジャンプアニメーション
	private Animation 		attackAnimation;		// 攻撃アニメーション
	
	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public Vector2 GetPosition() { return position; }
	
	// コンストラクタ
	public Player() {
		// テクスチャの読み込み
		Texture texture = new Texture(Gdx.files.internal("data/char.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);
		
		// スプライトに反映
		sprite = new Sprite(region);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		
		charge		 = 0;
		money		 = 0;
		direction	 = 1;
		currentState = STAND;
		velocity	 = 0;
		weapon		 = WeaponManager.GetInstace().GetWeapon("");
		jump		 = false;
	}
	
	//************************************************************
	// Update
	// 更新処理はここにまとめる
	//************************************************************
	public void Update(World world) {		
		position = body.getPosition();
		
		Stand(world);		// 立ち処理
		Jump(world);		// ジャンプ処理
		Move(world);		// 移動処理
		Gravity(world);		// 重力計算処理
		
		body.setTransform(position, body.getAngle());
	}
	
	//************************************************************
	// Draw
	// 描画処理はここでまとめる
	//************************************************************
	public void Draw(SpriteBatch batch) {
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
		if( GetGroundJudge(world) ) {
			// 上押したらジャンプ！
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				jump = true;
				jumpCount = 0;
				currentState = JUMP;
			}
		}
		
		// ジャンプ中の処理
		if( jump ) {
			jumpCount ++;
			position.y -= FIRSTSPEED;
			
			// ジャンプ終わり
			if( jumpCount > 20) {
				jumpCount = 0;
				jump = false;
			}
		}
	}
	
	//************************************************************
	// Gravity
	// 重力計算処理。常にやってます
	//************************************************************
	private void Gravity(World world) {
		// 空中にいる時は落下移動
		if(!GetGroundJudge(world)) {
			fall = jumpCount *= GRAVITY;
			position.y += fall;
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
			
			if( GetGroundJudge(world) ) {	// もし地面なら歩くモーションにするので現在の状態を歩きに。
				currentState = DASH;
			}
		}
		// 左が押された
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			direction = LEFT;
			position.x += direction;
			
			if( GetGroundJudge(world) ) {
				currentState = DASH;
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
				return true;
			}
		}
		return false;
	}
}
