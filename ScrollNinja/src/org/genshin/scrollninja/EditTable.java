package org.genshin.scrollninja;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//========================================
// フレーム用のクラス
//========================================
public class EditTable implements ActionListener {
	
	private final int			WIDTH	= 300;
	private final int			HEIGHT	= 720;
	
	private EditCanvas editCanvas = new EditCanvas(); 
	private Frame  frm = new Frame();
	private boolean frontFlag;
	private int count = 0;
	private int positionX;
	private int positionY;
	
	public void Init() {
        
	       /* フレームを作成します。*/
        frm.setSize(new Dimension(WIDTH,HEIGHT));
        
        /* インセット値を確定させる */
        frm.addNotify();
        Insets a = frm.getInsets();
        int t = a.top;
        int b = a.bottom;
        int r = a.right;
        int l = a.left;
        
        /* 前面へ */
        frm.toFront();
        
        /* タイトルの設定 */
        frm.setTitle("ツールボックス");
        
        /* 表示位置 */
        frm.setBounds((int)ScrollNinja.window.x - WIDTH,0/*(int)ScrollNinja.window.y - a.top*/, WIDTH, HEIGHT);

        /* フレームに登録します。*/
        frm.add(editCanvas);

        /* フレームを表示させます。*/
        frm.setVisible(true);
	}
	
	/**
	 * コンストラクタ
	 */
	public EditTable(){
		frontFlag = true;
		positionX = 0;
		positionY = 0;
	}
	
	/**
	 * 更新
	 */
	public void Update() {
		SetWindow();
		editCanvas.update();
		
		if( frontFlag ) {
			frm.setAlwaysOnTop(true);		// 常に前面だけど非アクティブ
		}
		else {
			frm.toBack();
			frm.setAlwaysOnTop(false);
		}
		
	}
	
	/**
	 * 描画
	 */
	public void Draw() {
		
	}
	
	/**
	 * ウインドウの位置変更
	 */
	public void SetWindow() {
		if( Gdx.input.isKeyPressed(Keys.Q) ) {
			count ++;
		}
		else {
			count = 0;
		}
		
		if( frontFlag && count == 1 ) {
			frontFlag = false;
		}
		else if( !frontFlag && count == 1 ) {
			frontFlag = true;
		}
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
		private final static int MAX_IMAGE = 5;
		private MouseForCanvas mouse;
		private boolean justPressed;
		private Point point;
		private int character;
		
		/**
		 * コンストラクタ
		 */
		public EditCanvas(){
			
			mouse = new MouseForCanvas();
			
			// ここで画像読み込み
			StructImageManager.CreateStructImage("data/shuriken.png");
			StructImageManager.CreateStructImage("data/shuriken.png");
			StructImageManager.CreateStructImage("data/shuriken.png");
			StructImageManager.CreateStructImage("data/shuriken.png");
			StructImageManager.CreateStructImage("data/shuriken.png");
			
			point = new Point();
			justPressed = false;
			character = 0;
			
			this.addMouseListener( mouse );
			this.addMouseMotionListener( mouse );
		}
		
		/**
		 *  更新
		 */
		public void update() {
			if( mouse.GetClick() ) {
				point = mouse.GetPosition();
				int x = 0;
				int y = 0;

				for( int i = 7; i >= 0; i -- ) {
					if( point.y >= i * 100 ) {
						y = i + 1;
						i = 0;
					}
				}
				for( int j = 3; j >= 0; j -- ) {
					if( point.x >= j * 100 ) {
						x = j + 1;
						j = 0;
					}
				}
				character = x + ( (y * 3) - 3);
				StructObjectManager.CreateStructObject(character);
				mouse.ResetClick();
			}
		}
		
		/**
		 * @OVERRIDE
		 */
		public void update(Graphics g) {
		}
		
		/**
		 * @OVERRIDE
		 * 描画(自動呼び出し)
		 */
		public void paint(Graphics g) {
			int k = 0;

			for( int i = 0; i < 7; i ++ ) {
				for( int j = 0; j < 3; j ++, k ++ ) {
					if( k >= MAX_IMAGE ) { return; }		// 全部読み込んだら終了
					
					g.drawImage(StructImageManager.GetImage(k), j * 100, i * 100, 100, 100, this);
				}
			}
		}
	}
}