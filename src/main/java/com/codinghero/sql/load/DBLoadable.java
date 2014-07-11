package com.codinghero.sql.load;

/**
 * list to file, ready to be loaded to DB<br/>
 * 
 * @author liutong628@gmail.com
 */
public interface DBLoadable {

	/**
	 * FIELDS TERMINATED
	 */
	public static final String FIELDS_TERMINATED = "\t";
	
	/**
	 * LINES TERMINATED
	 */
	public static final String LINES_TERMINATED = "\n";

	public String toDBLineString();

}
