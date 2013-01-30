package fr.voltanite.activity;

import java.util.ArrayList;

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

	private String path= "";
	private String nom_parent;
	private Noeud current_node;
	String qrcode;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);    

		try 
		{
			setContentView(R.layout.activity_display_noeuds);

			View linearLayout = findViewById(R.id.database_nodes_layout);

			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();
			
			int nbLignes = nbdd.getNbNoeuds();
			ArrayList<Button> buttons = new ArrayList<Button>();

			for (int i = 0; i < nbLignes; i ++)
			{
				current_node= nbdd.getNoeudById(i);
				buttons.add(new Button(this));
				Button btmp = buttons.get(buttons.size() - 1);
				btmp.setText("Noeud : " + current_node.getId() +" " + current_node.getNom() + "\n " 
						+ "Id père : " + current_node.getPere());
				btmp.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
				btmp.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
						path = path + current_node.getNom()+"/";
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
			

			Intent intent = getIntent();
			path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			Utils.popDebug(getBaseContext(), path);
			TextView t=new TextView(this); 
			t=(TextView)findViewById(R.id.path_pere); 
			t.setHint(path);
			


		}
		catch (Exception e)
		{
			Utils.popDebug(this, "Exception : " + e.getMessage());
		}

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
