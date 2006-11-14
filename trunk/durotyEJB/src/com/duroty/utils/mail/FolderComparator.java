package com.duroty.utils.mail;

import java.util.Comparator;

import javax.mail.Folder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class FolderComparator implements Comparator {
    /**
     * Creates a new FolderComparator object.
     */
    public FolderComparator() {
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
        String s0;
        String s1;

        s0 = ((Folder) o1).getFullName();                       
        s1 = ((Folder) o2).getFullName();
        
        if (s1.equalsIgnoreCase("INBOX")) {
        	return 1;
        }
        
        if (s1.equalsIgnoreCase("Borrador")) {
        	return -1;
        }
        
        if (s0.equalsIgnoreCase("junkmail")) {
        	return 2;
        }

        return (s0.compareToIgnoreCase(s1));
    }
}
