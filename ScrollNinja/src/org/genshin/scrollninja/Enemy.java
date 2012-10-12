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
// 10/9	座標は動いてるけど絵が付いてってない。
//		当たり判定と同期するから先に当たり判定作る。
// 10/11 ジャンプ、移動、強制追跡(Xが越えると)
//		 マネージャ、アップデートの引数をworldに
//		 二段ジャンプ制御

public class Enemy extends CharacterBase {
	//========================================
	// 定数宣言
	// 敵の種類
	//========================================
	private final static int WALKENEMY		=  0;			// 歩兵?
	private final static int JUMPENEMY		=  1;			// ジャンプしてくるやつ
	private final static int CHASEENEMY	=  2;			// 追跡
	private final static int DASHENEMY		=  3;			// 走り
	private final static int ATTACKENEMY	=  4;			// 攻撃
	private final static int AUTOENEMY		=  5;			// オート
	private final static int HIDEENEMY		=  6;			// 隠密
	private final static int RUSH			=  7;			// 突撃
	
	
	private final static int RIGHT			=  5;
	private final static int LEFT			= -5;
	
	// 変数宣言
	private String			name;			// 呼び出す時の名前
	private int				enemyType;		// 敵の種類
	private int				direction;		// 向いてる方向
	private int				invincible;		// 無敵時間
	private float			stateTime;		// 
	private TextureRegion	frame[];		// アニメーションのコマ
	private TextureRegion	nowFrame;		// 現在のコマ
	private Animation		animation;		// アニメーション
	
	private Player player;
	
	// モーション
	private boolean jump;
	private float fall;
	
	// コンストラクタ 
	Enemy(String Name, int type, Vector2 pos) {
		name		= new String(Name);
		enemyType	= type;
		position	= pos;
		hp			= 100;
		attackNum	= 0;
		speed		= 0;
		
		Create();
	}
	
	// コンストラクタ 
	Enemy(String Name, int type, float x, float y) {
		name		= new String(Name);
		enemyType	= type;
		position.x	= x;
		position.y	= y;
		hp			= 100;
		attackNum	= 0;
		speed		= 0;
		
		jump = false;
		fall = 0.0f;
		
		Create();
	}
	
	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public void Update(World world) {
		sprite.setPosition(position.x - 32, position.y - 32);
		sprite.setRotation((float) (body.getAngle()*180/Math.PI));
		
		position = body.getPosition();
		body.setTransform(position ,0);
		nowFrame = animation.getKeyFrame(stateTime, true);
		stateTime ++;
		
		Move();
		Gravity(world);
		
		sprite.setRegion(nowFrame);
		body.setTransform(position, body.getAngle());
	}
	
	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public void Draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	//************************************************************
	// Create
	// テクスチャの読み込みとかスプライトのセットとかやる
	//************************************************************
	public void Create() {
		switch(enemyType) {
		case WALKENEMY:
			// テクスチャの読み込み
			Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion region = new TextureRegion(texture, 50, 0, 64, 64);

			// スプライトに反映
			sprite = new Sprite(region);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

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
		case JUMPENEMY:
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
			WalkEnemy(player);
			JumpEnemy();
			break;
		case JUMPENEMY :
			break;
		case CHASEENEMY :
			break;
		case DASHENEMY :
			break;
		case ATTACKENEMY :
			break;
		case AUTOENEMY :
			break;
		case HIDEENEMY :
			break;
			
		
		}
	}
	private float enemywalkspd = 2.5f;
	
	//************************************************************
	// walk
	// chase
	//************************************************************
	public void WalkEnemy(Player player) {
		
		player = PlayerManager.GetPlayer("プレイヤー");

		// プレイヤーのX座標が敵のX座標より右にあるとき
		if(player.position.x > position.x ) {
			enemywalkspd = -4.0f;

			System.out.print("teki");
			System.out.println(position.x);
			System.out.print("player");
			System.out.println(player.position.x);
		}
		else if(player.position.x < position.x)
			position.x += enemywalkspd * 1.1 ;
		else
			position.x += enemywalkspd;
			
		
	}
	
	//************************************************************
	// jump
	//************************************************************
	public void JumpEnemy() {
		
		if(!jump) {
			// 上押したらジャンプ！
			if (Gdx.input.isKeyPressed(Keys.A)) {
				jump = true;
				fall = 15.0f;
			}
		}
		
		if(jump) {
			position.y += fall;
		}
		
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
	// Get
	// ゲッターまとめ
	//************************************************************
	public Enemy GetEnemy() { return this; }
	public String GetName(){ return name; }
	
	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
}
