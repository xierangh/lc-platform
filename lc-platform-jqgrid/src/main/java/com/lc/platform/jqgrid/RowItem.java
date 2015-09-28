package com.lc.platform.jqgrid;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RowItem {
	private int id;
	private Object[] cell;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	public RowItem(int id, Object[] cell) {
		super();
		this.id = id;
		for (int i = 0; i < cell.length; i++) {
			if(cell[i] instanceof Timestamp){
				cell[i] = formatter.format(cell[i]);
			}
		}
		this.cell = cell;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object[] getCell() {
		return cell;
	}

	public void setCell(Object[] cell) {
		this.cell = cell;
	}

}
