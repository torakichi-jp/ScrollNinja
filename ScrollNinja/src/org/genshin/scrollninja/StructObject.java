package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StructObject {
	
	// プレイヤー
	private final int			PLAYER			= 0;
	
	// アイテム
	private final int			ONIGIRI_ITEM	= 10;
	private final int			OKANE_ITEM		= 11;
	
	// 敵
	private final int			NORMAL_ENEMY	= 100;
	private final int			RARE_ENEMY		= 101;
	private final int			AUTO_ENEMY		= 102;
	
	// ステージオブジェクト
	private final int			ROCK_OBJECT		= 1000;
	private final int			ROCK2_OBJECT	= 1001;

	/**
	 * テキストファイルに吐き出す情報
	 */
	public int		type;			/* オブジェクトの種類
	 								 * ０				プレイヤー
	 								 * １０〜９９			アイテム
	 								 * １００〜９９９		敵
	 								 * １０００〜			ステージオブジェクト
	 								 */
	public float	positionX;		// 座標Ｘ
	public float	positionY;		// 座標Ｙ
	public int		priority;		// 優先度（数値が低い方が手前）
	
	/**
	 * エディタで使うもの
	 */
	public boolean	hold;
	public ArrayList<Sprite> sprite;
	
	/**
	 * コンストラクタ
	 */
	public StructObject(int Type, float x, float y, int p) {
		sprite = new ArrayList<Sprite>();
		type = Type;
		positionX = x;
		positionY = y;
		priority = p;
		hold = false;
		
		// スプライト読み込み
		Create();
	}
	
	/**
	 * 更新
	 */
	public void Update() {
		
		// 左クリックでオブジェクトを掴む
		if( Mouse.LeftClick() ) {
			if( positionX - 1.6 < (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) && positionX + 1.6 > (Mouse.GetPosition().x * 0.1 - 64.0 ) + (GameMain.camera.position.x) &&
				positionY - 2.4 < (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 ) && positionY + 2.4 > (GameMain.camera.position.y) - (Mouse.GetPosition().y * 0.1 - 36.0 )) {
				hold = true;
			}
			else {
				hold = false;
			}
		}
		
		// とりあえず右クリックで解放
		if( Mouse.RightClick() ) {
			hold = false;
		}
	}
	
	/**
	 * 描画
	 */
	public void Draw() {
		for( int i = 0; i < sprite.size(); i ++ ) {
			sprite.get(i).setPosition(positionX, positionY);
			sprite.get(i).draw(GameMain.spriteBatch);
		}
	}
	
	/**
	 * 生成
	 */
	public void Create() {
		switch(type) {
		case NORMAL_ENEMY:
			CreateEnemy();
		}
	}
	
	public void CreateEnemy() {
		Texture texture = new Texture(Gdx.files.internal("data/enemy.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);

		// スプライトに反映
		sprite.add(new Sprite(region));
		sprite.get(0).setOrigin(sprite.get(0).getWidth() * 0.5f, sprite.get(0).getHeight() * 0.5f);
		sprite.get(0).setScale(ScrollNinja.scale);
	}
}
