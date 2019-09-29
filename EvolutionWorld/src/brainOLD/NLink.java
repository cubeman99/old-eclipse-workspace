package brainOLD;


/**
 * A class to represent a link between neurons
 */
public class NLink {
	public double weight;
	public Neuron owner;
	public Neuron link;
	
	public NLink(Neuron owner, Neuron link, double weight) {
		this.owner  = owner;
		this.link   = link;
		this.weight = weight;
	}
	
	
}
