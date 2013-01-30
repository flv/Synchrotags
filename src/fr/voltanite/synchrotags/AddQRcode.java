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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qrcode);
        Intent intent = getIntent();
        String qrcode = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        findViewById(R.id.node_creation_parent_search).setOnClickListener(rechercheparent);
        findViewById(R.id.node_creation_metadata_add).setOnClickListener(meta);
        findViewById(R.id.node_creation_validate).setOnClickListener(validation);
        
        EditText paramParent = null;
        paramParent = (EditText)findViewById(R.id.node_creation_name_input);
        if(qrcode != null)
        	paramParent.setHint(qrcode);
        
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
					Intent intent = new Intent(getBaseContext(), MainActivity.class);
					startActivity(intent);
				}
			};
}
