package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;

import java.util.ArrayList;
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
        ArrayList<T> newElements = (ArrayList<T>) elements.clone();
        ArrayList<U> otherElements = (ArrayList<U>) other.elements.clone();

        outer:
        for (int i = 0; i < newElements.size(); i++){
            for (int j = 0; j < otherElements.size() && i < newElements.size(); j++){
                Pair<Optional<T>, Optional<U>> pair = function.apply(elements.get(i), other.elements.get(j));
                if (pair.getA().isPresent()) newElements.set(i, pair.getA().get());
                else{
                    newElements.remove(i);
                    i--;
                }
                if (pair.getB().isPresent()) otherElements.set(j, pair.getB().get());
                else{
                    otherElements.remove(j);
                    j--;
                }
            }
        }
        return new Pair<>(new EntitySet<>(newElements), new EntitySet<>(otherElements));
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
