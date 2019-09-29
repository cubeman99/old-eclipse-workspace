package cmg.ui;

import cmg.graphics.Draw;

public class Label extends Component {
	private String text;
	
	
	public Label(String text) {
		this.text = text;
	}
	
	@Override
	public void render() {
		Draw.drawString(text, rect.getX1(), rect.getY1(), Draw.LEFT, Draw.TOP);
	}
}
