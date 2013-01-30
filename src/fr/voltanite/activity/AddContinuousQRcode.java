package fr.voltanite.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class AddContinuousQRcode extends Activity {
	private static String QRCODE;
	private static String NAME = "Node name stub";
	private static String DESC = "Node desc stub";
	private static int FATHER = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_continuous_qrcode);
		Intent intent = getIntent();
		QRCODE = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		findViewById(R.id.add_meta).setOnClickListener(meta);
		findViewById(R.id.validation).setOnClickListener(validation);

		EditText paramParent = null;
		paramParent = (EditText)findViewById(R.id.saisie_nom);
		if(QRCODE != null)
			paramParent.setHint(QRCODE);

	}
	
	 public void onResume()
	    {
	    	super.onResume();
	    	NAME = "Node name stub";
	    	DESC = "Node desc stub";
	    	FATHER = 0;
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_qrcode, menu);
		return true;
	}

	public final OnClickListener meta = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(intent);
		}
	};

	public final OnClickListener validation = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);

			TextView tnom = (TextView) findViewById(R.id.saisie_nom);
			String nom = tnom.getText().toString();
			TextView tpar = (TextView) findViewById(R.id.saisie_parent);					
			String parent = tpar.getText().toString();
			TextView tdesc = (TextView) findViewById(R.id.saisie_desc);
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
			nbdd.open();
			try {
				nbdd.insertNoeud(noeud);
			} catch (NoMatchableNodeException e) {
				// TODO Auto-generated catch block
				Utils.popDebug(getBaseContext(), "Exception : " + e.getMessage());
			}
			nbdd.close();
			startActivity(intent);
		}
	};

}
