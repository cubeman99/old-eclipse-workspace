package zelda.common.properties;

public class EventProperty extends Property {
	private static final long serialVersionUID = 1L;
	private String description;

	public EventProperty(String name, String description) {
		this(name, "", description);
	}
	
	public EventProperty(String name, String script, String description) {
		super(name, script);
		this.description = description;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
}
