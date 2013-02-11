package fr.voltanite.activity;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import fr.voltanite.noeud.Metadata;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class AddQRcode extends Activity {
	private static String QRCODE;
	private static String NAME = "Node name stub";
	private static String DESC = "Node desc stub";
	private static int FATHER = 0;
	public static ArrayList<Metadata> METAS = new ArrayList<Metadata>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_qrcode);
		Intent intent = getIntent();
		QRCODE = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		findViewById(R.id.node_creation_parent_search).setOnClickListener(rechercheparent);
		findViewById(R.id.node_creation_metadata_add).setOnClickListener(meta);
		findViewById(R.id.node_creation_validate).setOnClickListener(validation);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_qrcode, menu);
		return true;
	}

	public void onResume()
	{
		super.onResume();
		NAME = "Node name stub";
		DESC = "Node desc stub";
		FATHER = 0;
	}

	public final OnClickListener rechercheparent = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), ParentSearch.class);
			startActivity(intent);
		}
	};

	public final OnClickListener meta = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), AddMetadataActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
		}
	};

	public final OnClickListener validation = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);

			TextView tnom = (TextView) findViewById(R.id.node_creation_name_input);
			String nom = tnom.getText().toString();
			TextView tpar = (TextView) findViewById(R.id.node_creation_parent_input);					
			String parent = tpar.getText().toString();
			TextView tdesc = (TextView) findViewById(R.id.node_creation_desc_input);
			String desc = tdesc.getText().toString();
			if (!nom.equals(""))					{
				NAME = nom;
			}
			if (!desc.equals(""))
			{
				DESC = desc;
			}
			if (!parent.equals(""))
			{
				int fath = Integer.parseInt(parent);
				FATHER = fath;
			}

			Noeud noeud = new Noeud(NAME, QRCODE, DESC, FATHER, 0);
			NoeudsBDD nbdd = new NoeudsBDD(getBaseContext());
			
			try {
				System.out.println("entrer dans le try");
				nbdd.open();
				System.out.println("bdd ouverte");
				ArrayList<Noeud> bd_noeuds = nbdd.getNoeuds();
				System.out.println("getnoeuds effectué");
				ArrayList<Integer> ids = new ArrayList<Integer>();
				System.out.println("Arralists init");
				for (Noeud node : bd_noeuds)
				{
					ids.add(node.getId());
					System.out.println("ids.add("+ node.getId() + ")");
					ids.add(node.getMeta());
					System.out.println("ids.add(" + node.getMeta() + ")");
				}
				int max = 0;
				for (Integer val : ids )
				{
					if (val > max)
					{
						max = val;
					}
				}
				max += 1;
				noeud.setMeta(max);
				System.out.println("meta du noeud ajouté : (" + noeud.getMeta() + ")");
				nbdd.insertNoeud(noeud);
				System.out.println("noeud ajouté");
				for(Metadata meta : METAS)
				{
					meta.setId(max);
					nbdd.insertMeta(meta);					
				}				
				nbdd.close();
			} catch (NoMatchableNodeException e) {
				// TODO Auto-generated catch block
				Utils.popDebug(getBaseContext(), "Exception : " + e.getMessage());
			}
			

			METAS = new ArrayList<Metadata>();
			startActivity(intent);
		}
	};
}
