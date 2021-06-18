package net.za.dawes.wintexprotocol.wintexanalysis;

import java.util.Date;

import net.za.dawes.wintexprotocol.WintexMessage;

class WintexRecord {
    WintexMessage request = null;
    WintexMessage response = null;
    Date date = new Date();
    String comment = null;
    long count = 1;

    public String toString() {
        return date + " >> " + request + " !! << " + response + " !! " + (comment == null ? "" : comment);
    }
}