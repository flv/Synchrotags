package fr.voltanite.synchrotags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
             IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
             integrator.initiateScan();
           }
         };
  
    
}
