package fr.voltanite.synchrotags;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.scan_anything).setOnClickListener(scanAnything);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private final Button.OnClickListener scanAnything = new Button.OnClickListener() {
        public void onClick(View v) {
          IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
          integrator.initiateScan();
        }
      };
}
