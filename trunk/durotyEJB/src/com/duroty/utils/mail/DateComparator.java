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
public class DateComparator implements Comparator {
    /**
     * Creates a new DateComparator object.
     */
    public DateComparator() {
        super();

        // TODO Auto-generated constructor stub
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

        return (d1.compareTo(d0));
    }
}
