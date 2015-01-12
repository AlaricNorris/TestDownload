package com.example.testdownload ;

import java.io.UnsupportedEncodingException ;
import java.net.URLEncoder ;
import android.app.DownloadManager ;
import android.app.DownloadManager.Request ;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.content.IntentFilter ;
import android.content.SharedPreferences ;
import android.database.Cursor ;
import android.net.Uri ;
import android.os.Bundle ;
import android.preference.PreferenceManager ;
import android.support.v4.app.Fragment ;
import android.util.Log ;
import android.view.LayoutInflater ;
import android.view.View ;
import android.view.ViewGroup ;
import android.webkit.MimeTypeMap ;
import android.widget.TextView ;
import com.example.testdownload.dummy.DummyContent ;

/**
 * A fragment representing a single APK detail screen.
 * This fragment is either contained in a {@link APKListActivity}
 * in two-pane mode (on tablets) or a {@link APKDetailActivity}
 * on handsets.
 */
public class APKDetailFragment extends Fragment {

	private String URL ;

	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id" ;

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem ;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public APKDetailFragment() {
	}

	@ Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;
		if(getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)) ;
		}
		downloadManager = (DownloadManager) getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE) ;
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
	}

	@ Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container ,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_apk_detail , container , false) ;
		// Show the dummy content as text in a TextView.
		if(mItem != null) {
			((TextView) rootView.findViewById(R.id.apk_detail)).setText(mItem.content) ;
			URL = mItem.url ;
		}
		return rootView ;
	}

	/**
	 * 	(non-Javadoc)
	 * 	@see android.support.v4.app.Fragment#onResume()
	 */
	@ Override
	public void onResume() {
		super.onResume() ;
//		if( ! prefs.contains(DL_ID)) {
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL)) ;
			request.setDestinationInExternalPublicDir("TIZA" , mItem.content + ".apk") ;
			request.setTitle(mItem.content) ;
			request.setDescription(mItem.content) ;
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) ;
			// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
			// request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
			// request.setMimeType("application/com.trinea.download.file");
			request.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(
					MimeTypeMap.getFileExtensionFromUrl(URL))) ;
			long downloadId = downloadManager.enqueue(request) ;
			//保存id 
			prefs.edit().putLong(DL_ID , downloadId).commit() ;
//		}
//		else {
//			//下载已经开始，检查状态
//			queryDownloadStatus() ;
//		}
		getActivity().registerReceiver(receiver ,
				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) ;
	}

	@ Override
	public void onPause() {
		super.onPause() ;
		getActivity().unregisterReceiver(receiver) ;
	}

	private DownloadManager downloadManager ;

	private SharedPreferences prefs ;

	private static final String DL_ID = "downloadId" ;

//	@ Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume() ;
//		if( ! prefs.contains(DL_ID)) {
//			String url = "http://10.0.2.2/android/film/G3.mp4" ;
//			//开始下载 
//			Uri resource = Uri.parse(encodeGB(url)) ;
//			DownloadManager.Request request = new DownloadManager.Request(resource) ;
//			request.setAllowedOverRoaming(false) ;
//			//设置文件类型
//			MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton() ;
//			String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
//					.getFileExtensionFromUrl(url)) ;
//			request.setMimeType(mimeString) ;
//			//在通知栏中显示 
//			request.setShowRunningNotification(true) ;
//			request.setVisibleInDownloadsUi(true) ;
//			//sdcard的目录下的download文件夹
//			request.setDestinationInExternalPublicDir("/download/" , "G3.mp4") ;
//			request.setTitle("移动G3广告") ;
//			long id = downloadManager.enqueue(request) ;
//		}
//		else {
//			//下载已经开始，检查状态
//			queryDownloadStatus() ;
//		}
//		getActivity().registerReceiver(receiver ,
//				new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) ;
//	}
	/**
	 * 如果服务器不支持中文路径的情况下需要转换url的编码。
	 * @param string
	 * @return
	 */
	public String encodeGB(String string) {
		//转换中文编码
		String split[] = string.split("/") ;
		for(int i = 1 ; i < split.length ; i ++ ) {
			try {
				split[i] = URLEncoder.encode(split[i] , "GB2312") ;
			}
			catch(UnsupportedEncodingException e) {
				e.printStackTrace() ;
			}
			split[0] = split[0] + "/" + split[i] ;
		}
		split[0] = split[0].replaceAll("\\+" , "%20") ;//处理空格
		return split[0] ;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@ Override
		public void onReceive(Context context , Intent intent) {
			//这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
			Log.v("intent" , "" + intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID , 0)) ;
			queryDownloadStatus() ;
		}
	} ;

	private void queryDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query() ;
		query.setFilterById(prefs.getLong(DL_ID , 0)) ;
		Cursor c = downloadManager.query(query) ;
		if(c.moveToFirst()) {
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) ;
			switch(status) {
				case DownloadManager.STATUS_PAUSED :
					Log.v("down" , "STATUS_PAUSED") ;
				case DownloadManager.STATUS_PENDING :
					Log.v("down" , "STATUS_PENDING") ;
				case DownloadManager.STATUS_RUNNING :
					//正在下载，不做任何事情
					Log.v("down" , "STATUS_RUNNING") ;
					break ;
				case DownloadManager.STATUS_SUCCESSFUL :
					//完成
					Log.v("down" , "下载完成") ;
					break ;
				case DownloadManager.STATUS_FAILED :
					//清除已下载的内容，重新下载
					Log.v("down" , "STATUS_FAILED") ;
					downloadManager.remove(prefs.getLong(DL_ID , 0)) ;
					prefs.edit().clear().commit() ;
					break ;
			}
		}
	}
}
