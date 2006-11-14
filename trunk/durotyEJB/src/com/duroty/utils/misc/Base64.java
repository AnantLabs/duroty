package com.duroty.utils.misc;


/**
 * - moved to the net.matuschek.util tree by Daniel Matuschek - replaced
 * deprecated getBytes() method in method decode - added String encode(String)
 * method to encode a String to  base64
 */
/**
 * Base64 encoder/decoder.  Does not stream, so be careful with using large
 * amounts of data
 *
 * @author Nate Sammons
 * @author Daniel Matuschek
 * @version $Id: Base64.java,v 1.1 2006/03/13 11:15:24 durot Exp $
 */
public class Base64 {
    /**
     * Creates a new Base64 object.
     */
    private Base64() {
        super();
    }

    /**
     * Encode some data and return a String.
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static String encode(byte[] d) {
        if (d == null) {
            return null;
        }

        byte[] data = new byte[d.length + 2];
        System.arraycopy(d, 0, data, 0, d.length);

        byte[] dest = new byte[(data.length / 3) * 4];

        // 3-byte to 4-byte conversion
        for (int sidx = 0, didx = 0; sidx < d.length; sidx += 3, didx += 4) {
            dest[didx] = (byte) ((data[sidx] >>> 2) & 077);
            dest[didx + 1] = (byte) (((data[sidx + 1] >>> 4) & 017) |
                ((data[sidx] << 4) & 077));
            dest[didx + 2] = (byte) (((data[sidx + 2] >>> 6) & 003) |
                ((data[sidx + 1] << 2) & 077));
            dest[didx + 3] = (byte) (data[sidx + 2] & 077);
        }

        // 0-63 to ascii printable conversion
        for (int idx = 0; idx < dest.length; idx++) {
            if (dest[idx] < 26) {
                dest[idx] = (byte) (dest[idx] + 'A');
            } else if (dest[idx] < 52) {
                dest[idx] = (byte) ((dest[idx] + 'a') - 26);
            } else if (dest[idx] < 62) {
                dest[idx] = (byte) ((dest[idx] + '0') - 52);
            } else if (dest[idx] < 63) {
                dest[idx] = (byte) '+';
            } else {
                dest[idx] = (byte) '/';
            }
        }

        // add padding
        for (int idx = dest.length - 1; idx > ((d.length * 4) / 3); idx--) {
            dest[idx] = (byte) '=';
        }

        return new String(dest);
    }

    /**
     * Encode a String using Base64 using the default platform encoding
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static String encode(String s) {
        return encode(s.getBytes());
    }

    /**
     * Decode data and return bytes.
     *
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static byte[] decode(String str) {
        if (str == null) {
            return null;
        }

        byte[] data = str.getBytes();

        return decode(data);
    }

    /**
     * Decode data and return bytes.  Assumes that the data passed in is ASCII
     * text.
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final static byte[] decode(byte[] data) {
        int tail = data.length;

        while (data[tail - 1] == '=') {
            tail--;
        }

        byte[] dest = new byte[tail - (data.length / 4)];

        // ascii printable to 0-63 conversion
        for (int idx = 0; idx < data.length; idx++) {
            if (data[idx] == '=') {
                data[idx] = 0;
            } else if (data[idx] == '/') {
                data[idx] = 63;
            } else if (data[idx] == '+') {
                data[idx] = 62;
            } else if ((data[idx] >= '0') && (data[idx] <= '9')) {
                data[idx] = (byte) (data[idx] - ('0' - 52));
            } else if ((data[idx] >= 'a') && (data[idx] <= 'z')) {
                data[idx] = (byte) (data[idx] - ('a' - 26));
            } else if ((data[idx] >= 'A') && (data[idx] <= 'Z')) {
                data[idx] = (byte) (data[idx] - 'A');
            }
        }

        // 4-byte to 3-byte conversion
        int sidx;

        // 4-byte to 3-byte conversion
        int didx;

        for (sidx = 0, didx = 0; didx < (dest.length - 2);
                sidx += 4, didx += 3) {
            dest[didx] = (byte) (((data[sidx] << 2) & 255) |
                ((data[sidx + 1] >>> 4) & 3));
            dest[didx + 1] = (byte) (((data[sidx + 1] << 4) & 255) |
                ((data[sidx + 2] >>> 2) & 017));
            dest[didx + 2] = (byte) (((data[sidx + 2] << 6) & 255) |
                (data[sidx + 3] & 077));
        }

        if (didx < dest.length) {
            dest[didx] = (byte) (((data[sidx] << 2) & 255) |
                ((data[sidx + 1] >>> 4) & 3));
        }

        if (++didx < dest.length) {
            dest[didx] = (byte) (((data[sidx + 1] << 4) & 255) |
                ((data[sidx + 2] >>> 2) & 017));
        }

        return dest;
    }

    /**
     * A simple test that encodes and decodes the first commandline argument.
     *
     * @param args DOCUMENT ME!
     */
    public static final void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Base64 string");
            System.exit(0);
        }

        try {
            String e = Base64.encode(args[0].getBytes());
            String d = new String(Base64.decode(e));
            System.out.println("Input   = '" + args[0] + "'");
            System.out.println("Encoded = '" + e + "'");
            System.out.println("Decoded = '" + d + "'");
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
