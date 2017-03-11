package com.liveproject.ycce.iimp;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Laptop on 03-03-2017.
 */
public class LocalIdGen {
    private SecureRandom random = new SecureRandom();

    public String nextLocalId() {
        return new BigInteger(130, random).toString(32);
    }

}
