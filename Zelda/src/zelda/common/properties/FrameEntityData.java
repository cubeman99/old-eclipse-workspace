package zelda.common.properties;

import java.util.ArrayList;
import zelda.game.control.script.Function;
import zelda.game.world.Frame;

public class FrameEntityData implements PropertyHolder {
	private Properties properties;
	private Functions functions;
	//private ScriptEvents events;
	private PropertyHolder source;
	
	

	// ================== CONSTRUCTORS ================== //

	public FrameEntityData(PropertyHolder source) {
		this(source, new Properties());
	}
	
	public FrameEntityData(PropertyHolder source, Properties p) {
		this.source = source;
		properties  = p;
		functions   = new Functions();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Functions getFunctions() {
		return functions;
	}
	
	public PropertyHolder getSource() {
		if (source == null)
			return this;
		return source;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setSource(PropertyHolder source) {
		this.source = source;
	}
	
	public void addFunction(Function func) {
		functions.addFunction(func);
	}
	
	public void addProperty(String name) {
		properties.set(new Property(name, ""));
	}
	
	public void addProperty(String name, Object value) {
		properties.set(new Property(name, value));
	}
	
	public void addProperty(String name, Object value, String description) {
		properties.set(new DocProperty(name, value, description));
	}
	
	public void addEvent(String name, String script, String description) {
		properties.set(new EventProperty(name, script, description));
	}
	
	public void addEvent(String name, String description) {
		properties.set(new EventProperty(name, description));
	}
	
	public String callFunction(String funcName, ArrayList<String> args, PropertyHolder holder, Frame frame) {
		return functions.callFunction(funcName, args, holder, frame);
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public Properties getProperties() {
		return properties;
	}
	
	@Override
	public void onChangeProperty(Property p) {
		
	}
}
