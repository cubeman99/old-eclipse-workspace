package zelda.common.properties;

public interface PropertyHolder {
	public Properties getProperties();
	public void onChangeProperty(Property p);
}
