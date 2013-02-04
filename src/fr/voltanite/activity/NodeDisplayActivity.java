package fr.voltanite.activity;

import java.util.ArrayList;
import java.util.Currency;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.activity.R;
import fr.voltanite.utils.Utils;

public class NodeDisplayActivity extends Activity {

	private static String path= "/";
	private String nom_parent;
	private static Noeud noeud;
	//private static Noeud current_node;
	String qrcode;
	
	
	public void setPath(String npath)
	{
		path = npath;
	}
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);    

		try 
		{
			setContentView(R.layout.activity_display_noeuds);

			View linearLayout = findViewById(R.id.database_nodes_layout);
			
			Intent intent = getIntent();
			path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			//Utils.popDebug(getBaseContext(), path);
			TextView t = (TextView)findViewById(R.id.path_pere); 
			t.setHint(path);

			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();

			int nbLignes = nbdd.getNbNoeuds();
			ArrayList<Button> buttons = new ArrayList<Button>();
			Noeud current_node;
			for (int i = 0; i < nbLignes; i ++)
			{
				current_node= nbdd.getNoeudById(i);
				final String myPath = current_node.getNom();
				buttons.add(new Button(this));
				Button btmp = buttons.get(buttons.size() - 1);
				btmp.setText(current_node.toString());
				btmp.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				btmp.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
						path = path + "/" + myPath;
						intent.removeExtra(MainActivity.EXTRA_MESSAGE);
						intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
						startActivity(intent);
					}
				});
			}
			nbdd.close();

			for (Button btn : buttons)
			{
				((ViewGroup) linearLayout).addView(btn);
			}

		}
		catch (Exception e)
		{
			Utils.popDebug(this, "Exception : " + e.getMessage());
		}

	}
	
	public void onBackPressed()
	{
		int lastSlash = path.lastIndexOf("/");
		if (path.length() != 0)
		{
			path = path.substring(0, lastSlash);
		}
		Intent intent = getIntent();
		intent.removeExtra(MainActivity.EXTRA_MESSAGE);
		intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
		super.onBackPressed();
	}

	//	
	//public final OnClickListener show_content = new OnClickListener() {
	//		
	//		public void onClick(View v) {
	//			Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
	//			path = path + nom_parent+"/";
	//			intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
	//			startActivity(intent);
	//		}
	//	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_display_message, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
