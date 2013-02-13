package fr.voltanite.noeud;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoeudsBDD {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "Synchrotags.db";

	// Champs de la table table_noeuds	
	private static final String TABLE_NOEUDS = DatabaseConstants.TABLE_NOEUDS;
	private static final String COL_CLE = DatabaseConstants.COL_CLE;
	private static final int NUM_COL_CLE = DatabaseConstants.NUM_COL_CLE;
	private static final String COL_NOM = DatabaseConstants.COL_NOM;
	private static final int NUM_COL_NOM = DatabaseConstants.NUM_COL_NOM;
	private static final String COL_QRCODE = DatabaseConstants.COL_QRCODE;
	private static final int NUM_COL_QRCODE = DatabaseConstants.NUM_COL_QRCODE;
	private static final String COL_DESCRIPTION = DatabaseConstants.COL_DESCRIPTION;
	private static final int NUM_COL_DESCRIPTION = DatabaseConstants.NUM_COL_DESCRIPTION;
	private static final String COL_PERE = DatabaseConstants.COL_PERE;
	private static final int NUM_COL_PERE = DatabaseConstants.NUM_COL_PERE;
	private static final String COL_META = DatabaseConstants.COL_META;
	private static final int NUM_COL_META = DatabaseConstants.NUM_COL_META;

	// Champs de la table table_meta
	private static final String TABLE_META = DatabaseConstants.TABLE_META;
	private static final String COL_CLE_META = DatabaseConstants.COL_CLE_META;
	private static final int NUM_COL_CLE_META = DatabaseConstants.NUM_COL_CLE_META;
	private static final String COL_TYPE = DatabaseConstants.COL_TYPE;
	private static final int NUM_COL_TYPE = DatabaseConstants.NUM_COL_TYPE;
	private static final String COL_CONTENU = DatabaseConstants.COL_CONTENU;
	private static final int NUM_COL_CONTENU = DatabaseConstants.NUM_COL_CONTENU;

	private SQLiteDatabase bdd;

	public BaseSQLite maBaseSQLite;

	public NoeudsBDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new BaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	public void raz(){
		// Remise à zéro de la bdd
		maBaseSQLite.onUpgrade(bdd, VERSION_BDD, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}

	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD(){
		return bdd;
	}



	/* --------------------------------------------------------------
	 * Methodes d'acces aux données contenues dans la table Noeuds 
	 *  
	 * --------------------------------------------------------------
	 */
	public int getNbNoeuds()
	{
		Cursor curs = bdd.rawQuery("select * from " + TABLE_NOEUDS + ";", null);
		return curs.getCount();
	}

	public long insertNoeud(Noeud noeud) throws NoMatchableNodeException
	{
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_NOM, noeud.getNom());
		values.put(COL_QRCODE, noeud.getContenuQrcode());
		values.put(COL_DESCRIPTION, noeud.getDescription());
		values.put(COL_PERE, noeud.getPere());
		values.put(COL_META, noeud.getMeta());

		//on insère l'objet dans la BDD via le ContentValues

		return bdd.insertWithOnConflict(TABLE_NOEUDS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public long updateNoeud(Noeud ancienNoeud, Noeud nouveauNoeud) throws NoMatchableNodeException
	{

		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_NOM, nouveauNoeud.getNom());
		values.put(COL_PERE, nouveauNoeud.getPere());
		values.put(COL_QRCODE, nouveauNoeud.getContenuQrcode());
		values.put(COL_DESCRIPTION, nouveauNoeud.getDescription());
		values.put(COL_META, nouveauNoeud.getMeta());

		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_CLE + " = " + ancienNoeud.getId() + ";", null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Impossible de mettre à jour un noeud non inséré dans la bd");
		}
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.update(TABLE_NOEUDS, values, COL_CLE + "=" + ancienNoeud.getId(), null);
	}
	
	public long updateMeta(Metadata ancienneMeta, Metadata nouvelleMeta) throws NoMatchableNodeException
	{
		ContentValues values = new ContentValues();
		
		values.put(COL_CLE_META, nouvelleMeta.getId());
		values.put(COL_TYPE, nouvelleMeta.getType());
		values.put(COL_CONTENU, nouvelleMeta.getData());
		
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + " where " + COL_CLE_META + " = " + ancienneMeta.getId() 
				+ " and " + COL_TYPE + " = '" + ancienneMeta.getType() + "';"			
				, null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("La métadonnée à mettre à jour n'est pas présente dans la bdd");
		}
		
		return bdd.update(TABLE_META, values, COL_CLE_META + " = " + ancienneMeta.getId() + " and " + COL_TYPE + " = '" + ancienneMeta.getType() + "'" , null);
	}
	
	
	
	

	public void removeNoeud(Noeud noeud) throws NoMatchableNodeException{
		//Suppression d'un noeud de la BDD
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_CLE + " = " + noeud.getId() + ";" 
				, null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Le noeud à supprimmer n'est pas présent dans la BD");
		}
		bdd.delete(TABLE_NOEUDS, COL_CLE + " = " + noeud.getId(), null);
		c = bdd.rawQuery("select * from " + TABLE_META + " where " + COL_CLE_META + " = " + noeud.getId() + ";", null);
		if (c.getCount() != 0)
		{
			ArrayList<Metadata> metas = getMetasById(noeud.getId());
			for (Metadata meta : metas)
			{
				removeMeta(meta);
			}
		}
		
		c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_PERE + " = " + noeud.getId() + ";", null);
		if (c.getCount() != 0)
		{
			ArrayList<Noeud> enfants = getNoeudsByPere(noeud.getId());
			for (Noeud enfant : enfants)
			{
				Noeud newEnf = enfant;
				newEnf.setPere(0);
				updateNoeud(enfant, newEnf);
			}
		}

	}

	public Noeud getNoeudById(int id) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_CLE + " = " + id + ";" , null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Aucun résultat pour getNoeudById(" + id +")");
		}
		else {
			c.moveToFirst();
			return cursorToNoeud(c);
		}
	}

	public Noeud getNoeudByNom(String nom) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_NOM + " = " + nom + ";", null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Aucun résultat pour getNoeudByNom(" + nom +")");
		}
		else {
			c.moveToFirst();
			return cursorToNoeud(c);
		}
	}	

	public ArrayList<Noeud> getNoeudsByCode(String code) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_QRCODE + " = '" +code + "';", null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Aucun résultat pour getNoeudByCode(" + code +")");
		}
		else {
			ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
			int nbRes = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < nbRes; i ++)
			{
				noeuds.add(cursorToNoeud(c));
				if (c.getCount() != i + 1)
				{
					c.move(1);
				}
			}
			c.close();
			return noeuds;
		}
	}

	public ArrayList<Noeud> getNoeudsByPere(int id) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_PERE + " = " + id + ";", null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Aucun résultat pour getNoeudsByPere(" + id +")");
		}
		else 
		{
			ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
			int nbRes = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < nbRes; i ++)
			{
				noeuds.add(cursorToNoeud(c));
				if (c.getCount() != i + 1)
				{
					c.move(1);
				}
			}
			c.close();
			return noeuds;			
		}
	}

	public ArrayList<Noeud> getFils(Noeud noeud) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_PERE + " = " + noeud.getId() + ";", null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Aucun résultat pour getFils()");
		}
		else 
		{
			ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
			int nbRes = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < nbRes; i ++)
			{
				noeuds.add(cursorToNoeud(c));
				if (c.getCount() != i + 1)
				{
					c.move(1);
				}
			}
			c.close();
			return noeuds;			
		}
	}

	//Cette méthode permet de convertir un cursor en un noeud
	public static Noeud cursorToNoeud(Cursor c)
	{
		Noeud noeud = new Noeud();
		noeud.setId(c.getInt(NUM_COL_CLE));
		noeud.setNom(c.getString(NUM_COL_NOM));
		noeud.setContenuQrcode(c.getString(NUM_COL_QRCODE));
		noeud.setDescription(c.getString(NUM_COL_DESCRIPTION));
		noeud.setPere(c.getInt(NUM_COL_PERE));
		noeud.setMeta(c.getInt(NUM_COL_META));
		return noeud;
	}

	/* ----------------------------------------------------------------
	 * 
	 * Méthodes d'accès aux données concernant les métadonnées
	 * 
	 * ----------------------------------------------------------------
	 */

	public int getNbMeta()
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + ";", null);
		return c.getCount();
	}

	public void insertMeta(Metadata meta) throws NoMatchableNodeException
	{
		System.out.println("Insert de " + meta.toString());
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + " where " + COL_CLE + " = " + meta.getId() +";",
				null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Pas de noeud auquel associer la métadonnée"); 
		}
		else 
		{
			//Création d'un ContentValues (fonctionne comme une HashMap)
			ContentValues values = new ContentValues();
			//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
			values.put(COL_CLE_META, meta.getId());
			System.out.println("clé méta : " + meta.getId());
			values.put(COL_TYPE, meta.getType());
			System.out.println("type méta : " + meta.getType());
			values.put(COL_CONTENU, meta.getData());
			System.out.println("contenu méta : " + meta.getData());
			//on insère l'objet dans la BDD via le ContentValues
			bdd.insertWithOnConflict(TABLE_META, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	public void removeMeta(Metadata meta) throws NoMatchableNodeException
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + " where " + COL_CLE_META + " = " + meta.getId() +
				" and " + COL_TYPE + " = '" + meta.getType() +"';",
				null);
		if (c.getCount() == 0)
		{
			throw new NoMatchableNodeException("Cette metadonnée n'est pas présente dans la bd"); 
		}	
		else 
		{
			bdd.delete(TABLE_META, COL_CLE_META + " = " + meta.getId() + " and " + COL_TYPE + " = '" + meta.getType() + "'", null);			
		}
	}

	public ArrayList<Metadata> getMetasById(int id) throws NoMatchableNodeException
	{
		ArrayList<Metadata> metas = new ArrayList<Metadata>();
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + " where (" + COL_CLE_META + " = " + id + ");", null);
		int nbRes = c.getCount();

		if (nbRes == 0) {
			return metas;			
		}

		c.moveToFirst();

		for (int i = 0; i < nbRes; i ++)
		{
			metas.add(cursorToMeta(c));
			if (c.getCount() != i + 1)
			{
				c.move(1);
			}
		}
		c.close();
		return metas;
	}

	/*public Metadata[] getAllMetas()
	{
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + ";", 
								null);
		if (c.getCount() == 0) 
		{
			return new Metadata[0];
		}

		metas = new Metadata[c.getCount()];
		c.moveToFirst();
		return cursorToMeta(c);
	}*/

	public Metadata cursorToMeta(Cursor c){
		// TODO Auto-generated method stub
		Metadata meta = new Metadata();
		meta.setId(c.getInt(NUM_COL_CLE_META));
		meta.setType(c.getString(NUM_COL_TYPE));
		meta.setData(c.getString(NUM_COL_CONTENU));
		return meta;
	}

	public ArrayList<Metadata> getMetas() {
		ArrayList<Metadata> metas = new ArrayList<Metadata>();
		Cursor c = bdd.rawQuery("select * from " + TABLE_META + ";", null);
		int nbRes = c.getCount();

		if (nbRes == 0) {
			return metas;			
		}

		c.moveToFirst();

		for (int i = 0; i < nbRes; i ++)
		{
			metas.add(cursorToMeta(c));
			if (c.getCount() != i + 1)
			{
				c.move(1);
			}
		}
		c.close();
		return metas;
	}

	public ArrayList<Noeud> getNoeuds() {
		ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
		Cursor c = bdd.rawQuery("select * from " + TABLE_NOEUDS + ";", null);
		int nbRes = c.getCount();
		if (nbRes == 0)
		{
			return noeuds;
		}

		c.moveToFirst();

		for (int i = 0; i < nbRes; i++)
		{
			noeuds.add(cursorToNoeud(c));
			if (c.getCount() != i + 1)
			{
				c.move(1);
			}
		}
		c.close();
		return noeuds;
	}


}