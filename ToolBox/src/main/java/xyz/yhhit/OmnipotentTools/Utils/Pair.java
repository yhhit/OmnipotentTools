package xyz.yhhit.OmnipotentTools.Utils;

public class Pair<L,R> {
    L left;
    R Right;

    public Pair(L left, R right) {
        this.left = left;
        Right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return Right;
    }

    public void setRight(R right) {
        Right = right;
    }
}
