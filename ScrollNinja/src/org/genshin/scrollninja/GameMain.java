package org.genshin.scrollninja;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameMain implements Screen{
	private Game					ScrollNinjya;
	private OrthographicCamera		camera;				// カメラ
	private SpriteBatch				spriteBatch;		// スプライトバッチ
	private World					world;				// ワールドマトリクス
	private Box2DDebugRenderer		renderer;			//
	private StageObjectManager		SOM;				// ステージオブジェクトマネージャ
	private Background				BG;					// 背景
	private Player					PLAYER;				// プレイヤー
	
	// コンストラクタ
	public GameMain() {
		SOM					= StageObjectManager.GetInstace();
		BG					= Background.GetInstace();
		PLAYER				= Player.GetInstace();
		camera				= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch 		= new SpriteBatch();
		world				= new World(new Vector2(0, -100.0f), true);
		renderer			= new Box2DDebugRenderer();
		
		CreateWorld();
		CreateCharacter();
		CreateStageObject();
	}
	
	//************************************************************
	// Update
	// 更新処理
	//************************************************************
	public void Update() {
		PLAYER.Update(world);
	}
	
	//************************************************************
	// Draw
	// 描画処理
	//************************************************************
	public void Draw() {
		PLAYER.Draw(spriteBatch);
	}
	
	//************************************************************
	// CreateWorld
	// フィールドの作成
	//************************************************************
	private void CreateWorld() {
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/test.json"));
		
		// ボディタイプ設定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体
		bd.position.set(-(Gdx.graphics.getWidth() / 2), -1024);
		
		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;		// 密度
		fd.friction		= 100.0f;	// 摩擦
		fd.restitution	= 0;		// 反発係数
		
		// ボディ作成
		BG.SetBody(world.createBody(bd));
		loader.attachFixture( BG.GetBody(), "bgTest", fd, 2048);
	}
	
	//************************************************************
	// CreateCharacter
	// プレイヤーの作成
	//************************************************************
	private void CreateCharacter() {
		BodyDef def	= new BodyDef();
		def.type	= BodyType.DynamicBody;		// 動く物体
		Player.GetInstace().SetBody(world.createBody(def));
		
		// 当たり判定の作成
		PolygonShape poly		= new PolygonShape();
		poly.setAsBox(16, 24);
		
		// ボディ設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 50;
		fd.friction		= 100.0f;
		fd.restitution	= 0;
		fd.shape		= poly;
		
		// 
		PLAYER.GetBody().createFixture(fd);
		PLAYER.SetFixture(PLAYER.GetBody().createFixture(poly, 0));
		poly.dispose();	
		PLAYER.GetBody().setBullet(true);			// すり抜け防止
		PLAYER.GetBody().setTransform(0, 300, 0);	// 初期位置
	}
	
	//************************************************************
	// CreateStageObject
	// ステージオブジェクトの作成
	//************************************************************
	private void CreateStageObject() {
		// 当たり判定読み込み
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/stageObject.json"));

		// Bodyのタイプを設定 Staticは動かない物体
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0, 0);

		// Bodyの設定を設定
		FixtureDef fd	= new FixtureDef();
		fd.density		= 1000;				// 密度
		fd.friction		= 100f;				// 摩擦
		fd.restitution	= 0;				// 反発係数

		// ステージオブジェクトの作成
		SOM.CreateStageObject("岩");
		SOM.GetStageObject("岩").SetBody(world.createBody(bd));

		// 各種設定を適用。引数は　Body、JSON中身のどのデータを使うか、FixtureDef、サイズ
		loader.attachFixture(SOM.GetStageObject("岩").GetBody(), "gravestone", fd, 256);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void render(float delta) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void show() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void hide() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void pause() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void resume() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
