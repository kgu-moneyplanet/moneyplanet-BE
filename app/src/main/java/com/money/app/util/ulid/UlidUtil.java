package com.money.app.util.ulid;

import de.huxhorn.sulky.ulid.ULID;
public class UlidUtil {
    private static final ULID ulid = new ULID();

    public static String generate() {
        return ulid.nextULID();  // ex: "01HZCW3BRQ6D53AT4Z4RGKS1NB"
    }
}

