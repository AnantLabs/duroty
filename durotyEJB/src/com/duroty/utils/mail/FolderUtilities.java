package com.duroty.utils.mail;

import java.util.Arrays;
import java.util.Comparator;

import javax.mail.Folder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class FolderUtilities {
    /**
     * Creates a new FolderUtilities object.
     */
    public FolderUtilities() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param sub DOCUMENT ME!
     */
    public static void sortFolderList(Folder[] sub) {
        int inner = 0;
        int outer = 0;

        for (inner = 0; inner < sub.length; ++inner) {
            if (sub[inner].getName().equalsIgnoreCase("INBOX")) {
                if (inner > 0) {
                    Folder temp = sub[inner];
                    sub[inner] = sub[0];
                    sub[0] = temp;
                }

                outer = 1;

                break;
            }
        }

        /* outer is inherited! */
        for (; outer < (sub.length - 1); ++outer) {
            for (inner = outer + 1; inner < sub.length; ++inner) {
                if (sub[outer].getName().compareTo(sub[inner].getName()) > 0) {
                    Folder temp = sub[outer];
                    sub[outer] = sub[inner];
                    sub[inner] = temp;
                    --outer;

                    break;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sub DOCUMENT ME!
     */
    public static void sortFolderList(Folder[] sub, Comparator comparator) {
        Arrays.sort(sub, comparator);
    }
}
