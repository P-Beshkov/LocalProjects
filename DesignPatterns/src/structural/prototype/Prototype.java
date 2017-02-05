package structural.prototype;

public interface Prototype extends Cloneable {
	
	public AccessControl clone() throws CloneNotSupportedException;

}