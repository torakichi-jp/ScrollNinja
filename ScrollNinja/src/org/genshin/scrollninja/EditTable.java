package org.genshin.scrollninja;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//========================================
// フレーム用のクラス
//========================================
public class EditTable implements ActionListener {
	
	private final int			WIDTH	= 300;
	private final int			HEIGHT	= 700;
	
	private Frame  frm = new Frame();
	private Button btn;
	
	public void test() {
	       /* フレームを作成します。*/
        frm.setSize(new Dimension(300,700));
        
        /* 前面へ */
        frm.toFront();
        
        /* タイトルの設定 */
        frm.setTitle("ツールボックス");
        
        /* 表示位置 */
        frm.setBounds((int)ScrollNinja.window.x, WIDTH - (int)ScrollNinja.window.y, WIDTH, HEIGHT);

        /* フレームに登録します。*/
        frm.add(new EditCanvas());

        /* フレームを表示させます。*/
        frm.setVisible(true);
	}
	
	/**
	 * コンストラクタ
	 */
	public EditTable(){
		
	}
	
	/**
	 * 更新
	 */
	public void Update() {
		frm.setAlwaysOnTop(true);		// 常に前面だけど非アクティブ
	}
	
	/**
	 * 描画
	 */
	public void Draw() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	//========================================
	// 描画用クラス
	//========================================
	public class EditCanvas extends Canvas {
		
		private Color flameColor;			// フレーム色
		private Color backColor;			// 背景色
		private Image image[] = new Image[10];
		
		/**
		 * コンストラクタ
		 */
		public EditCanvas(){
			image[0] = FileOperation.LoadImage("data/player.png");
		}
		
		/**
		 * @OVERRIDE
		 * 描画(自動呼び出し)
		 */
		public void paint(Graphics g) {
			g.drawImage(image[0], 0, 0, 100, 100, this);
		}
	}
	
	//========================================
	// 画像クラス
	//========================================
	public class StructImage {
		
		private Image image;
		private Vector2 position;
		
		StructImage( Image i, int x, int y ) {
			position = new Vector2( x, y );
			image = i;
		}
	}
	
	//========================================
	// 画像管理クラス
	//========================================
	public class StructImageManager {
		
	}
}