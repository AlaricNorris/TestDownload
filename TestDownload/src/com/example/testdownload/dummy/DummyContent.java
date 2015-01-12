package com.example.testdownload.dummy ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<DummyItem> ITEMS = new ArrayList<DummyItem>() ;

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String , DummyItem> ITEM_MAP = new HashMap<String , DummyItem>() ;
	static {
		// Add 3 sample items.
		addItem(new DummyItem("1" , "Meilishuo_3.6.1_10006" ,
				"http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk")) ;
		addItem(new DummyItem("2" , "shiweiying" , "http://58.218.196.207:8002/client/shiweiying.apk")) ;
		addItem(new DummyItem("3" , "APK 3" , "APK 1")) ;
	}

	private static void addItem(DummyItem item) {
		ITEMS.add(item) ;
		ITEM_MAP.put(item.id , item) ;
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {

		public String id ;

		public String content ;

		public String url ;

		/**
		 * 	Creates a new instance of DummyItem.
		 * 	@param id
		 * 	@param content
		 * 	@param url
		 */
		public DummyItem(String id , String content , String url) {
			super() ;
			this.id = id ;
			this.content = content ;
			this.url = url ;
		}

		/**
		 * 	(non-Javadoc)
		 * 	@see java.lang.Object#toString()
		 */
		@ Override
		public String toString() {
			return content ;
		}
	}
}
