package com.cmeza.sdgenerator.util;

/**
 * Created by carlos on 23/04/17.
 */
public class Tuple<X extends Comparable<? super X>, Y extends Comparable<? super Y>> implements Comparable<Tuple<X, Y>>{
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X left(){
        return this.x;
    }

    public Y right(){
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Tuple<X, Y> o) {
        int d = this.x.compareTo(o.x);
        if (d == 0)
            return this.y.compareTo(o.y);
        return d;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}