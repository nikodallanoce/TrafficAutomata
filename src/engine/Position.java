package engine;

public class Position {

    public int[] getIndexes() {
        return indexes;
    }

    public int getLane(){
        return indexes[0];
    }

    public int getCell(){
        return indexes[1];
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    private int[] indexes;

    public Position(int lane, int cell){
        this.indexes = new int[]{lane, cell};
    }

}
