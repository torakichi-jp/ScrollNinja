package org.genshin.scrollninja;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	private ReadFile(){}
	
	public static void read(String filePath) {
        try{
        	// ファイル読み込み
            FileReader f = new FileReader(filePath);
            BufferedReader b = new BufferedReader(f);
            String s;
            while((s = b.readLine())!=null){
            	
            	// 読み込んだ奴数字に置き換えればおｋ
                System.out.println(s);
            }
            b.close();
        }catch(Exception e){
            System.out.println("ファイル読み込み失敗");
        }
  
	}
}
