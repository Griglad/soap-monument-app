package com.soap.utilities;

import com.soap.jpa.DbMuseum;
import com.soap.model.Museum;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Grigorios Ladas
 */
public final class MuseumUtil {

    private final double startingLatitude = 35.01186;
    private final double endingLongitude = 28.2225;
    private final Pattern inputPattern = Pattern.compile("^\\D{4,80}+(\\s\\D{4,80}+)*$");
    private static volatile MuseumUtil INSTANCE = null;


    private MuseumUtil() {

    }

    private static final Object lock = new Object();

    public static MuseumUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new MuseumUtil();
                }
            }

        }
        return INSTANCE;

    }


    public Museum fromdbToMuseum(DbMuseum dbMuseum) {
        Museum museum = new Museum();
        museum.setName(dbMuseum.getName());
        museum.setLatitude(dbMuseum.getPoint().getX());
        museum.setLongitude(dbMuseum.getPoint().getY());
        museum.setPlace(dbMuseum.getPlace());
        museum.setRegion(dbMuseum.getRegion());
        museum.setDescription(dbMuseum.getDescription());
        return museum;
    }

    public DbMuseum fromMuseumToDb(Museum museum) {
        DbMuseum dbmuseum = new DbMuseum();
        dbmuseum.setName(museum.getName());
        dbmuseum.setRegion(museum.getRegion());
        dbmuseum.setPlace(museum.getPlace());
        dbmuseum.setDescription(museum.getDescription());
        dbmuseum.setPoint(GeometryHelper.createPoint(museum.getLatitude(), museum.getLongitude()));
        return dbmuseum;
    }

    public DbMuseum retrieveMinDistanceMuseum(Map<DbMuseum, Double> museumDoubleMap) {

        DbMuseum museum = Collections.min(museumDoubleMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        return museum;

    }

    public void updateCounter(DbMuseum dbMuseum) {
        long counter = dbMuseum.getCounter();
        dbMuseum.setCounter(++counter);
    }

    public boolean isValidPattern(String... params) {

        return Arrays.stream(params).allMatch(e -> inputPattern.matcher(e).matches());
    }

    public boolean isValidCoordinates(double latitude, double longitude) {
        return Double.compare(latitude, startingLatitude) >= 0 && Double.compare(longitude, endingLongitude) <= 0;

    }


}
