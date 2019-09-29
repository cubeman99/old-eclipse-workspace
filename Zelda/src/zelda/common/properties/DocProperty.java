package zelda.common.properties;

public class DocProperty extends Property {
	private static final long serialVersionUID = 1L;
	private String description;
	
	public DocProperty(String name, Object defaultValue, String description) {
		super(name, defaultValue);
		this.description = description;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
}
