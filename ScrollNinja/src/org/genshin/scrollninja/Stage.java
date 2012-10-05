

package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Stage {
	
	private String name;
	private Vector2 size;
	private ArrayList<Background>backgroundLayers;
	private Sprite sprite;
	private ArrayList<Item> popItems;
	private ArrayList<Enemy> popEnemys;
	private ArrayList<ObJectBase> object;
	private Vector2 cameraPos = new Vector2();
	private OrthographicCamera camera;
	
	private Player asd = Player.GetInstace();
	
	private GameScreen zz;
	
	
	

	
	
	
	// コンストラクタ
	public Stage(String Name){
		name = new String(Name);						// オブジェクト化と同時にステージ番号の代入
	}
	
	// 参照
	public Stage GetStage() {
		return this;
	}
	
	// 敵ポップ
	public void popEnemy() {
		
	}
	
	// アイテムポップ
	public void popItem() {
		
	}
	
	// 背景移動
	public void moveBackground() {
		/*
		 * camera move
		 * */
		
		
		//cameraPos = asd.GetPosition();

		cameraPos = zz.GetChrPos();
	
		
		camera.position.set(cameraPos.x, cameraPos.y, 0);
		camera.update();
		

		// 遠景をカメラの位置に合わせて移動
		ScrollNinja.bgSpr.setPosition
			(cameraPos.x - 400 + (cameraPos.x * -0.05f), -512 + (cameraPos.y * -0.15f));
	}
	
	public Player spawnPlayer(Player player) {
		return player;
	}
	
	public int timeCount(int nowTime) {
		// 制限時間 or 経過時間加算
		nowTime += 1;
		
		return nowTime;
	}
	
}
