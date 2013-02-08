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

public class ContinuousQRCodeSonScan extends Activity {

	private static int FATHER;
	public static ArrayList<Metadata> METAS = new ArrayList<Metadata>();
	private static String NAME;
	private static String QRCODE;
	private static String DESC;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continuous_son_scan);
		FATHER = getIntent().getIntExtra("FatherId", 0);
		QRCODE = getIntent().getStringExtra("SonCode");
		View tvFather = findViewById(R.id.sonscan_father_remainder);
		((TextView) tvFather).setText("Id père : " + FATHER);
		
	}
	
	public void onResume()
	{
		super.onResume();
		NAME = "Name Stub";
		DESC = "Desc Stub";
	}
	
	public void onBackPressed()
	{
		Intent intent = new Intent(this, ContinuousQRCodeFatherScan.class);
		intent.putExtra("FatherCode", "Father QrCode Stub");
		startActivity(intent);
		finish();
	}
	
	public void createMeta(View v)
	{
		Intent intent = new Intent(this, AddMetadataContinuousSonActivity.class);
		int requestCode = 1;
		startActivityForResult(intent, requestCode);
	}
	
	public void createSon(View v)
	{
		
		TextView tnom = (TextView) findViewById(R.id.sonscan_name);
		String nom = tnom.getText().toString();
		TextView tdesc = (TextView) findViewById(R.id.sonscan_desc);
		String desc = tdesc.getText().toString();
		if (!nom.equals(""))					{
			NAME = nom;
		}
		if (!desc.equals(""))
		{
			DESC = desc;
		}

		Noeud noeud = new Noeud(NAME, QRCODE, DESC, FATHER, 0);
		NoeudsBDD nbdd = new NoeudsBDD(getBaseContext());
		
		try {
			nbdd.open();
			ArrayList<Noeud> bd_noeuds = nbdd.getNoeuds();
			ArrayList<Integer> ids = new ArrayList<Integer>();
			for (Noeud node : bd_noeuds)
			{
				ids.add(node.getId());
				ids.add(node.getMeta());
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
			nbdd.insertNoeud(noeud);
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
		Intent intent = new Intent(this, ContinuousQRCodeSonScan.class);
		intent.putExtra("FatherId", FATHER);
		intent.putExtra("SonCode", "Son QrCode Stub");
		startActivity(intent);
	}
	
	public void endScans(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
