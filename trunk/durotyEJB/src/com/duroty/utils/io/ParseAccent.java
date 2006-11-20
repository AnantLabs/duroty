/*
 * StripAccent.java
 *
 * Created on 28 de abril de 2004, 11:40
 */
package com.duroty.utils.io;


/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class ParseAccent {
    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String start(String text) {
        text = text.replaceAll("�", "a");
        text = text.replaceAll("�", "a");
        text = text.replaceAll("�", "a");
        text = text.replaceAll("�", "a");

        text = text.replaceAll("�", "A");
        text = text.replaceAll("�", "A");
        text = text.replaceAll("�", "A");
        text = text.replaceAll("�", "A");

        text = text.replaceAll("�", "e");
        text = text.replaceAll("�", "e");
        text = text.replaceAll("�", "e");
        text = text.replaceAll("�", "e");

        text = text.replaceAll("�", "E");
        text = text.replaceAll("�", "E");
        text = text.replaceAll("�", "E");
        text = text.replaceAll("�", "E");

        text = text.replaceAll("�", "i");
        text = text.replaceAll("�", "i");
        text = text.replaceAll("�", "i");
        text = text.replaceAll("�", "i");

        text = text.replaceAll("�", "I");
        text = text.replaceAll("�", "I");
        text = text.replaceAll("�", "I");
        text = text.replaceAll("�", "I");

        text = text.replaceAll("�", "o");
        text = text.replaceAll("�", "o");
        text = text.replaceAll("�", "o");
        text = text.replaceAll("�", "o");

        text = text.replaceAll("�", "O");
        text = text.replaceAll("�", "O");
        text = text.replaceAll("�", "O");
        text = text.replaceAll("�", "O");

        text = text.replaceAll("�", "u");
        text = text.replaceAll("�", "u");
        text = text.replaceAll("�", "u");
        text = text.replaceAll("�", "u");

        text = text.replaceAll("�", "U");
        text = text.replaceAll("�", "U");
        text = text.replaceAll("�", "U");
        text = text.replaceAll("�", "U");

        return text;
    }
}
