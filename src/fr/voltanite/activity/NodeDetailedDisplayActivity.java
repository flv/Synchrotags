package fr.voltanite.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.voltanite.noeud.Metadata;
import fr.voltanite.noeud.NoMatchableNodeException;
import fr.voltanite.noeud.Noeud;
import fr.voltanite.noeud.NoeudsBDD;
import fr.voltanite.utils.Utils;

public class NodeDetailedDisplayActivity extends Activity {

	private static Noeud NODE;
	private static String NAME;
	private static String CODE;
	private static String DESC;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_node_display);
		// Récupération du noeud
		Intent intent = getIntent();
		NODE = new Noeud();
		NODE.setId(intent.getIntExtra("NodeId", 0));
		NODE.setNom(intent.getStringExtra("NodeName"));
		NODE.setContenuQrcode(intent.getStringExtra("NodeCode"));
		NODE.setDescription(intent.getStringExtra("NodeDesc"));
		NODE.setPere(intent.getIntExtra("NodeFather", 0));
		NODE.setMeta(intent.getIntExtra("NodeMeta", 0));
		NAME = NODE.getNom();
		CODE = NODE.getContenuQrcode();
		DESC = NODE.getDescription();

		// Remplissage des champs texte du layout
		((TextView) findViewById(R.id.detailed_node_display_name)).setText(NAME);
		((TextView) findViewById(R.id.detailed_node_display_codecontent)).setText(CODE);
		((TextView) findViewById(R.id.detailed_node_display_description)).setText(DESC);
		// Génération des visualisation de métadonnées
		fillMetas();

	}

	private void fillMetas()
	{
		NoeudsBDD nbdd = new NoeudsBDD(this);
		nbdd.open();

		try {
			View llayout = findViewById(R.id.detailed_node_display_metadatas_layout);
			ArrayList<Metadata> metas = nbdd.getMetasById(NODE.getMeta());
			nbdd.close();
			for (int i = 0; i < metas.size(); i ++)
			{
				final Metadata meta = metas.get(i);
				LinearLayout metaLayout = new LinearLayout(this);
				TextView metaType = new TextView(this);
				metaType.setText(meta.getType() + "   ");
				metaType.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				TextView metaContent = new TextView(this);
				metaContent.setText(meta.getData() + "    ");
				metaContent.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				Button btnRm = new Button(this);
				btnRm.setText(" - ");
				Button btnEdit = new Button(this);
				btnEdit.setText("Edit");

				btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(), MetadataModification.class);
						intent.putExtra("MetaId", meta.getId());
						intent.putExtra("MetaType", meta.getType());
						intent.putExtra("MetaCont", meta.getData());
						startActivityForResult(intent, 3);
					}
				});

				btnRm.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Metadata metaToRm = new Metadata();
						metaToRm.setId(meta.getId());
						metaToRm.setData(meta.getData());
						metaToRm.setType(meta.getType());
						NoeudsBDD nbdd = new NoeudsBDD(getBaseContext());
						nbdd.open();
						try {
							nbdd.removeMeta(metaToRm);
						} catch (NoMatchableNodeException e) {
							// TODO Auto-generated catch block
							Utils.popDebug(getBaseContext(), e.getMessage());
						}
						finally {
							nbdd.close();
						}
						((LinearLayout) findViewById(R.id.detailed_node_display_metadatas_layout)).removeAllViews();
						fillMetas();
					}
				});
				
				metaLayout.addView(metaType);
				metaLayout.addView(metaContent);
				metaLayout.addView(btnEdit);
				metaLayout.addView(btnRm);

				((LinearLayout) llayout).addView(metaLayout);
			}

			Button btnAdd = new Button(this);
			btnAdd.setText("Add Metadata");

			btnAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent (getBaseContext(), AddMetadataToViewedNode.class);
					intent.putExtra("MetaId", NODE.getMeta());
					startActivityForResult(intent, 4);
				}
			});

			((LinearLayout) llayout).addView(btnAdd);

		} catch (NoMatchableNodeException e) {
			// TODO Auto-generated catch block
			Utils.popDebug(this, e.getMessage());
		}
	}

	public void onResume()
	{

		super.onResume();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 4) {

			if(resultCode == RESULT_OK){

				Utils.emptyLinearLayout(this, this, R.id.detailed_node_display_metadatas_layout);
				fillMetas();
			}

		} 
	}

	public void onValidate(View view)
	{

		if (!((TextView) findViewById(R.id.detailed_node_display_name)).getText().toString().equals(NODE.getNom()))
		{
			NAME = ((TextView) findViewById(R.id.detailed_node_display_name)).getText().toString();
		}
		if (!((TextView) findViewById(R.id.detailed_node_display_description)).getText().toString().equals(NODE.getDescription()))
		{
			DESC = ((TextView) findViewById(R.id.detailed_node_display_description)).getText().toString();
		}
		if (!((TextView) findViewById(R.id.detailed_node_display_codecontent)).getText().toString().equals(NODE.getContenuQrcode()))
		{
			CODE = ((TextView) findViewById(R.id.detailed_node_display_codecontent)).getText().toString();
		}
		Noeud newNode = new Noeud(NAME, CODE, DESC, NODE.getPere(), NODE.getMeta());
		NoeudsBDD nbdd = new NoeudsBDD(this);
		nbdd.open();
		try {
			nbdd.updateNoeud(NODE, newNode);
		} catch (NoMatchableNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			nbdd.close();
		}
		Intent intent = new Intent (this, NodeDisplayActivity.class);
		intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(intent.getIntExtra(MainActivity.EXTRA_MESSAGE_ID, 0)));
		startActivity(intent);
		finish();
	}
	
	public void onRemove(View v)
	{
		Noeud nodeToRmv = NODE;
		NoeudsBDD nbdd = new NoeudsBDD(this);
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
			Intent intent = new Intent(this, NodeDisplayActivity.class);
			intent.putExtra(MainActivity.EXTRA_MESSAGE, "/Racine");
			intent.putExtra(MainActivity.EXTRA_MESSAGE_ID, String.valueOf(MainActivity.id_racine));
			startActivity(intent);
		}
	}
}
