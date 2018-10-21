package com.dugonggames.shooter.shooter;

import com.dugonggames.shooter.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntitySetTest {

    @Test
    public void test_map() {
        EntitySet<Integer> s = EntitySet.of(1,2,3,4);
        EntitySet<Integer> actual = s.map(i -> i*2);
        assertEquals(EntitySet.of(2,4,6,8), actual);
    }

    @Test
    public void test_filterMap() {
        EntitySet<Integer> s = EntitySet.of(8,2,3,8,4,5,7,2);
        EntitySet<Integer> actual = s.filterMap(i -> i % 2 == 0 ? Optional.of(i/2) : Optional.empty());
        assertEquals(EntitySet.of(4,1,4,2,1), actual);
    }

    @Test
    public void test_mapCross_identity() {
        EntitySet<Integer> s1 = EntitySet.of(4,5,6);
        EntitySet<Integer> s2 = EntitySet.of(7,8,9,10);

        Pair<EntitySet<Integer>, EntitySet<Integer>> actual = s1.mapCross(s2, (a,b) -> new Pair<>(Optional.of(a), Optional.of(b)));
        assertEquals(EntitySet.of(4,5,6), actual.getA());
        assertEquals(EntitySet.of(7,8,9,10), actual.getB());
    }

    @Test
    public void test_mapCross_empty() {
        EntitySet<Integer> s1 = EntitySet.of(4,5,6);
        EntitySet<Integer> s2 = EntitySet.of(7,8,9,10,11);

        Pair<EntitySet<Integer>, EntitySet<Integer>> actual = s1.mapCross(s2, (a,b) -> new Pair<>(Optional.empty(), Optional.empty()));
        assertEquals(EntitySet.of(), actual.getA());
        assertEquals(EntitySet.of(10,11), actual.getB());
    }

    @Test
    public void test_mapCross_decrement() {
        EntitySet<Integer> s1 = EntitySet.of(4,5,6);
        EntitySet<Integer> s2 = EntitySet.of(7,8,9,10);

        Pair<EntitySet<Integer>, EntitySet<Integer>> actual = s1.mapCross(s2, (a,b) -> new Pair<>(Optional.of(a-1), Optional.of(b-1)));
        assertEquals(EntitySet.of(0,1,2), actual.getA());
        assertEquals(EntitySet.of(4,5,6,7), actual.getB());
    }
}
