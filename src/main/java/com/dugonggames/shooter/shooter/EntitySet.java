package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntitySet<T> {
    private ArrayList<T> elements;

    public EntitySet(){
        elements = new ArrayList<T>();
    }

    public void add(T t){
        elements.add(t);
    }

    public EntitySet filter(Predicate<T> predicate){
        EntitySet<T> filteredSet = new EntitySet<T>();
        for (T bullet : elements){
            if(predicate.test(bullet)) filteredSet.elements.add(bullet);
        }
        return filteredSet;
    }

    public <U> EntitySet<U> map(Function<T, U> function){
        EntitySet<U> mappedSet = new EntitySet<U>();
        for (T bullet : elements){
            mappedSet.elements.add(function.apply(bullet));
        }
        return mappedSet;
    }

    public <U> EntitySet<U> filtermap(Function<T, Optional<U>> function){
        EntitySet<U> filtermappedSet = new EntitySet<U>();
        for (T bullet : elements){
            Optional<U> filteredBullet = function.apply(bullet);
            filteredBullet.ifPresent(u -> filtermappedSet.elements.add(u));
        }
        return filtermappedSet;
    }

    public <U> Pair<EntitySet<T>, EntitySet<U>> mapCross(BiFunction<T, U, Pair<Optional<T>, Optional<U>>> function){
        throw new RuntimeException("Not implemented yet!");
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
