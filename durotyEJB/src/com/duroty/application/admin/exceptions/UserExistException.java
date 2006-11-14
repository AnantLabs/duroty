package com.duroty.application.admin.exceptions;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class UserExistException extends AdminException {
    /**
     *
     */
    private static final long serialVersionUID = -1887867240880448306L;
    
    public UserExistException(String message) {
        super(message);
    }
}
