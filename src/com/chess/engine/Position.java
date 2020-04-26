package com.chess.engine;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class Position {

    private final int xCoordinate, yCoordinate;
    private static int counter;
    private static int constructorKey = 0;
    private static List<Position> positions = new ArrayList<>();
    public static final Map<String, Position> POSITION_CACHE = createAllPossiblePositions();
    private final String cachedToString, cachedKey;
    private static char[] letters = ("abcdefgh").toCharArray(); //use it for algebraic notations

    private Position(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        cachedToString = String.format("Position at row %d column %d", xCoordinate, yCoordinate);
        cachedKey = String.format("%d%d", xCoordinate, yCoordinate);
    }

    private static Map<String, Position> createAllPossiblePositions() {
        if (counter == 0) {
            Map<String, Position> positions = new HashMap<>();
            for (int i = -2; i < 8; i++) {
                for (int j = -2; j < 8; j++) {
                    Position e = new Position(i, j);
                    constructorKey++;
                    positions.put(e.getKey(), e);
                    constructorKey--;
                }
            }
            counter++;
            return ImmutableMap.copyOf(positions);
        }
        throw new RuntimeException("The method createAllPossiblePositions can be called only once");
    }

    public static List<Position> getPositions(String... strings) {
        for (String i : strings) {
            if (POSITION_CACHE.containsKey(i)) positions.add(POSITION_CACHE.get(i));
            else throw new IllegalArgumentException("The position you gave has no match with the existing positions");
        }
        return ImmutableList.copyOf(positions);
    }

    @Override
    public String toString() {
        return cachedToString;
    }

    public String getKey() {
        return cachedKey;
    }

    public final int getxCoordinate() {
        return xCoordinate;
    }

    public final int getyCoordinate() {
        return yCoordinate;
    }
}