package fr.voltanite.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import fr.voltanite.noeud.Metadata;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class ContinuousQRCodeFatherScan extends Activity {
	
	public static ArrayList<Metadata> METAS = new ArrayList<Metadata>();
	private static String QRCODE;
	private static String NAME;
	private static String DESC;
	private static int FATHER;
	private static int ParentSearchResult = 26;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continuous_father_scan);
		Intent mIntent = getIntent();
		QRCODE = mIntent.getStringExtra("FatherCode");
	}
	
	public void onResume()
	{
		super.onResume();
		NAME = "NameStub";
		DESC = "DescStub";
		FATHER = 0;
	}
	
	public void onBackPressed()
	{
		Intent intent = new Intent (this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void parentSearch(View v)
	{
		Intent intent = new Intent(getBaseContext(), ParentSearch.class);
		intent.putExtra(MainActivity.EXTRA_MESSAGE, "/Racine");
		intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(0));
		startActivityForResult(intent, ParentSearchResult);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == ParentSearchResult)
		{
			if (resultCode == RESULT_OK)
			{
				int fathId = intent.getIntExtra("FatherId", 0);
				((TextView)findViewById(R.id.conti_fatherscan_father)).setText("" + fathId);
				System.out.println("Parent id reçue dans ContinuousFather " + fathId);
			}
		}
	}
	
	public void createNode(View v)
	{
		Intent intent = new Intent(this, ContinuousQRCodeSonScan.class);
		
		TextView tnom = (TextView) findViewById(R.id.conti_fatherscan_saisie_nom);
		String nom = tnom.getText().toString();
		TextView tpar = (TextView) findViewById(R.id.conti_fatherscan_father);					
		String parent = tpar.getText().toString();
		TextView tdesc = (TextView) findViewById(R.id.conti_fatherscan_saisie_desc);
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
			nbdd.open();
			ArrayList<Noeud> bd_noeuds = nbdd.getNoeuds();
			ArrayList<Integer> ids = new ArrayList<Integer>();
			ArrayList<Integer> metas = new ArrayList<Integer>();
			for (Noeud node : bd_noeuds)
			{
				ids.add(node.getId());
				metas.add(node.getMeta());
			}
			int max = 0;
			for (Integer val : ids )
			{
				if (val > max)
				{
					max = val;
				}
			}
			int fatherId = max + 1;
			for (Integer val : metas)
			{
				if (val > max)
				{
					max = val;
				}
			}
			max += 1;
			noeud.setMeta(max);
			nbdd.insertNoeud(noeud);
			for(Metadata meta : METAS)
			{
				meta.setId(max);
				nbdd.insertMeta(meta);					
			}				
			nbdd.close();
			intent.putExtra("FatherId", fatherId);
			intent.putExtra("SonCode", "Son QrCode Stub");
		} catch (NoMatchableNodeException e) {
			// TODO Auto-generated catch block
			Utils.popDebug(getBaseContext(), "Exception : " + e.getMessage());
		}

		METAS = new ArrayList<Metadata>();
		startActivity(intent);
	}
	
	
	public void createMeta(View v)
	{
		Intent intent = new Intent(this, AddMetadataContinuousFatherActivity.class);
		int requestCode = 1;
		startActivityForResult(intent, requestCode);
	}
}