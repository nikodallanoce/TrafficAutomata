package pkg;

public interface Road{
    public int hashCode();
    public Request handleRequest(Request request);
    public int getXStartingPoint();
    public int getYStartingPoint();
    public void setOccupiedCells(int x, int y);

    public Road getNextRoad();
}
