package org.genshin.scrollninja;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MainMenu implements Screen{
	private Game scrollNinja;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture texture;				// テクスチャー

	private Sprite modeContinue;			// コンティニュー
	private Sprite modeNewGame;				// ニューゲーム
	private Sprite modeLoadGame;			// ロードゲーム
	private Sprite modeNetwork;				// ネットワーク
	private Sprite modeOption;				// オプション
	private Sprite modeExit;				// エグジット

	private int spritePositionX;
	// 画像座標
	private final float MOVE_SPEED = 0.5f;	// スクロール時の移動速度
	private boolean scrollFlag;				// スクロールフラグ
	private boolean selectMenu;				// 選択したメニュー

	private final static int FADE_MENU = -50;		// 画像移動の終わり座標

	// コンストラクタ
	public MainMenu(Game game) {
		this.scrollNinja = game;
		/*
		switch (ScroolNinja.aspectRatio) {
		case XGA:	// 4:3
			camera = new OrthographicCamera(10.0f * 0.1f, 7.5f * 0.1f);
			break;
		case HD:	// 16:9
			camera = new OrthographicCamera(10.0f * 0.1f, 5.625f * 0.1f);
			break;
		case SXGA:	// 5:4
			camera = new OrthographicCamera(10.0f * 0.1f, 8.0f * 0.1f);
			break;
		case WUXGA:	// 16:10
			camera = new OrthographicCamera(10.0f * 0.1f, 6.25f * 0.1f);
			break;
		}
		*/
		// カメラ作成
		camera = new OrthographicCamera(ScrollNinja.window.x * 0.1f, ScrollNinja.window.y * 0.1f);
		// スプライトバッチ作成
		batch = new SpriteBatch();

		// テクスチャ読み込み
		try {
			texture = new Texture(Gdx.files.internal("data/menu_test.png"));
		} catch (NullPointerException e) {
			System.out.println("ファイルがありません");
		} catch (GdxRuntimeException e) {
			System.out.println("テクスチャサイズは２の乗数にしてください");
		}
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		spritePositionX = -105;

		// 位置は後で要調整
		// 選択肢コンテニュー
		TextureRegion region = new TextureRegion(texture, 0, 0, 256, 35);
		modeContinue = new Sprite(region);
		modeContinue.setPosition(spritePositionX, 0);
		modeContinue.setScale(0.1f);

		// 選択肢ニューゲーム
		region = new TextureRegion(texture, 0, 40, 256, 35);
		modeNewGame = new Sprite(region);
		modeNewGame.setPosition(spritePositionX, -4);
		modeNewGame.setScale(0.1f);

		// 選択肢ロードゲーム
		region = new TextureRegion(texture, 0, 85, 256, 35);
		modeLoadGame = new Sprite(region);
		modeLoadGame.setPosition(spritePositionX, -8);
		modeLoadGame.setScale(0.1f);

		// 選択肢ネットワーク
		region = new TextureRegion(texture, 0, 128, 256, 35);
		modeNetwork = new Sprite(region);
		modeNetwork.setPosition(spritePositionX, -12);
		modeNetwork.setScale(0.1f);

		// 選択肢オプション
		region = new TextureRegion(texture, 0, 176, 256, 35);
		modeOption = new Sprite(region);
		modeOption.setPosition(spritePositionX, -16);
		modeOption.setScale(0.1f);

		// 選択肢終了
		region = new TextureRegion(texture, 0, 218, 256, 35);
		modeExit = new Sprite(region);
		modeExit.setPosition(spritePositionX, -20);
		modeExit.setScale(0.1f);

		// 初期化
		scrollFlag = false;
		selectMenu = false;
	}

	// 更新
	public void update(float delta) {

		// 選択肢をクリックしたら画像移動
		moveSprite();

		// エンターキーでコンティニュー　仮挿入中
		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
				scrollFlag = true;		// スプライトを動かすフラグオン
				//scrollNinja.setScreen(new SettingsScreen(scrollNinja));
		}

		// クリックされたらゲームステート移行
		if(Gdx.input.isTouched()) {

			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			// コンティニュー
			if(x > 530 && x < 770 && y > 105 && y < 140) {
				//stateC = true;
				scrollFlag = true;
			}

			// ニューゲーム
			if(x > 530 && x < 770 && y > 140 && y < 175) {

			}

			// ロードゲーム
			if(x > 530 && x < 770 && y > 175 && y < 210) {

			}

			// ネットワーク
			if(x > 530 && x < 770 && y > 210 && y < 245) {

			}

			// オプション
			if(x > 530 && x < 770 && y > 245 && y < 280) {

			}

			// 終了
			if( x > 530 && x < 770 && y > 315 && y < 350 ) {

				int message =
						JOptionPane.showConfirmDialog(null, "終了しますか？", "Exit", JOptionPane.YES_NO_OPTION);

				if(message == JOptionPane.OK_OPTION) {
					//scrollNinja.dispose();

					System.exit(0);
				}
			}
		}

		// ゲームメイン移行
		if(selectMenu)
			scrollNinja.setScreen(new GameMain(scrollNinja));

	}
	//---------------------------------------------------
	// 画像移動
	// 画面端にいったらステート移行
	//---------------------------------------------------
	public void moveSprite() {

		if(scrollFlag) {
			// 加速して画面外
			spritePositionX += MOVE_SPEED;

			// スプライト移動
			modeContinue.setPosition(spritePositionX, 0);
			modeNewGame.setPosition(spritePositionX, -4);
			modeLoadGame.setPosition(spritePositionX, -8);
			modeNetwork.setPosition(spritePositionX, -12);
			modeOption.setPosition(spritePositionX, -16);
			modeExit.setPosition(spritePositionX, -20);
		}
		if(spritePositionX >= FADE_MENU) {
			scrollFlag = false;
			selectMenu = true;
		}
	}

	// 描画関係
	public void draw(float delta) {
		// クリア
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// スプライト描画
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Background.GetSprite(0).draw(batch);
		Background.GetSprite(1).draw(batch);

		// メニュー選択肢描画
		modeContinue.draw(batch);
		modeNewGame.draw(batch);
		modeLoadGame.draw(batch);
		modeNetwork.draw(batch);
		modeOption.draw(batch);
		modeExit.draw(batch);

		batch.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}
}