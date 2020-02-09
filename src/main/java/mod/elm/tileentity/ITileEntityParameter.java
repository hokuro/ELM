package mod.elm.tileentity;

public interface ITileEntityParameter {
	public int getField(int id);
	public void setField(int id, int value);
	public int getFieldCount();
}
