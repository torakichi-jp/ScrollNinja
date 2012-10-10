package org.genshin.scrollninja;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class MainMenu implements Screen , MouseListener{
	private Game scrollNinja;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture texture;		// テクスチャー
	private Sprite cursor;		// カーソル

	private Sprite mode_Continue;		// コンティニュー
	private Sprite mode_NewGame;		// ニューゲーム
	private Sprite mode_LoadGame;		// ロードゲーム
	private Sprite mode_Network;		// ネットワーク
	private Sprite mode_Option;			// オプション
	private Sprite mode_Exit;			// エグジット

	private int SpritePosX;		// 画像座標
	private int SpriteSpd;		// スクロール時の移動速度
	private boolean SprFlg;		// スクロールフラグ
	private boolean stateC;		// ステートチェンジフラグ
	
	private final static int FADE_MENU = 500;		// 画像移動の終わり座標
	private float rotation;

	// カーソルの位置
	private int position;

	// コンストラクタ
	public MainMenu(Game game) {
		this.scrollNinja = game;

		// カメラ作成
		camera =
			new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// スプライトバッチ作成
		batch = new SpriteBatch();

		// カーソルの初期位置
		// 0:GameRun 1:Settings
		position = 0;

		// テクスチャ読み込み
		try {
			texture = new Texture(Gdx.files.internal("data/menu_test.png"));
		} catch (NullPointerException e) {
			System.out.println("ファイルがありません");
		} catch (GdxRuntimeException e) {
			System.out.println("テクスチャサイズは２の乗数にしてください");
		}
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		SpritePosX = 0;
		// カーソルセット
		TextureRegion region = new TextureRegion(texture, 0, 200, 50, 50);
		cursor = new Sprite(region);
		cursor.setOrigin(cursor.getWidth() / 2, cursor.getHeight() / 2);
		cursor.setPosition(-200, 0);

		// 選択肢コンテニュー
		region = new TextureRegion(texture, 0, 0, 256, 35);
		mode_Continue = new Sprite(region);
		mode_Continue.setPosition(SpritePosX, 0);

		// 選択肢ニューゲーム
		region = new TextureRegion(texture, 0, 40, 256, 35);
		mode_NewGame = new Sprite(region);
		mode_NewGame.setPosition(SpritePosX, -40);
		
		// 選択肢ロードゲーム
		region = new TextureRegion(texture, 0, 85, 256, 35);
		mode_LoadGame = new Sprite(region);
		mode_LoadGame.setPosition(SpritePosX, -80);
		
		// 選択肢ネットワー
		region = new TextureRegion(texture, 0, 128, 256, 35);
		mode_Network = new Sprite(region);
		mode_Network.setPosition(SpritePosX, -120);
		
		// 選択肢オプション
		region = new TextureRegion(texture, 0, 176, 256, 35);
		mode_Option = new Sprite(region);
		mode_Option.setPosition(SpritePosX, -160);
		
		// 選択肢終了
		region = new TextureRegion(texture, 0, 218, 256, 35);
		mode_Exit = new Sprite(region);
		mode_Exit.setPosition(SpritePosX, -200);
		
		// おまけの回転
		rotation = 0;
		
		// 初期化
		SprFlg = false;
		stateC = false;
		SpriteSpd = 0;
		
	}

	// 更新
	public void update(float delta) {
		
		// (仮)現在未使用
		// キー入力
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (position == 1) {
				cursor.setPosition(-200, 0);
				position = 0;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			if (position == 0) {
				cursor.setPosition(-200, -125);
				position = 1;
			}
		}
		// 画像移動
		moveSprite();
		
		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			if (position == 0) {
				SprFlg = true;		// スプライトを動かすフラグオン
				return;
			}
			if (position == 1) {
				scrollNinja.setScreen(new SettingsScreen(scrollNinja));
				return;
			}
		}
		
		// クリックされたらゲームステート移行
		if(Gdx.input.isTouched()) {
			
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			
			// コンティニュー
			if(x > 435 && x < 600 && y > 265 && y < 300) {
				//stateC = true;
				SprFlg = true;
			}
			
			// ニューゲーム
			if(x > 435 && x < 625 && y > 310 && y < 340) {
				
			}
			
			// ロードゲーム
			if(x > 435 && x < 635 && y > 350 && y < 380) {
				
			}			
			
			// ネットワーク
			if(x > 435 && x < 590 && y > 390 && y < 420) {
				
			}
			
			// オプション
			if(x > 435 && x < 555 && y > 427 && y < 460) {
				
			}
			
			// 終了
			if( x > 435 && x < 505 && y > 470 && y < 500 ) {
				
				int message = JOptionPane.showConfirmDialog(null, "終了しますか？", "Exit", JOptionPane.YES_NO_OPTION);
				
				if(message == JOptionPane.OK_OPTION) {
					//scrollNinja.dispose();
					
					System.exit(0);	
				}
			}
			
			//System.out.println(y);
			
		}
		
		// ゲームメイン移行
		if(stateC)
			scrollNinja.setScreen(new GameMain(scrollNinja));
		
	}
	//---------------------------------------------------
	// 画像移動
	// 画面端にいったらステート移行
	//---------------------------------------------------
	public void moveSprite() {
		
		if(SprFlg) {
			// 加速して画面外
			SpritePosX += SpriteSpd;
			SpriteSpd += 1;
			
			// スプライト移動
			mode_Continue.setPosition((int)SpritePosX, 0);
			mode_NewGame.setPosition((int)SpritePosX, -40);
			mode_LoadGame.setPosition((int)SpritePosX, -80);
			mode_Network.setPosition((int)SpritePosX, -120);
			mode_Option.setPosition((int)SpritePosX, -160);
			mode_Exit.setPosition((int)SpritePosX, -200);
		}
		if(SpritePosX >= FADE_MENU) {
			SprFlg = false;
			stateC = true;
		}
	}

	// 描画関係
	public void draw(float delta) {
		// クリア
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// スプライト描画
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		ScrollNinja.bgSpr.draw(batch);
//		ScrollNinja.stageSpr.draw(batch);
		Background.GetSprite()[0].draw(batch);
		Background.GetSprite()[2].draw(batch);
		cursor.draw(batch);
		
		// メニュー選択肢描画
		mode_Continue.draw(batch);
		mode_NewGame.draw(batch);
		mode_LoadGame.draw(batch);
		mode_Network.draw(batch);
		mode_Option.draw(batch);
		mode_Exit.draw(batch);
		
		batch.end();

		// おまけの回転
		rotation += 1;
		cursor.setRotation(rotation);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}