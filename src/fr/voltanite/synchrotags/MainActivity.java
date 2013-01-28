package fr.voltanite.synchrotags;

import java.io.IOException;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "null";
	  private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.qr_code_scanner).setOnClickListener(scanAnything);
        findViewById(R.id.show_base).setOnClickListener(showbdd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private final Button.OnClickListener showbdd = new Button.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(getBaseContext(), ScrollableGeneric.class);
          startActivity(intent);
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
       
       private final Button.OnClickListener scanAnything = new Button.OnClickListener() {
           public void onClick(View v) {
             //IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
             //integrator.initiateScan();
        	   Intent addqrcode = new Intent(getBaseContext(), AddQRcode.class);
   	    	String message = "test qrcode";//scanResult.getContents();
   	    	addqrcode.putExtra(EXTRA_MESSAGE, message);
   	    	JSONObject jtest = new JSONObject();
   	    	try {
				jtest.put("batman", "truc");
//				String url = "http://192.168.5.71:9000/iut-manager-web/bidule";
				String url = "http://217.70.191.21/testandroidjson.php";
				HttpResponse re = HTTPPoster.doPost(url, jtest);
				String temp = EntityUtils.toString(re.getEntity());
				if (temp.compareTo("SUCCESS")==0)
				{
				    Toast.makeText(getBaseContext(), "Sending complete!", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), temp, Toast.LENGTH_LONG).show();
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
   	    	startActivity(addqrcode);
           }      
         };   
         

    
}
