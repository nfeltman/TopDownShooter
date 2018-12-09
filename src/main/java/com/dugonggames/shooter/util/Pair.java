package com.dugonggames.shooter.util;

public class Pair<A, B> {
    private A a;
    private B b;

    @java.beans.ConstructorProperties({"a", "b"})
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return this.a;
    }

    public B getB() {
        return this.b;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Pair)) return false;
        final Pair<?, ?> other = (Pair<?, ?>) o;
        final Object this$a = this.getA();
        final Object other$a = other.getA();
        if (this$a == null ? other$a != null : !this$a.equals(other$a)) return false;
        final Object this$b = this.getB();
        final Object other$b = other.getB();
        if (this$b == null ? other$b != null : !this$b.equals(other$b)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $a = this.getA();
        result = result * PRIME + ($a == null ? 43 : $a.hashCode());
        final Object $b = this.getB();
        result = result * PRIME + ($b == null ? 43 : $b.hashCode());
        return result;
    }

    public String toString() {
        return "Pair(a=" + this.getA() + ", b=" + this.getB() + ")";
    }
}
