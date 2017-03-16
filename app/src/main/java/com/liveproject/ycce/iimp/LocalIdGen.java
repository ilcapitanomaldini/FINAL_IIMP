package com.liveproject.ycce.iimp;

/*COPYRIGHT NOTICE

Copyright By "YCCE TEAM" on 15-03-2017.

Members of "YCCE TEAM" are stated in the postscript.

We, the creators of this software (i.e. developers) referenced as "YCCE TEAM" or "we" from here on,
allow the person who gets this software and/or code for the software to present this software/code
in a presentation dated 16-03-2017 only. Any further usage would be deemed to be breach of contract.
 Accepting this software/code is legally binding and would mean that the terms stated here have been
  accepted. The person does not have the right to copy/modify/distribute or in any form make the
  software or code available to anyone without the explicit permission of all the members of
   "YCCE TEAM". It is the responsibility of the aforementioned person that this software/code
   does not get illegally distributed till the time the person is in possession of the software/code.

P.S. :
Members of "YCCE TEAM" :
1. Aakash Wankhede
2. Akash Kahalkar
3. Mayur Dongare
4. Ved Mehta*/

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
