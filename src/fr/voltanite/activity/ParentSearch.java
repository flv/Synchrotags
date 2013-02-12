package fr.voltanite.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class ParentSearch extends Activity {

	private static int  ParentSearchResult = 26;
	private static String path = "";
	private static int id_pere;
	String qrcode;
	private int id_selected;

	public void setPath(String npath)
	{
		path = npath;
	}


	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);    

		try 
		{
			setContentView(R.layout.activity_display_noeuds);
			View pageLayout = findViewById(R.id.database_nodes_layout);
			//RadioGroup parent_radio =(RadioGroup) findViewById(R.id.radio_parent);
			Intent intent = getIntent();
			path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			TextView t = (TextView)findViewById(R.id.path_pere); 
			t.setHint(path);
			id_pere=Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ID));	
			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();
			int nbLignes = nbdd.getNbNoeuds();
			ArrayList<TextView> txtvwNode = new ArrayList<TextView>();
			for (int i = 0; i < nbLignes; i ++)
			{
				final int sharedI = i;
				final Noeud current_node;
				try {

					current_node = nbdd.getNoeudById(i);
					if(current_node.getPere() == id_pere){
						final String myPath = current_node.getNom();
						final int parent_id = current_node.getId();
						txtvwNode.add(new TextView(this));
						TextView btmp = txtvwNode.get(txtvwNode.size() - 1);
						btmp.setText("Noeud : " + current_node.getId() +" " + current_node.getNom() + "\n " 
								+ "Id père : " + current_node.getPere());
						btmp.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						btmp.setClickable(true);
						RadioButton but = new RadioButton(this);

						but.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								id_selected = sharedI; 
								Utils.popDebug(getBaseContext(), String.valueOf(sharedI));
							}
						});
						//((ViewGroup) parent_radio).addView(but);
						// Affichage d'un noeud par maintien du click

						btmp.setOnLongClickListener(new OnLongClickListener() {

							public boolean onLongClick(View v) {							
								System.out.println("entrée dans le onLongClick");
								Intent intent = new Intent (getBaseContext(), AddQRcode.class);
								setResult(RESULT_OK, intent);
								intent.putExtra("FatherId", parent_id);
								System.out.println("Parent id émis par ParentSearch " + parent_id  );
								finish();
								return true;
							}
						});				


						// Navigation dans la bdd
						btmp.setOnClickListener(new OnClickListener() {

							public void onClick(View arg0) {
								System.out.println("Entrée dans le onClick");
								Intent intent = new Intent(getBaseContext(), ParentSearch.class);
								if(path == null)
								{
									path ="/"+myPath;
								}
								else{
									path = path + "/" + myPath;
								}
								id_pere = current_node.getId();
								intent.removeExtra(MainActivity.EXTRA_MESSAGE);
								intent.removeExtra(MainActivity.EXTRA_MESSAGE_ID);
								intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
								intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
								startActivityForResult(intent, ParentSearchResult);
							}
						});

					}

				}
				catch (NoMatchableNodeException e)
				{
					System.out.println("exception " + e.getMessage() + "    i = " + i );
				}
			}
			nbdd.close();

			for(int i=0; i<txtvwNode.size(); i++)
			{
				//parent_radio.addView(but);
				LinearLayout lineLayout1 = new LinearLayout(this);
				((ViewGroup) lineLayout1).addView(txtvwNode.get(i));
				((ViewGroup) pageLayout).addView(lineLayout1);
				//((ViewGroup) pageLayout).addView(parent_radio);
				lineLayout1.setOrientation(LinearLayout.HORIZONTAL); 
			}
			//RadioButton parent_check = (RadioButton) parent_radio.getCheckedRadioButtonId();		

		}


		catch (Exception e)
		{
			Utils.popDebug(this, "Exception : " + e.getMessage());
		}

	}

	public void onBackPressed()
	{
		if(path != null){
			int lastSlash = path.lastIndexOf('/');
			if (lastSlash !=0 ){
				path = path.substring(0, lastSlash);
			}
			else {path = null;}
			Intent intent = getIntent();
			intent.removeExtra(MainActivity.EXTRA_MESSAGE);
			intent.removeExtra(MainActivity.EXTRA_MESSAGE_ID);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
			intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
		}
		super.onBackPressed();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == ParentSearchResult)
		{
			if (resultCode == RESULT_OK)
			{
				int fathId = intent.getIntExtra("FatherId", 0);
				Intent resIntent = new Intent(getBaseContext(), AddQRcode.class);
				setResult(RESULT_OK, intent);
				int parent_id = 0;
				resIntent.putExtra("FatherId", intent.getIntExtra("FatherId", 0));
				System.out.println("Parent id reçue dans ParentSearch " + parent_id);
				finish();
			}
		}

		// else continue with any other code you need in the method
	}


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
