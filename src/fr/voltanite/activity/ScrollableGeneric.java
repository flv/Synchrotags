package fr.voltanite.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import fr.voltanite.synchrotags.R;

public class ScrollableGeneric extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_generic);
        findViewById(R.id.retour_accueil).setOnClickListener(retouraccueil);
        findViewById(R.id.list).setOnTouchListener(testlist);
    }
	
	private final Button.OnClickListener retouraccueil = new Button.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(getBaseContext(), MainActivity.class);
          startActivity(intent);
        }
      };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scrollable_generic, menu);
        return true;
    }
    
    public final OnTouchListener testlist = new OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent event) {
	    	openOptionsMenu();
			return false; 
	    }
	  };
    
}
