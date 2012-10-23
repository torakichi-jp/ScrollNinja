package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

//========================================
//クラス宣言		インターフェース表示を扱うクラス
//========================================
public class Interface {
	private static Sprite scroll;		// HPの巻物本体部分
	private static Sprite scrollRight;	// 巻物の右端
	private static Sprite hp;			// プレイヤーHP
	private static Sprite hyoutan;		// チャクラのひょうたん部分
	private static Sprite chakra;		// プレイヤーチャクラ
	//private static Sprite map;			// マップ
	//private static Sprite quitPause;	// ポーズ画面から抜ける用
	//private static Sprite icon;			// 現在地アイコン
	private ArrayList<Sprite> weapon;	// 武器

	//private static Sprite pauseMenu;	// ポーズメニュー
	private boolean worldmapflag;

	private static Sprite returnGame;				// ゲームに戻る
	private static Sprite title;					// タイトル
	//private static Sprite weaponReinforcement;	// 武器強化
	private static Sprite load;						// ロード

	private Sprite worldMap;					// ワールドマップ(仮)

	private Animation scrollAnimation;	// 巻物のアニメーション
	private TextureRegion nowFrame;		// 巻物の現在のコマ
	private float stateTime;			// アニメーション用
	private Player player;				// プレイヤー情報格納
	private GameMain gamemain;
	private float percentHP;			// 現在のHPの割合　1が最大
	private float countHP;				// 巻物を0.01ずつ現在のHPの割合まで動かすためのカウンタ
	private float percentChakra;		// 現在のチャクラの割合　1が最大
	private float countChakra;			// 巻物を0.01ずつ現在のチャクラの割合まで動かすためのカウンタ
	public static boolean calculateHP;		// HP計算
	public static boolean calculateChakra;	// チャクラ計算
	private static float transrateX;		// X移動量
	private boolean stopHP;
	private boolean stopChakra;
	private boolean pauseFlag;			// ポーズフラグ

	private final static float ICONPOSITIONX = 0.155f;
	private final static float ICONPOSITIONY = 0.28f;
	private final static float ICONSCROLL    = 0.095f;

	/*
	 *icon,map comment out
	 *worldmap : move character on map and animation while pause screen
	 * 10/23 if press [o] draw worldmap , press [p] to close worldmap
	 * */

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

		// アイコン(仮)
		Texture icontexture = new Texture(Gdx.files.internal("data/shuriken.png"));
		icontexture.setFilter(TextureFilter.Linear,TextureFilter.Linear);

		Texture pauseMenuTexture = new Texture(Gdx.files.internal("data/menu.png"));
		pauseMenuTexture.setFilter(TextureFilter.Linear,TextureFilter.Linear);

		// 巻物アニメーション
		TextureRegion[][] tmp = TextureRegion.split(texture, 128, 128);
		TextureRegion[] frame = new TextureRegion[3];
		int index = 0;
		for (int i = 0; i < frame.length; i++)
			frame[index++] = tmp[0][i];
		scrollAnimation = new Animation(5.0f, frame);
		scroll = new Sprite(scrollAnimation.getKeyFrame(0, false));
		scroll.setOrigin(scroll.getWidth() * 0.5f, scroll.getHeight() * 0.5f);
		scroll.setScale(ScrollNinja.scale);

		nowFrame = scrollAnimation.getKeyFrame(0, false);

		// HP部分
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 128, 512, 128);
		hp = new Sprite(tmpRegion);
		hp.setOrigin(hp.getWidth() * 0.5f, hp.getHeight() * 0.5f);
		hp.setScale(ScrollNinja.scale);

		// 巻物の右端部分
		tmpRegion = new TextureRegion(texture, 384, 0, 128, 128);
		scrollRight = new Sprite(tmpRegion);
		scrollRight.setOrigin(scrollRight.getWidth() * 0.5f, scrollRight.getHeight() * 0.5f);
		scrollRight.setScale(ScrollNinja.scale);

		// ひょうたん
		tmpRegion = new TextureRegion(texture, 0, 256, 128, 128);
		hyoutan = new Sprite(tmpRegion);
		hyoutan.setOrigin(hyoutan.getWidth() * 0.5f, hyoutan.getHeight() * 0.5f);
		hyoutan.setScale(ScrollNinja.scale);

		// チャクラ
		tmpRegion = new TextureRegion(texture, 128, 256, 128, 128);
		chakra = new Sprite(tmpRegion);
		chakra.setOrigin(chakra.getWidth() * 0.5f, chakra.getHeight() * 0.5f);
		chakra.setScale(ScrollNinja.scale);

		// ポーズメニュー
		TextureRegion returnGameRegion = new TextureRegion(pauseMenuTexture,0,0,256,35);
		returnGame = new Sprite(returnGameRegion);
		returnGame.setScale(ScrollNinja.scale);

		TextureRegion titleRegion = new TextureRegion(pauseMenuTexture,0,40,256,35);
		title = new Sprite(titleRegion);
		title.setScale(ScrollNinja.scale);

		TextureRegion loadRegion = new TextureRegion(pauseMenuTexture,0,85,256,35);
		load = new Sprite(loadRegion);
		load.setScale(ScrollNinja.scale);

		// ワールドマップ
		Texture worldMaptexture = new Texture(Gdx.files.internal("data/worldmap.png"));
		worldMaptexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion worldRegion = new TextureRegion(worldMaptexture);
		worldMap = new Sprite(worldRegion);
		//worldMap.setOrigin(worldMap.getWidth() * 0.5f,worldMap.getHeight() * 0.5f);
		worldMap.setScale(ScrollNinja.scale);
		// フルスクリーン時
		//worldMap.setPosition(-505,-540);

		// マップ ワールドマップ or ミニマップ
		/*
		TextureRegion maptmpRegion = new TextureRegion(maptexture);
		map = new Sprite(maptmpRegion);
		map.setOrigin(map.getWidth() * 0.5f , map.getHeight() * 0.5f);
		map.setScale(ScrollNinja.scale * 0.1f);

		// ポーズ終了
		TextureRegion pauseRegion = new TextureRegion(pausetexture);
		quitPause = new Sprite(pauseRegion);
		quitPause.setOrigin(quitPause.getWidth() * 0.5f, quitPause.getHeight() * 0.5f);
		quitPause.setScale(ScrollNinja.scale);

		TextureRegion iconRegion = new TextureRegion(icontexture);
		icon = new Sprite(iconRegion);
		icon.setOrigin(icon.getWidth() * 0.5f,icon.getHeight() * 0.5f);
		icon.setScale(ScrollNinja.scale * 0.4f);
*/

		// 最初の設定；
		percentHP = 1;
		countHP = percentHP;
		percentChakra = 0;
		countChakra = percentChakra;
		calculateHP = false;
		calculateChakra = false;
		transrateX = 0;
		stopHP = true;
		stopChakra = true;
		worldmapflag = false;
		stateTime = 0;
	}

	public void update() {
		// 描画位置セット
		Vector2 cameraPosition = new Vector2(GameMain.camera.position.x, GameMain.camera.position.y);
		scroll.setPosition(cameraPosition.x - 64 - (ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 6.4f),
						cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		hp.setPosition
			(cameraPosition.x - 256 - (ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 25.6f) - transrateX,
			cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		scrollRight.setPosition(cameraPosition.x - 64 -
							(ScrollNinja.window.x * 0.5f * ScrollNinja.scale - 6.4f) + 44.5f - transrateX,
							cameraPosition.y - 64 + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale -6.4f));
		/*
		hyoutan.setPosition(scroll.getX() + 51.2f, scroll.getY());
		chakra.setPosition(hyoutan.getX(), hyoutan.getY());
		*/

		// 位置調整
		/*
		map.setPosition(cameraPosition.x - map.getWidth() * 0.5f +
						(ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - map.getWidth() * 0.5f * 0.01f,
				cameraPosition.y - map.getHeight() * 0.5f + (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)
																			- map.getHeight() * 0.5f * 0.01f);
*/

		returnGame.setPosition(cameraPosition.x - returnGame.getWidth() * 0.5f
								+ (ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - returnGame.getWidth() * 0.5f * 0.1f,
								cameraPosition.y - returnGame.getHeight() * 0.5f
								+ (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)- returnGame.getHeight() * 0.5f * 0.5f);

		title.setPosition(cameraPosition.x - title.getWidth() * 0.5f
							+ (ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - title.getWidth() * 0.5f * 0.1f,
							cameraPosition.y - title.getHeight() * 0.5f
							+ (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)- title.getHeight() * 0.5f * 0.7f);

		load.setPosition(cameraPosition.x - load.getWidth() * 0.5f
				+ (ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - load.getWidth() * 0.5f * 0.1f,
				cameraPosition.y - load.getHeight() * 0.5f
				+ (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)- load.getHeight() * 0.5f * 0.9f);

		worldMap.setPosition(cameraPosition.x - worldMap.getWidth() * 0.5f
				+ (ScrollNinja.window.x * 0.5f * ScrollNinja.scale) - worldMap.getWidth() * 0.5f * 0.12f,
				cameraPosition.y - worldMap.getHeight() * 0.5f
				+ (ScrollNinja.window.y * 0.5f * ScrollNinja.scale)- worldMap.getHeight() * 0.5f * 0.09f);

		// HPに変動があれば計算
		if (calculateHP)
			calculateHP();

		// チャクラに変動があれば計算
		if (calculateChakra)
			calculateChakra();

		// HP回復　1フレームで0.01ずつ増加
		if ( !stopHP && countHP < 0 ) {
			countHP += 0.01f;
			hp.scroll(0.0085f, 0);
			transrateX -= 0.45f;

			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP_REVERSED);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);

			if (countHP > 0)
				stopHP = true;
		}
		// HP減る　1フレームで0.01ずつ減少
		if ( !stopHP && countHP > 0 ) {
			countHP -= 0.01f;
			hp.scroll(-0.0085f, 0);
			transrateX += 0.45f;

			stateTime += 1;
			scrollAnimation.setPlayMode(Animation.LOOP);
			nowFrame = scrollAnimation.getKeyFrame(stateTime, true);
			scroll.setRegion(nowFrame);

			if (countHP < 0)
				stopHP = true;
		}

		// チャクラ増える　1フレームで0.01ずつ増加
		if ( !stopChakra && countChakra < 0 ) {
			countChakra += 0.01f;
			chakra.scroll(0, -0.01f);

			if (countChakra > 0)
				stopChakra = true;
		}
		// チャクラ減る　1フレームで0.01ずつ減少
		if ( !stopChakra && countChakra > 0 ) {
			countChakra -= 0.01f;
			chakra.scroll(0, 0.01f);

			if (countChakra < 0)
				stopChakra = true;
		}

		if (stateTime > 60)
			stateTime = 0;

		// マップの表示
		Map();

		if(Gdx.input.isKeyPressed(Keys.O)) {
			worldmapflag = true;
		}
		if(Gdx.input.isKeyPressed(Keys.P)) {
			worldmapflag = false;
		}

		// アイコン
		/*
		if(!pauseFlag) {
		icon.setPosition( cameraPosition.x - icon.getWidth() * ICONPOSITIONX + PlayerManager.GetPlayer(0).GetPosition().x * ICONSCROLL
							+ (ScrollNinja.window.x * 0.5f * 0.02f) - icon.getWidth() * 0.5f * 0.01f,
							cameraPosition.y - icon.getHeight() * ICONPOSITIONY + PlayerManager.GetPlayer(0).GetPosition().y * ICONSCROLL
							+ (ScrollNinja.window.y * 0.5f * 0.02f) - icon.getHeight() * 0.5f * 0.01f);
		}*/
	}

	public void calculateHP() {
		countHP = percentHP;
		// プレイヤー情報取得
		player = PlayerManager.GetPlayer(0);
		// 現在の割合を取得
		percentHP = (float)player.GetHP() / (float)player.GetMaxHP();
		// いくつ減らすか計算
		countHP -= percentHP;
		calculateHP = false;
		stopHP = false;
	}

	public void calculateChakra() {
		countChakra = percentChakra;
		// プレイヤー情報取得
		player = PlayerManager.GetPlayer(0);
		// 現在の割合を取得
		percentChakra = (float)player.GetChakra() / (float)player.GetMaxChakra();
		// いくつ減らすか計算
		countChakra -= percentChakra;
		calculateChakra = false;
		stopChakra = false;
	}

	public void Map() {
		if(Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			System.out.print("mouseX:");
			System.out.println(x);
			System.out.print("mouseY:");
			System.out.println(y);
			//if(x > 600 && y < 150)
				//pauseFlag = true;
		}

		if(Gdx.input.isKeyPressed(Keys.M)) {

			pauseFlag = true;
		}

		if(pauseFlag) {
			//map.setScale(0.04f);
			/*
			 * マップの絵ができたら座標など変更する
			 * (512*512)
			 * */
			/*
			map.setPosition(GameMain.camera.position.x - map.getWidth() * 0.5f,
					GameMain.camera.position.y - map.getHeight() * 0.502f);
			quitPause.setPosition(GameMain.camera.position.x - quitPause.getWidth() * 0.5f +
									ScrollNinja.window.x * 0.5f * ScrollNinja.scale
									 - quitPause.getWidth() * 0.5f * ScrollNinja.scale,
					GameMain.camera.position.y - quitPause.getHeight() * 0.5f +
									ScrollNinja.window.y * 0.5f * ScrollNinja.scale
									 - quitPause.getHeight() * 0.5f * ScrollNinja.scale);

			icon.setPosition(PlayerManager.GetPlayer(0).position.x,PlayerManager.GetPlayer(0).position.y);
			*/

			// ゲーム進行をストップ
			GameMain.gameState = GameMain.GAME_PAUSED;
		}
		else {
			// 解除されたらマップサイズ戻す
			//map.setScale(ScrollNinja.scale * 0.1f);
		}
	}

	public void SetPauseFlag(boolean pauseflag) {
		pauseFlag = pauseflag;
	}
	public Sprite GetReturnGame(){return returnGame;}

	public void Draw() {
		hp.draw(GameMain.spriteBatch);
		scrollRight.draw(GameMain.spriteBatch);
		scroll.draw(GameMain.spriteBatch);
		chakra.draw(GameMain.spriteBatch);
		hyoutan.draw(GameMain.spriteBatch);
		//map.draw(GameMain.spriteBatch);

		if(pauseFlag) {
			/*quitPause.draw(GameMain.spriteBatch);
			icon.setScale(ScrollNinja.scale);
			icon.draw(GameMain.spriteBatch);
			*/

			returnGame.draw(GameMain.spriteBatch);
			title.draw(GameMain.spriteBatch);
			load.draw(GameMain.spriteBatch);

			if(worldmapflag) {
				worldMap.draw(GameMain.spriteBatch);
			}
		}
		else{
			//icon.setScale(ScrollNinja.scale * 0.4f);
			//icon.draw(GameMain.spriteBatch);
		}
	}
}