package org.hit.burkun.entities;

public class Entity {
	private String name;
	private String uid;
	private String dbid;
	private String def;
	private String comefrom;
	private TYPE type;
	private String primary_id;

	public enum TYPE {
		GENE_ID("gene_id"), 
		GENE_ONTOLOGY("gene_ontology"), 
		DISEASE_ONTOLOGY("disease_ontology"), 
		HP_ONTOLOGY("hp_ontology"),
		GENE_SYMBOL("gene_symbol"), 
		UNIPORT_AC("uniport_ac"),
		HGNC("hgnc"),
		OMIM("omim");
		
		private final String tag;

		TYPE(String tag) {
			this.tag = tag;
		}

		/** @return tag */
		public String getTag() {
			return tag;
		}

		@Override
		public String toString() {
			return tag;
		}
		
		public static TYPE getTypeByTag(String tag){
			if(tag.equals(GENE_ID.getTag())) return GENE_ID;
			if(tag.equals(GENE_ONTOLOGY.getTag())) return GENE_ONTOLOGY;
			if(tag.equals(DISEASE_ONTOLOGY.getTag())) return DISEASE_ONTOLOGY;
			if(tag.equals(HP_ONTOLOGY.getTag())) return HP_ONTOLOGY;
			if(tag.equals(GENE_SYMBOL.getTag())) return GENE_SYMBOL;
			if(tag.equals(UNIPORT_AC.getTag())) return UNIPORT_AC;
			if(tag.equals(HGNC.getTag())) return HGNC;
			if(tag.equals(OMIM.getTag())) return OMIM;
			return null;
		}
	}

	public Entity(String primaryID, String name, String uid, String dbid, TYPE type) {
		this.primary_id = primaryID;
		this.name = name;
		this.uid = uid;
		this.dbid = dbid;
		this.type = type;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "name: " + name + "\n" + "uid: " + uid + "\n" + "dbid: " + dbid
				+ "\n" + "def: " + def + "\n" + "comfrom: " + comefrom + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entity) {
			return ((Entity) obj).getUid().equals(this.uid)
					&& ((Entity) obj).getType() == this.type
					&& ((Entity) obj).getDbid().equals(this.dbid);
		}
		return false;
	}

	/**
	 * 实体的名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            实体的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return uid 实体的全局ID
	 */
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComfrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getPrimary_id() {
		return primary_id;
	}

	public void setPrimary_id(String primary_id) {
		this.primary_id = primary_id;
	}
	
}
