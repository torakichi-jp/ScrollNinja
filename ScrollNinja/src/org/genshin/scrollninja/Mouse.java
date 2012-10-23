package org.genshin.scrollninja;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Mouse implements MouseListener, MouseMotionListener {
	
	private static boolean rightClick;
	private static boolean leftClick;
	private static Vector2 position = new Vector2(0.0f, 0.0f);
	
	/**
	 * コンストラクタ
	 */
	public Mouse(){
		rightClick = false;
		leftClick = false;
	}
	
	/**
	 * 座標参照
	 * @return
	 */
	public static Vector2 GetPosition() { return position; }
	
	/**
	 * 右クリック判定
	 * @return
	 */
	public static boolean RightClick() { return rightClick; }
	
	/**
	 * 左クリック判定
	 * @return
	 */
	public static boolean LeftClick() { return leftClick; }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(arg0.getPoint());
	}

}
