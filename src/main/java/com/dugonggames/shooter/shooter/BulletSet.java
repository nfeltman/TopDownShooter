package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.MovingPoint;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BulletSet {
    private ArrayList<MovingPoint> bullets;

    public BulletSet(){
        bullets = new ArrayList<MovingPoint>();
    }

    public void add(MovingPoint bullet){
        bullets.add(bullet);
    }

    public BulletSet filter(Predicate<MovingPoint> predicate){
        BulletSet filteredSet = new BulletSet();
        for (MovingPoint bullet : bullets){
            if(predicate.test(bullet)) filteredSet.bullets.add(bullet);
        }
        return filteredSet;
    }

    public BulletSet map(Function<MovingPoint, MovingPoint> function){
        BulletSet mappedSet = new BulletSet();
        for (MovingPoint bullet : bullets){
            mappedSet.bullets.add(function.apply(bullet));
        }
        return mappedSet;
    }

    public BulletSet filtermap(Function<MovingPoint, Optional<MovingPoint>> function){
        BulletSet filtermappedSet = new BulletSet();
        for (MovingPoint bullet : bullets){
            Optional<MovingPoint> filteredBullet = function.apply(bullet);
            if (filteredBullet.isPresent()) filtermappedSet.bullets.add(filteredBullet.get());
        }
        return filtermappedSet;
    }

    public boolean any(Predicate<MovingPoint> predicate){
        for (MovingPoint bullet : bullets){
            if (predicate.test(bullet)) return true;
        }
        return false;
    }

    public void applyAll(Consumer<MovingPoint> consumer){
        for (MovingPoint bullet : bullets) consumer.accept(bullet);
    }
}
