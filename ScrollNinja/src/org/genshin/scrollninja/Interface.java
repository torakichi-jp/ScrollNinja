package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//========================================
//クラス宣言		インターフェース表示を扱うクラス
//========================================
public class Interface {
	private static Sprite scroll;		// HPの巻物本体部分
	private static Sprite scrollRight;	// 巻物の右端
	private static Sprite hp;			// プレイヤーHP
	private static Sprite hyoutan;		// チャクラのひょうたん部分
	private static Sprite chakra;		// プレイヤーチャクラ
	private static Sprite map;			// マップ
	private static Sprite quitPause;	// ポーズ画面から抜ける用
	private ArrayList<Sprite> weapon;	// 武器

	private Animation scrollAnimation;	// 巻物のアニメーション
	private TextureRegion nowFrame;		// 巻物の現在のコマ
	private float stateTime;			// アニメーション用

	private Player player;				// プレイヤー情報格納
	private GameMain gamemain;
	private float percentHP;			// 現在のHPの割合　1が最大
	private float countHP;				// 巻物を0.01ずつ現在のHPの割合まで動かすためのカウンタ
	private float percentChakra;		// 現在のチャクラの割合　1が最大
	private float countChakra;			// 巻物を0.01ずつ現在のチャクラの割合まで動かすためのカウンタ
	
	private boolean pauseFlag;			// ポーズフラグ

	// コンストラクタ
	public Interface() {
		weapon = new ArrayList<Sprite>();

		// テクスチャ画像読み込み
		Texture texture = new Texture(Gdx.files.internal("data/interface.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture maptexture = new Texture(Gdx.files.internal("data/stage_main.png"));
		maptexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture pausetexture = new Texture(Gdx.files.internal("data/shuriken.png"));
		pausetexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// 巻物アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 128, 128);
		TextureRegion[] frame = new TextureRegion[3];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		scrollAnimation = new Animation(5.0f, frame);
		scroll = new Sprite(scrollAnimation.getKeyFrame(0, false));
		scroll.setOrigin(scroll.getX() * 0.5f, scroll.getY() * 0.5f);
		scroll.setScale(0.1f);

		nowFrame = scrollAnimation.getKeyFrame(0, false);
		
		

		// HP部分
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 512, 128);
		hp = new Sprite(tmpRegion);
		hp.setOrigin(hp.getX() * 0.5f, hp.getY() * 0.5f);
		hp.setScale(0.1f);

		// 巻物の右端部分
		tmpRegion = new TextureRegion(texture, 384, 0, 128, 128);
		scrollRight = new Sprite(tmpRegion);
		scrollRight.setOrigin(scrollRight.getX() * 0.5f, scrollRight.getY() * 0.5f);
		scrollRight.setScale(0.1f);

		// ひょうたん
		tmpRegion = new TextureRegion(texture, 0, 256, 128, 128);
		hyoutan = new Sprite(tmpRegion);
		hyoutan.setOrigin(hyoutan.getX() * 0.5f, hyoutan.getY() * 0.5f);
		hyoutan.setScale(0.1f);

		// チャクラ
		tmpRegion = new TextureRegion(texture, 128, 256, 128, 128);
		chakra = new Sprite(tmpRegion);
		chakra.setOrigin(chakra.getX() * 0.5f, chakra.getY() * 0.5f);
		chakra.setScale(0.1f);
		
		// マップ
		TextureRegion maptmpRegion = new TextureRegion(maptexture);
		map = new Sprite(maptmpRegion);
		map.setOrigin(scrollRight.getX() , scrollRight.getY());
		
		// ポーズ終了
		TextureRegion pauseRegion = new TextureRegion(pausetexture);
		quitPause = new Sprite(pauseRegion);
		quitPause.setOrigin(scrollRight.getX(),scrollRight.getY());

		

		// 最初の設定；
		percentHP = 1;
		countHP = percentHP;
		percentChakra = 0;
		countChakra = percentChakra;
		
		pauseFlag = false;

		stateTime = 0;
	}

	public void update() {
		// 描画位置セット
		scroll.setPosition(GameMain.camera.position.x - (ScrollNinja.window.x * 0.5f * 0.1f),
						   GameMain.camera.position.y  - 12.8f + (ScrollNinja.window.y * 0.5f * 0.1f));
		scrollRight.setPosition(scroll.getX() + 42f, scroll.getY() + 0.5f);
		hp.setPosition(scroll.getX(), scroll.getY());
		hyoutan.setPosition(scroll.getX() + 51.2f, scroll.getY());
		chakra.setPosition(hyoutan.getX(), hyoutan.getY());
		// 位置調整
		map.setPosition(scroll.getX() + 60.0f, scroll.getY() + -5.0f);

		// プレイヤー情報取得
		player = PlayerManager.GetPlayer("プレイヤー");
		// 現在の割合を取得
		percentHP = player.GetHP() / player.GetMaxHP();
		//percentChakra = player.GetChakra() / player.GetMaxChakra();
		// いくつ減らすか計算
		countHP -= percentHP;
		countChakra -= percentChakra;

		// HP回復　1フレームで0.01ずつ増加
		if ( countHP > percentHP && countHP < 0.99 ) {
			countHP += 0.01f;
			hp.scroll(-0.01f, 0);
			hp.translateX(-0.51f);
			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP_REVERSED);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);
		}

		// HP減る　1フレームで0.01ずつ減少
		if ( countHP < percentHP && countHP > 0.01 ) {
			countHP -= 0.01f;
			hp.scroll(0.01f, 0);
			hp.translateX(0.51f);
			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);
		}

		// チャクラ増える　1フレームで0.01ずつ増加
		if ( countChakra > percentChakra && countChakra < 0.99 ) {
			countChakra += 0.01f;
			chakra.scroll(0, -0.01f);			System.out.println(scroll.getX());
			System.out.println(scroll.getY());
			
		}

		// チャクラ減る　1フレームで0.01ずつ減少
		if ( countChakra < percentChakra && countChakra > 0.01 ) {
			countChakra -= 0.01f;
			chakra.scroll(0, 0.01f);
		}

		if (stateTime > 60)
			stateTime = 0;
		// ポーズ
		Map();
	}
	
	public void Map() {
		
		if(Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			System.out.print("mouseX:");
			System.out.println(x);
			System.out.print("mouseY:");
			System.out.println(y);
			if(x > 600 && y < 150)
				pauseFlag = true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.M)) {
			// マップを表示
			// ゲーム進行をストップ
			pauseFlag = true;
		}
		if(pauseFlag) {
			map.setScale(0.04f);
			/* 
			 * マップの絵ができたら座標など変更する
			 * (512*512)
			 * */
			map.setPosition(scroll.getX(), scroll.getY() - 70);
			quitPause.setScale(0.1f);
			quitPause.setPosition(scroll.getX() + 50 ,scroll.getY() + 5);
		}
		else {
			map.setScale(0.01f);
		}
	}
	
	public boolean GetPauseFlag() {
		return pauseFlag;
	}
	public void SetPauseFlag(boolean pauseflag) {
		pauseFlag = pauseflag;
	}

	public void Draw() {
		scrollRight.draw(GameMain.spriteBatch);
		hp.draw(GameMain.spriteBatch);
		scroll.draw(GameMain.spriteBatch);
		chakra.draw(GameMain.spriteBatch);
		hyoutan.draw(GameMain.spriteBatch);
		map.draw(GameMain.spriteBatch);
		
		if(pauseFlag) 
			quitPause.draw(GameMain.spriteBatch);
	}
}