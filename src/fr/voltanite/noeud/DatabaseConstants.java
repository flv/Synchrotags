package fr.voltanite.noeud;

public class DatabaseConstants {
	public static final int VERSION_BDD = 1;
	public static final String NOM_BDD = "Synchrotags.db";

	public static final String TABLE_NOEUDS = "TABLE_NOEUDS";
	public static final String COL_CLE = "CLE";
	public static final int NUM_COL_CLE = 0;
	public static final String COL_NOM = "NOM";
	public static final int NUM_COL_NOM = 1;
	public static final String COL_QRCODE = "QRCODE";
	public static final int NUM_COL_QRCODE = 2;
	public static final String COL_PERE = "PERE";
	public static final int NUM_COL_PERE = 3;
	public static final String COL_META = "METADONNEES";
	public static final int NUM_COL_META = 4;

	public static final String TABLE_META = "TABLE_META";
	public static final String COL_CLE_META = "CLE";
	public static final int NUM_COL_CLE_META = 0;
	public static final String COL_TYPE = "TYPE_METADONNEE";
	public static final int NUM_COL_TYPE = 1;
	public static final String COL_CONTENU = "CONTENU_METADONNEE";
	public static final int NUM_COL_CONTENU = 2;
}
