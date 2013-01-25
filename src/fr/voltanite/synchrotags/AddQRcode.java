package fr.voltanite.synchrotags;


import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddQRcode extends Activity {
	private String parent ="null";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qrcode);
        Intent intent = getIntent();
        parent = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        findViewById(R.id.recherche_parent).setOnClickListener(rechercheparent);
        findViewById(R.id.add_meta).setOnClickListener(meta);
        findViewById(R.id.validation).setOnClickListener(validation);
        
        EditText paramParent = null;
        paramParent = (EditText)findViewById(R.id.saisie_nom);
        if(parent != null)
        	paramParent.setHint(parent);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_qrcode, menu);
        return true;
    }
    
    public final OnClickListener rechercheparent = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), ScrollableGeneric.class);
			startActivity(intent);
		}
	};
	
	 public final OnClickListener meta = new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ScrollableGeneric.class);
				startActivity(intent);
			}
		};
		
		 public final OnClickListener validation = new OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), CaptureQrCode.class);
					startActivity(intent);
				}
			};
}
