package fr.voltanite.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import fr.voltanite.noeud.Metadata;

public class AddMetadataActivity extends Activity {
	private static Metadata meta;
	private static String TYPE;
	private static String CONTENT;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_metadata);
	}

	public void onResume()
	{
		super.onResume();
		meta = new Metadata();
		TYPE = "Metadata type stub";
		CONTENT = "Metadata content stub";
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
		meta.setType(TYPE);
		meta.setData(CONTENT);
		AddQRcode.METAS.add(meta);
		//intent.putExtra("newMetaInserted", true);
		setResult(RESULT_OK);
		finish();
	}
}
