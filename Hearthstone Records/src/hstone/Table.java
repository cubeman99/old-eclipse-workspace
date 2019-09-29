package hstone;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import cmg.graphics.Draw;

public class Table {
	private Cell[][] cells;
	private int[] rowSpans;
	private int[] colSpans;
	private int rowCapacity;
	private int numRows;
	private int numCols;

	
	
	// ================== CONSTRUCTORS ================== //

	public Table(int numColumns) {
		rowCapacity = 10;
		numCols     = numColumns;
		numRows     = 0;
		cells       = new Cell[rowCapacity][numCols];
		rowSpans    = new int[rowCapacity];
		colSpans    = new int[numCols];
		
		for (int i = 0; i < numCols; i++)
			colSpans[i] = 80;
	}

	
	
	// =================== ACCESSORS =================== //
	
	public int numRows() {
		return numRows;
	}
	
	public int numColumns() {
		return numCols;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setColumnSpans(int... spans) {
		colSpans = spans;
	}
	
	public void addRow(Cell... row) {
		if (numRows + 1 > rowCapacity) {
			rowCapacity += 10;
			int[] newRowSpans = new int[rowCapacity];
			Cell[][] newCells = new Cell[rowCapacity][numCols];
			
			for (int r = 0; r < numRows; r++) {
				newRowSpans[r] = rowSpans[r];
				for (int c = 0; c < numCols; c++) {
					Cell temp = cells[r][c];
					newCells[r][c] = temp; 
				}
			}
			rowSpans = newRowSpans;
			cells    = newCells;
		}
		rowSpans[numRows] = 20;
		cells[numRows]    = row;
		numRows++;
	}
	
	public void update() {
		
	}
	
	public void draw(int dx, int dy) {
		int y = 0;
		
		
		for (int r = 0; r < numRows; r++) {
			int x = 0;
			int yspan = rowSpans[r];
			
			for (int c = 0; c < numCols; c++) {
				Cell cell = cells[r][c];
				int xspan = colSpans[c];
				
				Draw.setColor(cell.getBackgroundColor());
				Draw.fillRect(dx + x, dy + y, xspan, yspan);
				
				Draw.setColor(Color.BLACK);
				Draw.drawRect(dx + x, dy + y, xspan, yspan);
				
				Draw.getGraphics().setFont(new Font("default", cell.isBold() ? Font.BOLD : Font.PLAIN, 13));
				Draw.setColor(cell.getTextColor());
				Draw.drawString(cell.getText(), dx + x + (xspan / 2), dy + y + (yspan / 2), Draw.CENTER, Draw.MIDDLE);
				
				x += xspan;
			}
			y += yspan;
		}
	}
}
