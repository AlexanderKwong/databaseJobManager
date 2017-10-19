package com.mastercom.bigdata.db;

public enum EmbeddDBTable {

	TB_CFG_JOB_INFORMATION("TB_CFG_JOB_INFORMATION",
			new String[][]{
			new String[]{"id","integer","","not null generated always as identity (start with 1, increment by 1)"},
			new String[]{"database_type","varchar","50","NOT NULL"},
			new String[]{"url","varchar","500","NOT NULL"},
			new String[]{"username","varchar","50","NOT NULL"},
			new String[]{"password","varchar","50","NOT NULL"},
			new String[]{"jobname","varchar","100","NOT NULL"},
			new String[]{"order_content","varchar","4000","NOT NULL"},
			new String[]{"plan_frequency","varchar","50","NOT NULL"},
			new String[]{"daily_time","varchar","50","NOT NULL"},
			new String[]{"day","varchar","25","NOT NULL"},
			new String[]{"declaration","varchar","4000",""},
			new String[]{"status","integer","","NOT NULL"},
			new String[]{"states","varchar","50","NOT NULL"},
			new String[]{"buildtime","varchar","100",""}
	});
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 列，每一行是一列的元数据，包括 columnName,columnType,length,limit
	 */
	private String[][] columns;
	
	
	public String getTableName() {
		return tableName;
	}

	public String[][] getColumns() {
		return columns;
	}

	private EmbeddDBTable(String tableName, String[][] columns){
		this.tableName = tableName;
		this.columns = columns;
	}
	
	public static String getCreateSQL(EmbeddDBTable embeddDBTable){
		StringBuilder sb = new StringBuilder("CREATE TABLE " + embeddDBTable.getTableName() + "(");
		
		for(String[] column : embeddDBTable.columns){
			sb.append(column[0]).append(" ").append(column[1]);
			if(!"".equals(column[2])){
				sb.append("(").append(column[2]).append(")");
			}
			sb.append(" ").append(column[3]).append(",");
		}
		
		sb.delete(sb.length()-1, sb.length());
		sb.append(")");
		return sb.toString();
	}

}
