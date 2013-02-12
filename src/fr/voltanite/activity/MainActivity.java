package fr.voltanite.activity;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import fr.voltanite.noeud.BaseSQLite;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;



public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "qrcode_stub";
	public final static String EXTRA_MESSAGE_ID = "";
	public static int id_racine = 0;
	public static String LOGINUSR;
	public static String LOGINPWD;
	
	private static final String TAG = MainActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.qr_code_scanner).setOnTouchListener((OnTouchListener) scanAnything);
		findViewById(R.id.show_base).setOnTouchListener((OnTouchListener)showbdd);
		findViewById(R.id.continuous_scan).setOnTouchListener((OnTouchListener)continuousQrcode);
		findViewById(R.id.testPHP).setOnTouchListener((OnTouchListener)testPHP);
		findViewById(R.id.testLive).setOnTouchListener((OnTouchListener)testLive);
		findViewById(R.id.testJEE).setOnTouchListener((OnTouchListener)testJEE);
		findViewById(R.id.testPHP2).setOnTouchListener((OnTouchListener)Login);
		Utils.popDebug(getBaseContext(), LOGINUSR);
		Utils.popDebug(getBaseContext(), LOGINPWD);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onBackPressed()
	{
		finish();
	}
	
	public void onCodeSearch(View v)
	{
		System.out.println("OnCodeSearch pressed");
		Intent intent = new Intent(this, NodeSearchByCode.class);
		intent.putExtra(EXTRA_MESSAGE, "/Racine");
		intent.putExtra(EXTRA_MESSAGE_ID, String.valueOf(id_racine));
		intent.putExtra("qrCode", "test qrcode");
		startActivity(intent);
	}

	private final TextView.OnTouchListener showbdd = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
			intent.putExtra(EXTRA_MESSAGE, "/Racine");
			intent.putExtra(EXTRA_MESSAGE_ID, String.valueOf(id_racine));
			startActivity(intent);
			return false;
		}

	};

	private final TextView.OnTouchListener testPHP = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			jsonTest();		
			return false;
		}

	};

	private final TextView.OnTouchListener testJEE = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Utils.popDebug(getBaseContext(), "Not yet.");	
			return false;
		}

	};

	private final TextView.OnTouchListener Login = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			//if(LOGINUSR == null && LOGINPWD == null){
			Intent intent = new Intent(getBaseContext(), LoginActivity.class);
			startActivity(intent);
			//else Utils.popDebug(getBaseContext(), "Syncronizationeuh");
			return false;
		}

	};

	private final TextView.OnTouchListener testLive = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			//IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
			//integrator.initiateScan();
			
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			startActivity(intent);
			return false;
		}

	};

	private final TextView.OnTouchListener continuousQrcode = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Intent intent = new Intent(getBaseContext(), ContinuousQRCodeFatherScan.class);
			intent.putExtra("FatherCode", "Father QrCode stub");
			startActivity(intent);
			return false;
		}

	};


	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			Intent addqrcode = new Intent(this, AddQRcode.class);
			String message = scanResult.getContents();
			addqrcode.putExtra(EXTRA_MESSAGE, message);
			startActivity(addqrcode);
		}
		// else continue with any other code you need in the method
	}

	private final TextView.OnTouchListener scanAnything = new TextView.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			Intent addqrcode = new Intent(getBaseContext(), AddQRcode.class);
			String message = "test qrcode";//scanResult.getContents();
			addqrcode.putExtra(EXTRA_MESSAGE, message);
			startActivity(addqrcode);  
			return false;
		}

	};


	private void jsonTest (){
		JSONObject jtest = new JSONObject();
		//getDatabasePath(Synchrotags);
		try {
			File file = new File(Environment.getDataDirectory(), "Synchrotags");
			
			jtest.put(LOGINUSR, LOGINPWD);
			//jtest.put("current_node", NoeudsBDD.)
			//		String url = "http://192.168.5.70:9000/iut-manager-web/bidule";
					String url = "http://h.n0m.fr:9000/alex/";
			//String url = "http://info-morgane.iut.u-bordeaux1.fr/perso/2012-2013/jmanenti/truc.php";
			HttpResponse re = HTTPPoster.doPost(url, jtest);
			String temp = EntityUtils.toString(re.getEntity());
			if (temp.compareTo("SUCCESS")==0)
			{
				Toast.makeText(getBaseContext(), "Sending complete!", 6000).show();
			}
			else
			{
				Toast.makeText(getBaseContext(), temp, 6000).show();
				Toast.makeText(getBaseContext(), jtest.toString(), 6000).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
