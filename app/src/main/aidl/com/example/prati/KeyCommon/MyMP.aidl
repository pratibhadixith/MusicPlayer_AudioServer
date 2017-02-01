// MyMP.aidl
package com.example.prati.KeyCommon;

// Declare any non-default types here with import statements

interface MyMP {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   // void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
     //       double aDouble, String aString);
      void playclip(int clipNumber);
           void resume_play_clip(int clipNumber);
           void stopclip(int clipNumber);
           void pauseclip(int clipNumber);
}
