package engine;

public class Position {
    private final int[] indexes;

    public Position(int lane, int cell){
        this.indexes = new int[]{lane, cell};
    }

    public int lane(){
        return indexes[0];
    }

    public int laneCell(){
        return indexes[1];
    }
}
