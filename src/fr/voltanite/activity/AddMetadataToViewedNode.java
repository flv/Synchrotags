package fr.voltanite.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import fr.voltanite.noeud.Metadata;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class AddMetadataToViewedNode extends Activity {
	private static Metadata meta;
	private static String TYPE;
	private static String CONTENT;
	private static int ID;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_metadata);
		Intent intent = getIntent();
		meta = new Metadata();
		TYPE = "Metadata type stub";
		CONTENT = "Metadata content stub";
		ID = intent.getIntExtra("MetaId", 0);
	}

	public void onValidate(View view)
	{
		//Intent intent = new Intent();
		TextView ttype = (TextView) findViewById(R.id.metadata_add_type);
		String type = ttype.getText().toString();
		TextView tcont = (TextView) findViewById(R.id.metadata_add_content);		
		String content = tcont.getText().toString();
		if (!type.equals(""))					
		{
			TYPE = type;
		}
		if (!content.equals(""))
		{
			CONTENT = content;
		}
		meta.setId(ID);
		meta.setType(TYPE);
		meta.setData(CONTENT);
		NoeudsBDD nbdd = new NoeudsBDD(this);
		nbdd.open();
		try {
			nbdd.insertMeta(meta);
		} catch (NoMatchableNodeException e) {
			Utils.popDebug(this, e.getMessage());
		}
		finally {
			nbdd.close();
		}
		setResult(RESULT_OK);
		finish();
	}
}
