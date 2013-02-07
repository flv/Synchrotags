package fr.voltanite.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class NodeDisplayActivity extends Activity {

	private static String path = "";
	private static int id_pere;
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
			TextView t = (TextView)findViewById(R.id.path_pere); 
			t.setHint(path);
			
			id_pere=Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ID));	
			
			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();
			int nbLignes = nbdd.getNbNoeuds();
			ArrayList<TextView> txtvwNode = new ArrayList<TextView>();
			//final Noeud current_node;
			for (int i = 0; i < nbLignes; i ++)
			{
				final Noeud current_node= nbdd.getNoeudById(i);
				if(current_node.getPere() == id_pere){
					final String myPath = current_node.getNom();
					txtvwNode.add(new TextView(this));
					TextView btmp = txtvwNode.get(txtvwNode.size() - 1);
					btmp.setText("Noeud : " + current_node.getId() +" " + current_node.getNom() + "\n " 
							+ "Id père : " + current_node.getPere());
					btmp.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
					btmp.setClickable(true);					
					// Affichage d'un noeud par maintien du click
					btmp.setOnLongClickListener(new OnLongClickListener() {
						
						public boolean onLongClick(View v) {							
							System.out.println("entrée dans le onLongClick");
							Intent intent = new Intent (getBaseContext(), NodeDetailedDisplayActivity.class);
							intent.putExtra("NodeId", current_node.getId());
							intent.putExtra("NodeName", current_node.getNom());
							intent.putExtra("NodeCode", current_node.getContenuQrcode());
							intent.putExtra("NodeDesc", current_node.getDescription());
							intent.putExtra("NodeFather", current_node.getPere());
							intent.putExtra("NodeMeta", current_node.getMeta());
							intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, id_pere);
							startActivity(intent);
							return true;
						}
					});				
					
					
					// Navigation dans la bdd
					btmp.setOnClickListener(new OnClickListener() {
						
						public void onClick(View arg0) {
							System.out.println("Entrée dans le onClick");
							Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
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
							startActivity(intent);
						}
					});
					
					/*btmp.setOnTouchListener(new OnTouchListener() {
						public boolean onTouch(View v, MotionEvent event) {
							System.out.println("Entrée dans le onTouch");
							final int action = event.getAction();
							boolean ret = false;

							switch (action) {
							case MotionEvent.ACTION_DOWN:
								ret = false;
								break;
							case MotionEvent.ACTION_MOVE:
								ret = false;
								break;
							case MotionEvent.ACTION_UP:
								Intent intent = new Intent(getBaseContext(), NodeDisplayActivity.class);
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
								startActivity(intent);
								ret = true;
								break;
							}

							return ret;	
						}
					});*/

				}

				/*current_node= nbdd.getNoeudById(i);
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
				});*/
			}
			nbdd.close();
			for (TextView btn : txtvwNode)
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
		int lastSlash = path.lastIndexOf('/');
		if (lastSlash !=0 ){
			path = path.substring(0, lastSlash);
		}
		Intent intent = getIntent();
		intent.removeExtra(MainActivity.EXTRA_MESSAGE);
		intent.removeExtra(MainActivity.EXTRA_MESSAGE_ID);
		intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
		intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
		super.onBackPressed();
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
