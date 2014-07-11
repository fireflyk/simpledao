package com.codinghero.sql;

import com.codinghero.sql.annotation.Ignore;
import com.codinghero.sql.annotation.Table;

@Table(name="easydaouser")
public class User {
	private Long id;

	private String name;
	
	@Ignore
	private String forDisplay;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getForDisplay() {
		return forDisplay;
	}

	public void setForDisplay(String forDisplay) {
		this.forDisplay = forDisplay;
	}
}
