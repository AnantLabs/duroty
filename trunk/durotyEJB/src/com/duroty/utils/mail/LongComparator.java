package com.duroty.utils.mail;

import java.util.Comparator;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class LongComparator implements Comparator {
    /**
     * Creates a new LongComparator object.
     */
    public LongComparator() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1, Object o2) {
        Date d0;
        Date d1;

        try {
            d0 = ((Message) o1).getReceivedDate();
            d1 = ((Message) o2).getReceivedDate();
        } catch (MessagingException me) {
            return 0;
        }

        long i = d0.getTime();
        long j = d1.getTime();

        return (i == j) ? 0 : ((i > j) ? 1 : (-1));
    }
}
