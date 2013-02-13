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
import android.widget.TextView;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class NodeSearchByCode extends Activity {

	private static String path;
	private static int id_pere;
	private static String searchedCode;
	String qrcode;



	public void setPath(String npath)
	{
		Intent intent = getIntent();
		intent.removeExtra(MainActivity.EXTRA_MESSAGE);
		intent.putExtra(MainActivity.EXTRA_MESSAGE, npath);
		path = npath;
	}


	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);    
		setContentView(R.layout.activity_display_noeuds);
		searchedCode= getIntent().getStringExtra("qrCode");
		fillNodeLayout();

	}
	
	public void BddRaz(View v)
	{
		NoeudsBDD nbdd = new NoeudsBDD(this);
		nbdd.open();
		nbdd.raz();
		nbdd.close();
		Intent intent = new Intent(this, NodeSearchByCode.class);		
		intent.putExtra(MainActivity.EXTRA_MESSAGE, "/Racine");
		intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(MainActivity.id_racine));
		startActivity(intent);
		finish();
	}

	protected void fillNodeLayout() {
		try 
		{

			View linearLayout = findViewById(R.id.database_nodes_layout);
			Intent intent = getIntent();
			path = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
			if (path.equals("/Racine"))
			{
				setPath("");
			}
			TextView t = (TextView)findViewById(R.id.path_pere); 
			t.setHint(path);

			id_pere=Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_MESSAGE_ID));	

			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();
			ArrayList<TextView> txtvwNode = new ArrayList<TextView>();
			ArrayList<LinearLayout> nodes = new ArrayList<LinearLayout>();
			
			ArrayList<Noeud> RequestedNodes = nbdd.getNoeudsByCode(searchedCode);
 			ArrayList<Noeud> AllNodes = nbdd.getNoeuds();
			int maxId = 0;
			for (Noeud node : AllNodes)
			{
				if (node.getId() > maxId)
				{
					maxId = node.getId();
				}
			}		
			
			for (int i = 0; i <= maxId; i ++)
			{

				LinearLayout nodeLayout = new LinearLayout(this);
				nodeLayout.setOrientation(LinearLayout.HORIZONTAL);	
				nodeLayout.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				final Noeud current_node;
				try 
				{
					System.out.println("Non exception i = " + i );
					current_node = nbdd.getNoeudById(i);
					
					if(current_node.getPere() == id_pere){
						
						final String myPath = current_node.getNom();
						txtvwNode.add(new TextView(this));
						TextView btmp = txtvwNode.get(txtvwNode.size() - 1);
						btmp.setText(current_node.toString());
						btmp.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.MATCH_PARENT));
						btmp.setClickable(true);	
						if (RequestedNodes.contains(current_node))
						{
							btmp.setBackgroundColor(0xFFFFF);
						}

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
								Intent intent = new Intent(getBaseContext(), NodeSearchByCode.class);
								if(path == null)
								{
									setPath("/"+ myPath);
								}
								else{
									setPath(path + "/" + myPath);
								}
								id_pere = current_node.getId();
								intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
								intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
								startActivity(intent);
							}
						});

						nodeLayout.addView(btmp);

						Button btnRm = new Button(this);
						btnRm.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
						btnRm.setText(" - ");
						btnRm.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Noeud nodeToRmv = new Noeud();
								nodeToRmv.setId(current_node.getId());
								nodeToRmv.setContenuQrcode(current_node.getContenuQrcode());
								nodeToRmv.setNom(current_node.getNom());
								nodeToRmv.setDescription(current_node.getDescription());
								nodeToRmv.setMeta(current_node.getMeta());
								nodeToRmv.setPere(current_node.getPere());
								NoeudsBDD nbdd = new NoeudsBDD(getBaseContext());
								nbdd.open();
								try {
									nbdd.removeNoeud(nodeToRmv);
								} catch (NoMatchableNodeException e) {
									// TODO Auto-generated catch block
									Utils.popDebug(getBaseContext(), e.getMessage());
								}
								finally
								{
									nbdd.close();
								}
								((LinearLayout)findViewById(R.id.database_nodes_layout)).removeAllViews();
								fillNodeLayout();

							}
						});

						nodeLayout.addView(btnRm);
						nodes.add(nodeLayout);

					}
				}
				catch (NoMatchableNodeException e)
				{
					System.out.println("exception " + e.getMessage() + "    i = " + i );
				}

//>>>>>>> c14f38d2954b7907966cb7d0fe91f07adceaa77e
			}
			nbdd.close();
			for (LinearLayout node : nodes)
			{
				((ViewGroup) linearLayout).addView(node);
			}
		}

		catch (Exception e)
		{
			Utils.popDebug(this, "Exception : " + e.getMessage());
			Intent intentExit = new Intent(this, MainActivity.class);
			startActivity(intentExit);
			finish();
		}
	}


	public void onBackPressed()
	{
//<<<<<<< HEAD
//		if(path != null){
//			int lastSlash = path.lastIndexOf('/');
//			if (lastSlash !=0 ){
//				path = path.substring(0, lastSlash);
//			}
//			else{path = null;}
//			Intent intent = getIntent();
//			intent.removeExtra(MainActivity.EXTRA_MESSAGE);
//			intent.removeExtra(MainActivity.EXTRA_MESSAGE_ID);
//			intent.putExtra(MainActivity.EXTRA_MESSAGE, path);
//			intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
//		}
//		super.onBackPressed();
//=======
		System.out.println("backpressed");
		System.out.println("ExMess : " + getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE));
		if (getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE).length() == 0)
		{
			Intent intent = new Intent (this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{

			int lastSlash = path.lastIndexOf('/');
			System.out.println("Path : " +path);
			System.out.println("Slash : " +lastSlash);
			if (path.substring(0, lastSlash).length() == 0)
			{
				setPath("/Racine");
			}
			else {
				setPath(path.substring(0, lastSlash));
			}
			System.out.println("Path : " + path);
			Intent intent = getIntent();
			intent.putExtra(MainActivity.EXTRA_MESSAGE, path);

			NoeudsBDD nbdd = new NoeudsBDD(this);
			nbdd.open();
			try {
				Noeud node = nbdd.getNoeudById(id_pere);
				id_pere = node.getPere();
			} catch (NoMatchableNodeException e) {
				Utils.popDebug(this, e.getMessage());
			}
			finally{
				nbdd.close();
			}

			intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(id_pere));
			startActivity(intent);
		}
//>>>>>>> c14f38d2954b7907966cb7d0fe91f07adceaa77e
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
