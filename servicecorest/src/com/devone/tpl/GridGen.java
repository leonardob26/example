package com.devone.tpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.devone.tpl.Db;
import com.devone.tpl.logger.Errors;

/**
 * @author boet
 *
 */
public class GridGen {

	private byte quantityColumn = 0;
	private String tableGrid;
	private String idTable;
	private TypeField typeIdTable;
	private String titleGrid ;
	private String conditionTableGrid="";
	private String orderGrid;	
	private boolean isDistinct = false;

	private String ServletsSource;
	public String query = ""; 
	public StringBuilder filterGrid = new StringBuilder();
	public int offSet=0;
	public byte elementPerPage=0;
	public int maxOffSet = 0;
	public String fieldCount = "";
	//public String aliasTableFilter;
//	private byte IteratorIdsControls = 0;
	
	private ArrayList<ColumnGrid> columnsGrid = new ArrayList<ColumnGrid>();
	
	public enum TypeData {TEXT, DATE, CHECK };
	public enum TypeField {INTEGER, TEXT, DATE, BOOLEAN, NUMERIC, OTHER};
	public enum TypeAction {loadData, loadOffSet, loadEdit, updData, delData, loadDataOfCombo};
	public enum TypeBoton {First, Previous, Next, Last, page};
	public Db db;
	
	public class ColumnGrid{
		String nameField, headerColumn, widthColumn;
		TypeData typeColumn;
	}
    
	/**
	 * Add a new column to the Grid
	 * @param nameField Name of Field
	 * @param headerColumn Header of column
	 * @param widthColumn Witdth of column (px or %) ex: 50px o 10%
	 * @param typeColumn Type of Column
	 */
    public void addColumnGrid(String nameField, String headerColumn, String widthColumn, TypeData typeColumn){
    	ColumnGrid col = new ColumnGrid();
    	col.nameField = nameField; col.headerColumn = headerColumn; col.widthColumn=widthColumn; col.typeColumn = typeColumn;
    	//columnsGrid.add(new String[]{nameField, headerColumn, widthColumn, typeColumn});
    	columnsGrid.add(col);
    	quantityColumn++;     	
    }
    public void addColumnGrid(String nameField, String headerColumn, String widthColumn){
    	addColumnGrid(nameField, headerColumn, widthColumn, TypeData.TEXT);
    }

/*    private StringBuilder createBody() {    	
    	StringBuilder cad = new StringBuilder();
    	cad.append(createTableBody());    	  	 
		return cad;
	}*/
	public String createDataTable() {
		StringBuilder cad = new StringBuilder();
		cad.append("<table class='table table-striped table-bordered'>");		
    	for (ColumnGrid col : columnsGrid) 
			cad.append("<th width='" + col.widthColumn+ "'>" + col.headerColumn + "</th>");
		cad.append("</tr>");		
		cad.append(createTableBody() + "</table>");
		return cad.toString();
	}

	public String createFooter() {
		final int countTagPag = maxOffSet<=6 ? maxOffSet : 6;  
		StringBuilder cad = new StringBuilder("<ul class='pagination'>");
		cad.append((offSet==0) ? "<li class='disabled'><span>&laquo;</span></li>" : "<li><a id='hrePrevious' href='#'>&laquo;</a></li>");
		
		for (byte i = 1; i <= countTagPag; i++) 
			cad.append((offSet+1)!=i ? 
					"<li><a id=page" + i + " href='#'>" + i + "</a></li>" : 
				    "<li class='active'><span>" + i + "<span class='sr-only'>(current)</span></span></li>");
		cad.append("<li class='disabled'><span> ... </span></li>");		
		cad.append((offSet!=maxOffSet)?"<li><a id='hreNext' href='#'>&raquo;</a></li>": "<li class='disabled'><span>&raquo;</span></li>");
		cad.append("</ul>");
		cad.append("<span class='badge pull-right'>" + Integer.toString(offSet+1) + " / " + Integer.toString(maxOffSet +1) +  "</span>");
		return cad.toString();
	}
	private StringBuilder createTableBody() {
    	StringBuilder cad = new StringBuilder();
   	  	//boolean alt = false;
   	  	String select = this.isDistinct?"distinct ":"";
   	  	for (ColumnGrid col : columnsGrid) 
			select += col.nameField + ",";
   	  	int itemName = 1;
 	  	if (select!="" && !columnsGrid.contains(idTable)){
   	  		select = select.substring(0, select.length()-1) ;
  			select = idTable + "," + select;
  			itemName =2;
   	  	}   	
   	  	if (orderGrid==null)
   	  		orderGrid = "";
   	  	String condition;
   	  	if (conditionTableGrid.isEmpty())
   	  		condition = filterGrid.toString();
   	  	else {
   	  		if (filterGrid.length()>0)
   	  			condition = conditionTableGrid + " and " + filterGrid;
   	  		else
   	  			condition = conditionTableGrid;
   	  	}
   	  	
		
		try {			
			if (elementPerPage!=0){
				String tmp = fieldCount.isEmpty()?"*": " distinct " + fieldCount;
				maxOffSet = (db.getDataValueInt(tableGrid, "count(" + tmp + ")", condition)/elementPerPage);
				if (offSet>maxOffSet)
					offSet = maxOffSet;
			}
			ResultSet rs = db.getData(tableGrid, select, condition, orderGrid, Integer.toString(elementPerPage), Integer.toString(offSet*elementPerPage));
			if (rs!=null){
				while (rs.next()){
					String nameField, idTable;
					if (this.idTable.contains("."))
						idTable = this.idTable.split("\\.")[1];
					else
						idTable = this.idTable;
					cad.append("<tr><td> <a  href='#' id='elem" + rs.getString(idTable) + "' >" + rs.getString(itemName) + "</a></td>");
					for (int i=1;i<columnsGrid.size();i++) {	  	
						nameField = columnsGrid.get(i).nameField; //
						if (nameField.contains(" as "))
							nameField = nameField.split(" as ")[1];
						else  if (nameField.contains("."))
								nameField = nameField.split("\\.")[1];
					    cad.append("<td>" + formatValueGrid(rs.getString(nameField), columnsGrid.get(i).typeColumn) + "</td>");
					}
					cad.append("</tr>");
				}
			}
			rs.close();
		} catch (SQLException e) {
			cad.setLength(0);
			cad.append(Errors.getError(e));
		} 
	    return cad;
	}

    private String formatValueGrid(String value, TypeData type){ 	
      	switch (type){
    		case DATE:
    			return convertDateTo("ES", value);
    		case CHECK:    		
    			if (value.equals("1") || value.equals("t"))
    				return "<input type='checkbox' id='chk' checked='checked' disabled='disabled' />";
    			else return "<input disabled='disabled' type='checkbox' id='chk'/>";
    		default:
    			return value;
    	}    	    	
    }
    public String convertDateTo(String format, String vDate){
		try
		{									
			String[] cad = vDate.split("[/.-]");
			if (format.equals("ES")){
				if (cad[0].length()==2)
					cad[0] = Integer.toString(Calendar.YEAR).substring(0,2) + cad[0];									
			} else 
				if (cad[2].length()==2)
					cad[2] = Integer.toString(Calendar.YEAR).substring(0,2) + cad[2];					
			return format.equals("ES")?cad[2] + "/" + cad[1] + "/" + cad[0]:cad[2] + cad[1] + cad[0] ;  
		}
		catch (Exception e){
			return Errors.getError(e);
		}				
	}    

	public String loadDataGrid(){
		return createDataTable().toString();		
	}

	public void setQuantityColumn(byte quantityColumn) {
		this.quantityColumn = quantityColumn;
	}

	public byte getQuantityColumn() {
		return quantityColumn;
	}

	public String getConditionTableGrid() {
		return conditionTableGrid;
	}

	public void setConditionTableGrid(String conditionTableGrid) {
		this.conditionTableGrid = conditionTableGrid;
	}
	public ArrayList<ColumnGrid> getColumnsGrid() {
		return columnsGrid;
	}

	public void setColumnsGrid(ArrayList<ColumnGrid> columnsGrid) {
		this.columnsGrid = columnsGrid;
	}
	public String getOrderGrid() {
		return orderGrid;
	}

	public void setOrderGrid(String orderGrid) {
		this.orderGrid = orderGrid;
	}
	public String getTableGrid() {
		return tableGrid;
	}

	public void setTableGrid(String tableGrid) {
		this.tableGrid = tableGrid;
	}

	public String getIdTable() {
		return idTable;
	}

	public void setIdTable(String idTable) {
		this.idTable = idTable;
	}

	public String getTitleGrid() {
		return titleGrid;
	}

	public void setTitleGrid(String titleGrid) {
		this.titleGrid = titleGrid;
	}
	public TypeField getTypeIdTable() {
		return typeIdTable;
	}
	public void setTypeIdTable(TypeField typeIdTable) {
		this.typeIdTable = typeIdTable;
	}
	public void setServletsSource(String servletsSource) {
		ServletsSource = servletsSource;
	}
	public String getServletsSource() {
		return ServletsSource;
	}
	public void setFilterGrid(StringBuilder filterGrid) {		
		this.filterGrid = filterGrid;
	}
	public boolean isDistinct() {
		return isDistinct;
	}
	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}
}
