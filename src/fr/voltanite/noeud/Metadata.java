package fr.voltanite.noeud;

public class Metadata {
	
	private int id;
	private String type;
	private String data;
	
	
	public Metadata(int id, String type, String data) {
		super();
		this.id = id;
		this.type = type;
		this.data = data;
	}
	
	public Metadata() {
		super();
		id = 0;
		data = "";
		type = "";
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Metadata) {
			if (((Metadata) o).getId() == id && ((Metadata) o).getData() == data && ((Metadata) o).getType() == type)
			{
				return true;
			}
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Metadata : clé " + id + " type " + type + " contenu " + data;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	
}
