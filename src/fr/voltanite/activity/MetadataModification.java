package fr.voltanite.activity;

import fr.voltanite.noeud.Metadata;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MetadataModification extends Activity {
	private static int ID;
	private static String TYPE;
	private static String CONTENT;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metadata_modification);
	}

	protected void onResume()
	{
		super.onResume();
		Intent intent = getIntent();
		View typeV = findViewById(R.id.metadata_modification_type_input);
		((TextView) typeV).setText(intent.getStringExtra("MetaType"));
		TYPE = intent.getStringExtra("MetaType");
		View contentV = findViewById(R.id.metadata_modification_content_input);
		((TextView) contentV).setText(intent.getStringExtra("MetaCont"));
		CONTENT = intent.getStringExtra("MetaCont");
		ID = intent.getIntExtra("MetaId", 0);
	}


	public void validate(View v)
	{
		View typeV = findViewById(R.id.metadata_modification_type_input);
		View contentV = findViewById(R.id.metadata_modification_content_input);


		Metadata oldMeta = new Metadata();
		oldMeta.setId(ID);
		System.out.println("old id " + ID);
		oldMeta.setType(TYPE);
		System.out.println("old type " + TYPE);
		oldMeta.setData(CONTENT);
		System.out.println("old data " + CONTENT);

		if (  	(!((TextView)typeV).getText().toString().equals(TYPE)) &&
				(!((TextView)typeV).getText().toString().equals(""))
				)
		{
			TYPE = ((TextView)typeV).getText().toString();
		}
		if (	(!((TextView)contentV).getText().toString().equals(CONTENT)) &&
				(!((TextView)contentV).getText().toString().equals(""))
				)
		{
			CONTENT = ((TextView)contentV).getText().toString();
		}
		Metadata newMeta = new Metadata();
		newMeta.setId(ID);
		System.out.println("new id " + ID);
		newMeta.setType(TYPE);
		System.out.println("new type " + TYPE);
		newMeta.setData(CONTENT);
		System.out.println("new data " + CONTENT);

		NoeudsBDD nbdd = new NoeudsBDD(this);
		nbdd.open();
		try {
			System.out.println(nbdd.updateMeta(oldMeta, newMeta));
			System.out.println("Nb metas : " + nbdd.getNbMeta());
			setResult(RESULT_OK);
			finish();
		} catch (NoMatchableNodeException e) {
			// TODO Auto-generated catch block
			Utils.popDebug(this, e.getMessage());
		}
	}

}
