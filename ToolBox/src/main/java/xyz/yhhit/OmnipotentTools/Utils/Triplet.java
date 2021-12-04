package xyz.yhhit.OmnipotentTools.Utils;

public class Triplet<L,M,R> {
    L left;
    M middle;
    R Right;

    public Triplet(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        Right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public M getMiddle() {
        return middle;
    }

    public void setMiddle(M middle) {
        this.middle = middle;
    }

    public R getRight() {
        return Right;
    }

    public void setRight(R right) {
        Right = right;
    }
}
