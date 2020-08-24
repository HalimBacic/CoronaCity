package interfaces;

public interface MovementOperations {
	
	public void relaiseRow(Integer x,Integer y);

	public void relaiseColumn(Integer x,Integer y);
	
    public boolean checkDist(Integer x, Integer y);
    
    public boolean checkField(Integer x, Integer y);
    
    public boolean checkOposite(Integer side);
    
    public void moveUp() throws InterruptedException;
    
    public void moveDown() throws InterruptedException;
    
    public void moveLeft() throws InterruptedException;
    
    public void moveRight() throws InterruptedException;
}
