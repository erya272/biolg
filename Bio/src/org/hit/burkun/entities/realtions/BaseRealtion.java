package org.hit.burkun.entities.realtions;



import org.hit.burkun.db.DBTypeConverter.DATATYPE;
import org.hit.burkun.entities.Entity;

public class BaseRealtion {
	public enum TYPE {
		//这里默认都是双向
		DO_GO("do_go"),
		GO_DO("go_do"),
		DO_HP("do_hp"),
		HP_DO("hp_do"),
		GO_HP("go_hp"),
		HP_GO("hp_go"),
		GOMF_GOBP("gomf_gobp"),
		GOBP_GOMF("gobp_gomf"),
		GOCC_GOMF("gocc_gomf"),
		GOMF_GOCC("gomf_gocc"),
		GOBP_GOCC("gobp_gocc"),
		GOCC_GOBP("gocc_gobp"),
		GENE_DISEASE("gene_disease"),
		DISEASE_GENE("disease_gene"),
		GENE_GENE("gene_gene"),
		DISEASE_DISEASE("disease_disease"),
		DO_DO("do_do"), 
		GO_GO("go_go"),
		HP_HP("hp_hp"),
		DO_GENE("do_gene"),
		HP_GENE("hp_gene"),
		GO_GENE("go_gene"),
		GENE_DO("gene_do"),
		GENE_GO("gene_go"),
		GENE_HP("gene_hp"),
		UNPORT_UNPORT("uniport_uniport");
		private final String tag;

		TYPE( String tag) {
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
			if(tag.equals(DO_DO.getTag())) return DO_DO;
			if(tag.equals(HP_DO.getTag())) return HP_DO;
			if(tag.equals(GO_HP.getTag())) return GO_HP;
			if(tag.equals(GOMF_GOBP.getTag())) return GOMF_GOBP;
			if(tag.equals(GOMF_GOCC.getTag())) return GOMF_GOCC;
			if(tag.equals(GOBP_GOCC.getTag())) return GOBP_GOCC;
			if(tag.equals(GENE_DISEASE.getTag())) return GENE_DISEASE;
			if(tag.equals(DISEASE_GENE.getTag())) return DISEASE_GENE;
			if(tag.equals(GENE_GENE.getTag())) return GENE_GENE;
			if(tag.equals(DISEASE_DISEASE.getTag())) return DISEASE_DISEASE;
			if(tag.equals(DO_GO.getTag())) return DO_GO;
			if(tag.equals(GO_GO.getTag())) return GO_GO;
			if(tag.equals(HP_HP.getTag())) return HP_HP;
			if(tag.equals(DO_GENE.getTag())) return DO_GENE;
			if(tag.equals(HP_GENE.getTag())) return HP_GENE;
			if(tag.equals(GO_GENE.getTag())) return GO_GENE;
			if(tag.equals(UNPORT_UNPORT.getTag())) return UNPORT_UNPORT;
			
			if(tag.equals(GO_DO.getTag())) return GO_DO;
			if(tag.equals(DO_HP.getTag())) return DO_HP;
			if(tag.equals(HP_GO.getTag())) return HP_GO;
			if(tag.equals(GOBP_GOMF.getTag())) return GOBP_GOMF;
			if(tag.equals(GOCC_GOMF.getTag())) return GOCC_GOMF;
			if(tag.equals(GOCC_GOBP.getTag())) return GOCC_GOBP;
			if(tag.equals(GENE_DO.getTag())) return GENE_DO;
			if(tag.equals(GENE_GO.getTag())) return GENE_GO;
			if(tag.equals(GENE_HP.getTag())) return GENE_HP;
			return null;
		}
	};
	
	
	 
	private boolean isDirect = false;
	//关系的描述，如致病，诱发等
	private String desc;
	private TYPE type;
	private Entity ent1;
	private Entity ent2;
	private String dbid; //数据库来源
	//计算置信度
	private Double confidscore = 0.0;
	//计算方式
	private String calcMethod;
	
	public BaseRealtion(Entity ent1, Entity ent2, boolean isDirect, TYPE type) {
		this.isDirect = isDirect;
		this.type = type;
		this.ent1 = ent1;
		this.ent2 = ent2;
	}

	@Override
	public String toString() {
		if(this.isDirect){
			return this.ent1.getDbid() + "->" + ent2.getDbid();
		}
		return this.ent1.getDbid() + "<->" + ent2.getDbid() + "\n";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseRealtion){
			BaseRealtion r = (BaseRealtion) obj;
			if(r.getEnt1().equals(this.ent1) && r.getEnt2().equals(this.ent2) && this.type == r.getType()){
				return true;
			}
		}
		return false;
	}
	
	public Double getConfidscore() {
		return confidscore;
	}

	public void setConfidscore(Double confidscore) {
		this.confidscore = confidscore;
	}

	public String getCalcMethod() {
		return calcMethod;
	}

	public void setCalcMethod(String calcMethod) {
		this.calcMethod = calcMethod;
	}

	public boolean isDirect() {
		return isDirect;
	}

	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Entity getEnt1() {
		return ent1;
	}

	public void setEnt1(Entity ent1) {
		this.ent1 = ent1;
	}

	public Entity getEnt2() {
		return ent2;
	}

	public void setEnt2(Entity ent2) {
		this.ent2 = ent2;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public TYPE getType(){
		return type;
	}
	public Entity getToEntity(){
		return ent1;
	}
	public Entity getFromEntiy(){
		return ent2;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	//dump to db
	public String[] dumpDBArray(){
		//Aacc, Bacc, Aname, Bname, score if any, method if any ,direction if any, direction_proper, category of relationship, category of A, category of B
		//renew ent1_pid, ent2_pid, score, score_method, isdirect, rel_desc, rel_type, ent1_type, ent2_type
		String[] res = new String[9];
		res[0] = ent1.getPrimary_id(); //记录的是entity在index_term中的id
		res[1] = ent2.getPrimary_id(); //纪录的是entity在index_term中的id
		res[2] = this.confidscore == null ? null : String.valueOf(confidscore);
		res[3] = this.calcMethod == null? null : calcMethod;
		res[4] = this.isDirect ? "1" : "0";
		res[5] = this.desc;
		res[6] = this.type.getTag();
		res[7] = this.ent1.getType().getTag();
		res[8] = this.ent2.getType().getTag();
		return res;
	}
	
	//dump inverse rel to db
	public String[] dumpReverseDBArray(){
		//Aacc, Bacc, Aname, Bname, score if any, method if any ,direction if any, direction_proper, category of relationship, category of A, category of B
		//renew ent1_pid, ent2_pid, score, score_method, isdirect, rel_desc, rel_type, ent1_type, ent2_type
		String[] res = new String[9];
		res[0] = ent2.getPrimary_id(); //记录的是entity在index_term中的id
		res[1] = ent1.getPrimary_id(); //纪录的是entity在index_term中的id
		res[2] = this.confidscore == null ? null : String.valueOf(confidscore);
		res[3] = this.calcMethod == null? null : calcMethod;
		res[4] = this.isDirect ? "1" : "0";
		res[5] = this.desc;
		String[] it = this.type.getTag().split("_");
		res[6] = it[1] + "_" + it[0];
		res[7] = this.ent2.getType().getTag();
		res[8] = this.ent1.getType().getTag();
		return res;
	}
	
	public static DATATYPE[] getDbDataTypes(){
		DATATYPE[] types  = new DATATYPE[9];
		types[0] = DATATYPE.I;
		types[1] = DATATYPE.I;
		types[2] = DATATYPE.D;
		types[4] = DATATYPE.I;
		return types;
	}
	
	
}
