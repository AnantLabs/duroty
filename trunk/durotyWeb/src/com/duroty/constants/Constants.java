package com.duroty.constants;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public final class Constants {
	public static final String CONTEXT_PROPERTIES = "contextProperties";
	public static final String JAAS_USERNAME = "j_username";
	public static final String JAAS_PASSWORD = "j_password";
	public static final String COOKIE_USERNAME = "c_username";
	public static final String COOKIE_PASSWORD = "c_password";
	
	/** DOCUMENT ME! */
    public final static String WELCOME_PATH = "welcomePath";
    
    /** DOCUMENT ME! */
    public final static String PUBLIC_PATH = "publicPath";
	
	/** DOCUMENT ME! */
    public final static String CONTROL_URI = "controlURI";
	
	/** DOCUMENT ME! */
    public final static String INDEX_URI = "/index.drt";
    
    /** DOCUMENT ME! */
    public final static String LOGON_URI = "logonURI";

    /** DOCUMENT ME! */
    public final static String LOGOFF_URI = "logoffURI";
    
    /** DOCUMENT ME! */
    public final static String LOGIN_FAILED_URI = "loginFailedURI";

    /** DOCUMENT ME! */
    public final static String AUTHENTICATION_FAILED_URI = "authenticationFailedURI";

    /** DOCUMENT ME! */
    public final static String LOGON_PROCESS_URI = "logonProcessURI";  
    
    /** DOCUMENT ME! */
    public final static String LOGOFF_PROCESS_URI = "logoffProcessURI";

    /** DOCUMENT ME! */
    public final static String ACCESS_DENIED_URI = "accessDeniedURI";
    
    /** DOCUMENT ME! */
    public final static String CHANGE_PASSWORD_URI = "changePasswordURI";

    /** DOCUMENT ME! */
    public static final String ACTION_SUCCESS_FORWARD = "success";

    /** DOCUMENT ME! */
    public static final String ACTION_FAIL_FORWARD = "failure";

    /** DOCUMENT ME! */
    public static final String INTERNAL_ERROR_GLOBAL_FORWARD = "internalError";

    /** DOCUMENT ME! */
    public static final String ILLEGAL_CONCURRENT_ACCESS_GLOBAL_FORWARD = "illegalConcurrentAccess";

    /** DOCUMENT ME! */
    public static final String AUTO_LOCALE_GLOBAL_FORWARD = "autoLocale";

    /** DOCUMENT ME! */
    public static final String USER_IS_NULL_GLOBAL_FORWARD = "userIsNull";

    /** DOCUMENT ME! */
    public static final String INCORRECT_PASSWORD_GLOBAL_FORWARD = "incorrectPassword";

    /** DOCUMENT ME! */
    public static final String USER_NOT_FOUND_GLOBAL_FORWARD = "userNotFound";

    /** DOCUMENT ME! */
    public static final String USER_NOT_ACTIVE_GLOBAL_FORWARD = "userNotActive";

    /** DOCUMENT ME! */
    public static final String USER_NOT_BEEN_AUTHENTICATED_GLOBAL_FORWARD = "userNotBeenAuthenticated";

    /** DOCUMENT ME! */
    public static final String LOGIN_GLOGAL_FORWARD = "login";

    /** DOCUMENT ME! */
    public static final String ROLE_REQUIRED_GLOGAL_FORWARD = "roleRequired";

    /** DOCUMENT ME! */
    public static final String USER_VIEW = "userView";

    /** DOCUMENT ME! */
    public static final String LANGUAGE_ERROR_PARAMETER = "languageErrors";

    /** DOCUMENT ME! */
    public static final String EXCEPTION_PARAMENTER = "exception";

    /** DOCUMENT ME! */
    public static final String ANONIMOUS_USER = "anonimous";

    /** DOCUMENT ME! */
    public static final String LOGIN_PROFILE = "loginProfile";

    /** DOCUMENT ME! */
    public static final String USER = "user";

    /** DOCUMENT ME! */
    public static final String USERNAME_COOKIE = "username";

    /** DOCUMENT ME! */
    public static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";
    
    /** DOCUMENT ME! */
    public static final String USERNAME_COOKIE_GUEST = "usernameGuest";

    /** DOCUMENT ME! */
    public static final String ENCRYPTED_PASSWORD_COOKIE_GUEST = "encryptedPasswordGuest";

    /** DOCUMENT ME! */
    public static final String LOGIN_SESSION_INSTANCE = "loginInstance";

    /** DOCUMENT ME! */
    public static final String USER_INFORMATION_SESSION_INSTANCE = "userInformationInstance";
    
    /** DOCUMENT ME! */
    public static final String USER_INFORMATION = "userInformation";
    
    /** DOCUMENT ME! */
    public static final String SESSION_ID = "sessionId";

    /** DOCUMENT ME! */
    public static final String LOGIN_REFERER = "loginReferer";

    /** DOCUMENT ME! */
    public static final String MAIL_SESSION_INSTANCE = "mailSessionInstance";
    
    public static final String MAIL_SEARCH_INSTANCE = "mailSearchInstance";
    
    /** DOCUMENT ME! */
    public static final String MAIL_USER_INFORMATION_INSTANCE = "mailUserInformationInstance";
    
    public static final String MAIL_PREFERENCES_INSTANCE = "mailPreferencesInstance";
    
    public static final String MAIL_PREFERENCES = "mailPreferences";
    
    /** DOCUMENT ME! */
    public static final String MANAGER_ROLE_INSTANCE = "managerRoleInstance";
    
    /** DOCUMENT ME! */
    public static final String MANAGER_DOMAIN_INSTANCE = "managerDomainInstance";
    
    /** DOCUMENT ME! */
    public static final String MANAGER_PERMISSION_INSTANCE = "managerPermissionInstance";
    
    /** DOCUMENT ME! */
    public static final String MAIN_RSS_INSTANCE = "rssInstance";
    
    public static final String BOOKMARK_INSTANCE = "bookmarkInstance";

    /**
     * Creates a new Constants object.
     */
    private Constants() {
    }
}
