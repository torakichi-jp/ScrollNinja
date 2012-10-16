package org.genshin.scrollninja;

public interface CollisionInterface {
	
	
	public void HitTest(Player obj);
	public void HitTest(Enemy obj);
	public void HitTest(Effect obj);
	public void HitTest(Item obj);
	public void HitTest(StageObject obj);

}
