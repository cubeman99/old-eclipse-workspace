package zelda.editor.tileSheet;

import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.editor.Editor;
import zelda.editor.tileSheet.templates.TemplateDungeon;
import zelda.editor.tileSheet.templates.TemplateInterior;
import zelda.editor.tileSheet.templates.TemplateOverworld;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.game.zone.Zone;


public class Tileset extends AbstractSet {
	private TilesetTemplate[] templates;
	private TilesetTemplate template;
	private int templateIndex;



	// ================== CONSTRUCTORS ================== //

	public Tileset(Editor editor) {
		super(editor, "Tiles");

		templates = new TilesetTemplate[] {
			new TemplateOverworld(),
			new TemplateInterior(),
			new TemplateDungeon()
		};

		for (int i = 0; i < templates.length; i++)
			templates[i].setIndex(i);

		templateIndex = 0;
		template = templates[0];
	}



	// =================== ACCESSORS =================== //



	// ==================== MUTATORS ==================== //

	public void setZone(Zone zone) {
		if (zone.getDefaultTemplate() != null)
			template = zone.getDefaultTemplate();
	}

	public GridTile newDefaultTile(Frame frame, Point loc) {
		return template.createTile(frame, template.getDefaultTileSourcePos(),
				loc);
	}

	public GridTile newTile(Frame frame, Point loc) {
		return template.createTile(frame, cursor, loc);
	}

	public GridTile newTile(Frame frame, Point sourcePos, Point loc) {
		return template.createTile(frame, sourcePos, loc);
	}

	public GridTile newTile(int templateIndex, Frame frame, Point sourcePos,
			Point loc) {
		return templates[templateIndex].createTile(frame, sourcePos, loc);
	}

	public void changeTileset() {
		templateIndex = (templateIndex + 1) % templates.length;
		template = templates[templateIndex];
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void createTile(Frame frame, Point pos) {
		Point loc = pos.dividedBy(Settings.TS);
		frame.setGridTile(newTile(frame, loc));
	}

	@Override
	public void eraseTile(Frame frame, Point pos) {
		Point loc = pos.dividedBy(Settings.TS);
		frame.setGridTile(newDefaultTile(frame, loc));
	}

	@Override
	protected Point getSize() {
		return template.getSize();
	}

	@Override
	public void drawSet() {
		Draw.drawImage(
				template.zoneSheet.getImageSheet(
						editor.getWorld().getCurrentFrame()).getBuffer(), 0, 0);
	}
}
