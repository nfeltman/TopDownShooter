package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntitySet<T> implements Iterable<T>{
    private ArrayList<T> elements;

    public EntitySet(){
        elements = new ArrayList<T>();
    }

    private EntitySet(ArrayList<T> elements){ this.elements = elements; }

    public static <T> EntitySet<T> of(T... ts) {
        final ArrayList<T> list = new ArrayList<T>(Arrays.asList(ts));
        return new EntitySet<>(list);
    }

    public void add(T t){
        elements.add(t);
    }

    public int size(){
        return elements.size();
    }

    public EntitySet<T> filter(Predicate<T> predicate){
        EntitySet<T> filteredSet = new EntitySet<T>();
        for (T bullet : elements){
            if(predicate.test(bullet)) filteredSet.elements.add(bullet);
        }
        return filteredSet;
    }

    public Pair<EntitySet<T>, EntitySet<T>> split(Predicate<T> predicate){
        EntitySet<T> filteredSet = new EntitySet<T>();
        EntitySet<T> splitSet = new EntitySet<T>();
        for (T element : elements){
            if (predicate.test(element)){
                filteredSet.elements.add(element);
            } else {
                splitSet.elements.add(element);
            }
        }
        return new Pair(filteredSet, splitSet);
    }

    public <U> EntitySet<U> map(Function<T, U> function){
        EntitySet<U> mappedSet = new EntitySet<U>();
        for (T bullet : elements){
            mappedSet.elements.add(function.apply(bullet));
        }
        return mappedSet;
    }

    public <U> EntitySet<U> filterMap(Function<T, Optional<U>> function){
        EntitySet<U> filtermappedSet = new EntitySet<U>();
        for (T bullet : elements){
            Optional<U> filteredBullet = function.apply(bullet);
            filteredBullet.ifPresent(u -> filtermappedSet.elements.add(u));
        }
        return filtermappedSet;
    }

    public <U> Pair<EntitySet<T>, EntitySet<U>> mapCross(EntitySet<U> other, BiFunction<T, U, Pair<Optional<T>, Optional<U>>> function){
        ArrayList<U> otherElements = (ArrayList<U>) other.elements.clone();

        EntitySet<T> newSet = filterMap(element -> {
            T newElement = element;
            for (int j = 0; j < otherElements.size(); j++){
                Pair<Optional<T>, Optional<U>> pair = function.apply(newElement, otherElements.get(j));
                if (pair.getB().isPresent()) otherElements.set(j, pair.getB().get());
                else {
                    otherElements.remove(j);
                    j--;
                }
                if (pair.getA().isPresent()) newElement = pair.getA().get();
                else return Optional.empty();
            }
            return Optional.of(newElement);
        });
        return new Pair<>(newSet, new EntitySet<>(otherElements));
    }

    public boolean any(Predicate<T> predicate){
        for (T bullet : elements){
            if (predicate.test(bullet)) return true;
        }
        return false;
    }

    public void applyAll(Consumer<T> consumer){
        for (T bullet : elements) consumer.accept(bullet);
    }

    @Override
    public Iterator<T> iterator() {
       return elements.iterator();
    }

    public String toString() {
        return elements.toString();
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EntitySet)) return false;
        final EntitySet<?> other = (EntitySet<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$elements = this.elements;
        final Object other$elements = other.elements;
        if (this$elements == null ? other$elements != null : !this$elements.equals(other$elements)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EntitySet;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $elements = this.elements;
        result = result * PRIME + ($elements == null ? 43 : $elements.hashCode());
        return result;
    }

    /*

    Implement if necessary.

    public void mapInPlace(Function<T, T> function){
        throw new RuntimeException("Not implemented yet!");
    }

    public void filterMapInPlace(Function<T, Optional<T>> function){
        throw new RuntimeException("Not implemented yet!");
    }

    */
}
