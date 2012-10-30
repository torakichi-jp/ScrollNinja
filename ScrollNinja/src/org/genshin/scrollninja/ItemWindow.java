package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JScrollPane;

import org.genshin.scrollninja.EditTable.EditCanvas;

public class ItemWindow {
	
	private final int			WIDTH	= 360;
	private final int			HEIGHT	= 720;
	private Frame  frm = new Frame();
	private boolean frontFlag;
	private int count = 0;
	private JScrollPane scrollPane;						// スクロールバー
	
	/**
	 * コンストラクタ
	 */
	public ItemWindow(){}
	
	/**
	 * 初期化
	 */
	public void Init(){
		 /* フレームを作成します。*/
	     frm.setSize(new Dimension(WIDTH,HEIGHT));
	     
	     /* 前面へ */
	     frm.toFront();
	     frm.setTitle("アイテムボックス");
	     
//	     scrollPane = new JScrollPane(editCanvas);
	     frm.add(scrollPane);
	
	     /* フレームを表示させます。*/
	     frm.setVisible(true);
	}

}
