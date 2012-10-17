package org.genshin.scrollninja;

import java.util.List;

import com.badlogic.gdx.physics.box2d.Contact;

public class CollisionDetector {
	
	private CollisionDetector() {}
	
	public static void HitTest(){
		
		List<Contact> contactList = GameMain.world.getContactList();

		for(int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);
			
			if( contact.isTouching() ) {
				ObJectBase a = (ObJectBase)contact.getFixtureA().getUserData();
				ObJectBase b = (ObJectBase)contact.getFixtureB().getUserData();
				a.collisionDispatch(b, contact);
				b.collisionDispatch(a, contact);
			}
		}
	}
}