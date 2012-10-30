package org.genshin.scrollninja;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonForEdit extends JButton implements ActionListener {
	
	private int type = 0;

	public ButtonForEdit(int Type) {
		// TODO 自動生成されたコンストラクター・スタブ
//		setText(name);
		type = Type;
		addActionListener(this);
		
		ImageIcon icon = new ImageIcon("data/shuriken.png");
		this.setIcon(icon);
	}

	/**
	 * @OVERRIDE
	 * ボタンが押されたら呼ばれる
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}
