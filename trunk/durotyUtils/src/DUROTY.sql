/*==============================================================*/
/* DBMS name:      PostgreSQL 7.3                               */
/* Created on:     09/11/2006 15:54:44                          */
/*==============================================================*/


/*drop index ALIAS_PK_I;

drop index ALIAS_UK_I;

drop index USE_ALI_FK_I;

drop index ATTACHMENT_PK_I;

drop index MES_ATT_FK_I;

drop index BAYESIAN_HAM_PK_I;

drop index BAYESIAN_HAM_UK_I;

drop index USE_HAM_FK_I;

drop index BAYESIAN_MESSAGECOUNTS_PK_;

drop index BAYESIAN_MESSAGECOUNTS_UK_I;

drop index USE_BACO_FK_;

drop index BAYESIAN_SPAM_PK_I;

drop index BAYESIAN_SPAM_UK_I;

drop index USE_SPAM_FK_I;

drop index BOOKMARK_PK_I;

drop index USE_BOO_FK_I;

drop index BUDDY_LIST_PK_I;

drop index BUDDY_LIST_UK_I;

drop index USER_BUDDY_FK_I;

drop index USER_OWNER_FK_I;

drop index CONTACT_PK_I;

drop index CONTACT_UK_I;

drop index USE_CON_FK_I;

drop index CONTACT_LIST_PK_I;

drop index CONTACT_LIST_UK_I;

drop index USE_COLI_FK_I;

drop index CONVERSATIONS_PK_I;

drop index USER_RECIPIENT_FK_I;

drop index USER_SENDER_FK_I;

drop index COLI_CON_FK_I;

drop index CON_COLI_FK_I;

drop index CON_COLI_PK_I;

drop index FEED_CHANNEL_DS_I;

drop index FEED_CHANNEL_PK_I;

drop index USE_FECH_FK_I;

drop index FECH_FEDA_FK_I;

drop index FEED_DATA_PK_I;

drop index FEED_DATA_UK_I;

drop index FILTER_PK_I;

drop index LAB_FIL_FK_I;

drop index IDENTITY_PK_I;

drop index IDENTITY_UK_I;

drop index USE_IDE_FK_I;

drop index LABEL_PK_I;

drop index LABEL_UK_I;

drop index USE_LAB_FK_I;

drop index LAB_MES_FK_I;

drop index LAB_MES_PK_I;

drop index MES_LAB_FK_I;

drop index MAIL_PREFERENCES_PK_I;

drop index MAIL_PREFERENCES_UK_I;

drop index USE_MAPR_FK_I;

drop index MESSAGE_PK_I;

drop index MESSAGE_UK_I;

drop index USE_MES_FK_I;

drop index MESSAGE_PROPERTIES_FK_I;

drop index MESSAGE_PROPERTIES_PK_I;

drop index ROLES_PK_I;

drop index ROLES_UK_I;

drop index USERS_PK_I;

drop index USE_USERNAME_UK_I;

drop index ROLE_USER_FK_I;

drop index USER_ROLE_FK_I;

drop index USER_ROLE_PK_I;

drop table ALIAS;

drop table ATTACHMENT;

drop table BAYESIAN_HAM;

drop table BAYESIAN_MESSAGECOUNTS;

drop table BAYESIAN_SPAM;

drop table BOOKMARK;

drop table BUDDY_LIST;

drop table CONTACT;

drop table CONTACT_LIST;

drop table CONVERSATIONS;

drop table CON_COLI;

drop table FEED_CHANNEL;

drop table FEED_DATA;

drop table FILTER;

drop table IDENTITY;

drop table LABEL;

drop table LAB_MES;

drop table MAIL_PREFERENCES;

drop table MESSAGE;

drop table MESSAGE_PROPERTIES;

drop table ROLES;

drop table USERS;

drop table USER_ROLE;

drop sequence SEQ_BAYESIAN;

drop sequence SEQ_BOOKMARK;

drop sequence SEQ_CHAT;

drop sequence SEQ_DUROTY;*/

create sequence SEQ_BAYESIAN
increment 1
minvalue 1;

create sequence SEQ_BOOKMARK
increment 1
start 1
cycle;

create sequence SEQ_CHAT
increment 1
start 1;

create sequence SEQ_DUROTY
increment 1
start 1;

/*==============================================================*/
/* Table: ALIAS                                                 */
/*==============================================================*/
create table ALIAS (
ALI_IDINT            INT4                 not null,
ALI_USE_IDINT        INT4                 not null,
ALI_NAME             VARCHAR(200)         not null
);

alter table ALIAS
   add constraint PK_ALIAS primary key (ALI_IDINT);

/*==============================================================*/
/* Index: ALIAS_PK_I                                            */
/*==============================================================*/
create unique index ALIAS_PK_I on ALIAS (
ALI_IDINT
);

/*==============================================================*/
/* Index: USE_ALI_FK_I                                          */
/*==============================================================*/
create  index USE_ALI_FK_I on ALIAS (
ALI_USE_IDINT
);

/*==============================================================*/
/* Index: ALIAS_UK_I                                            */
/*==============================================================*/
create unique index ALIAS_UK_I on ALIAS (
ALI_NAME
);

/*==============================================================*/
/* Table: ATTACHMENT                                            */
/*==============================================================*/
create table ATTACHMENT (
ATT_IDINT            INT4                 not null,
ATT_MES_IDINT        INT4                 not null,
ATT_NAME             VARCHAR(255)         not null,
ATT_SIZE             INT4                 not null,
ATT_PART             INT4                 not null,
ATT_CONTENT_TYPE     VARCHAR(50)          not null
);

alter table ATTACHMENT
   add constraint PK_ATTACHMENT primary key (ATT_IDINT);

/*==============================================================*/
/* Index: ATTACHMENT_PK_I                                       */
/*==============================================================*/
create unique index ATTACHMENT_PK_I on ATTACHMENT (
ATT_IDINT
);

/*==============================================================*/
/* Index: MES_ATT_FK_I                                          */
/*==============================================================*/
create  index MES_ATT_FK_I on ATTACHMENT (
ATT_MES_IDINT
);

/*==============================================================*/
/* Table: BAYESIAN_HAM                                          */
/*==============================================================*/
create table BAYESIAN_HAM (
HAM_IDINT            INT4                 not null,
HAM_USE_IDINT        INT4                 not null,
HAM_TOKEN            VARCHAR(255)         not null,
HAM_OCURRENCES       INT4                 not null
);

alter table BAYESIAN_HAM
   add constraint PK_BAYESIAN_HAM primary key (HAM_IDINT);

/*==============================================================*/
/* Index: BAYESIAN_HAM_PK_I                                     */
/*==============================================================*/
create unique index BAYESIAN_HAM_PK_I on BAYESIAN_HAM (
HAM_IDINT
);

/*==============================================================*/
/* Index: USE_HAM_FK_I                                          */
/*==============================================================*/
create  index USE_HAM_FK_I on BAYESIAN_HAM (
HAM_USE_IDINT
);

/*==============================================================*/
/* Index: BAYESIAN_HAM_UK_I                                     */
/*==============================================================*/
create unique index BAYESIAN_HAM_UK_I on BAYESIAN_HAM (
HAM_USE_IDINT,
HAM_TOKEN
);

/*==============================================================*/
/* Table: BAYESIAN_MESSAGECOUNTS                                */
/*==============================================================*/
create table BAYESIAN_MESSAGECOUNTS (
IDINT                INT4                 not null,
USE_IDINT            INT4                 not null,
HAM_COUNT            INT4                 not null,
SPAM_COUNT           INT4                 not null
);

alter table BAYESIAN_MESSAGECOUNTS
   add constraint PK_BAYESIAN_MESSAGECOUNTS primary key (IDINT);

/*==============================================================*/
/* Index: BAYESIAN_MESSAGECOUNTS_PK_                            */
/*==============================================================*/
create unique index BAYESIAN_MESSAGECOUNTS_PK_ on BAYESIAN_MESSAGECOUNTS (
IDINT
);

/*==============================================================*/
/* Index: USE_BACO_FK_                                          */
/*==============================================================*/
create  index USE_BACO_FK_ on BAYESIAN_MESSAGECOUNTS (
USE_IDINT
);

/*==============================================================*/
/* Index: BAYESIAN_MESSAGECOUNTS_UK_I                           */
/*==============================================================*/
create unique index BAYESIAN_MESSAGECOUNTS_UK_I on BAYESIAN_MESSAGECOUNTS (
USE_IDINT
);

/*==============================================================*/
/* Table: BAYESIAN_SPAM                                         */
/*==============================================================*/
create table BAYESIAN_SPAM (
SPAM_IDINT           INT4                 not null,
SPAM_USE_IDINT       INT4                 not null,
SPAM_TOKEN           VARCHAR(255)         not null,
SPAM_OCURRENCES      INT4                 not null
);

alter table BAYESIAN_SPAM
   add constraint PK_BAYESIAN_SPAM primary key (SPAM_IDINT);

/*==============================================================*/
/* Index: BAYESIAN_SPAM_PK_I                                    */
/*==============================================================*/
create unique index BAYESIAN_SPAM_PK_I on BAYESIAN_SPAM (
SPAM_IDINT
);

/*==============================================================*/
/* Index: USE_SPAM_FK_I                                         */
/*==============================================================*/
create  index USE_SPAM_FK_I on BAYESIAN_SPAM (
SPAM_USE_IDINT
);

/*==============================================================*/
/* Index: BAYESIAN_SPAM_UK_I                                    */
/*==============================================================*/
create unique index BAYESIAN_SPAM_UK_I on BAYESIAN_SPAM (
SPAM_USE_IDINT,
SPAM_TOKEN
);

/*==============================================================*/
/* Table: BOOKMARK                                              */
/*==============================================================*/
create table BOOKMARK (
BOO_IDINT            INT4                 not null,
BOO_USE_IDINT        INT4                 not null,
BOO_URL              TEXT                 not null,
BOO_TITLE            TEXT                 null,
BOO_KEYWORDS         TEXT                 null,
BOO_COMMENTS         TEXT                 null
);

alter table BOOKMARK
   add constraint PK_BOOKMARK primary key (BOO_IDINT);

/*==============================================================*/
/* Index: BOOKMARK_PK_I                                         */
/*==============================================================*/
create unique index BOOKMARK_PK_I on BOOKMARK (
BOO_IDINT
);

/*==============================================================*/
/* Index: USE_BOO_FK_I                                          */
/*==============================================================*/
create  index USE_BOO_FK_I on BOOKMARK (
BOO_USE_IDINT
);

/*==============================================================*/
/* Table: BUDDY_LIST                                            */
/*==============================================================*/
create table BUDDY_LIST (
BULI_IDINT           INT4                 not null,
BULI_OWNER_IDINT     INT4                 not null,
BULI_BUDDY_IDINT     INT4                 not null,
BULI_ACTIVE          BOOL                 not null,
BULI_LAST_DATE       TIMESTAMP            not null
);

alter table BUDDY_LIST
   add constraint PK_BUDDY_LIST primary key (BULI_IDINT);

/*==============================================================*/
/* Index: BUDDY_LIST_PK_I                                       */
/*==============================================================*/
create unique index BUDDY_LIST_PK_I on BUDDY_LIST (
BULI_IDINT
);

/*==============================================================*/
/* Index: USER_OWNER_FK_I                                       */
/*==============================================================*/
create  index USER_OWNER_FK_I on BUDDY_LIST (
BULI_OWNER_IDINT
);

/*==============================================================*/
/* Index: USER_BUDDY_FK_I                                       */
/*==============================================================*/
create  index USER_BUDDY_FK_I on BUDDY_LIST (
BULI_BUDDY_IDINT
);

/*==============================================================*/
/* Index: BUDDY_LIST_UK_I                                       */
/*==============================================================*/
create unique index BUDDY_LIST_UK_I on BUDDY_LIST (
BULI_OWNER_IDINT,
BULI_BUDDY_IDINT
);

/*==============================================================*/
/* Table: CONTACT                                               */
/*==============================================================*/
create table CONTACT (
CON_IDINT            INT4                 not null,
CON_USE_IDINT        INT4                 not null,
CON_NAME             VARCHAR(50)          null,
CON_EMAIL            VARCHAR(255)         not null,
CON_SENT_DATE        TIMESTAMP            null,
CON_RECEIVED_DATE    TIMESTAMP            null,
CON_COUNT            INT4                 not null,
CON_DESCRIPTION      TEXT                 null
);

alter table CONTACT
   add constraint PK_CONTACT primary key (CON_IDINT);

/*==============================================================*/
/* Index: CONTACT_PK_I                                          */
/*==============================================================*/
create unique index CONTACT_PK_I on CONTACT (
CON_IDINT
);

/*==============================================================*/
/* Index: USE_CON_FK_I                                          */
/*==============================================================*/
create  index USE_CON_FK_I on CONTACT (
CON_USE_IDINT
);

/*==============================================================*/
/* Index: CONTACT_UK_I                                          */
/*==============================================================*/
create unique index CONTACT_UK_I on CONTACT (
CON_USE_IDINT,
CON_EMAIL
);

/*==============================================================*/
/* Table: CONTACT_LIST                                          */
/*==============================================================*/
create table CONTACT_LIST (
COLI_IDINT           INT4                 not null,
COLI_USE_IDINT       INT4                 not null,
COLI_NAME            VARCHAR(100)         not null
);

alter table CONTACT_LIST
   add constraint PK_CONTACT_LIST primary key (COLI_IDINT);

/*==============================================================*/
/* Index: CONTACT_LIST_PK_I                                     */
/*==============================================================*/
create unique index CONTACT_LIST_PK_I on CONTACT_LIST (
COLI_IDINT
);

/*==============================================================*/
/* Index: USE_COLI_FK_I                                         */
/*==============================================================*/
create  index USE_COLI_FK_I on CONTACT_LIST (
COLI_USE_IDINT
);

/*==============================================================*/
/* Index: CONTACT_LIST_UK_I                                     */
/*==============================================================*/
create unique index CONTACT_LIST_UK_I on CONTACT_LIST (
COLI_USE_IDINT,
COLI_NAME
);

/*==============================================================*/
/* Table: CONVERSATIONS                                         */
/*==============================================================*/
create table CONVERSATIONS (
CONV_IDINT           INT4                 not null,
CONV_SENDER_IDINT    INT4                 not null,
CONV_RECIPIENT_IDINT INT4                 not null,
CONV_MESSAGE         TEXT                 not null,
CONV_STAMP           TIMESTAMP            not null
);

alter table CONVERSATIONS
   add constraint PK_CONVERSATIONS primary key (CONV_IDINT);

/*==============================================================*/
/* Index: CONVERSATIONS_PK_I                                    */
/*==============================================================*/
create unique index CONVERSATIONS_PK_I on CONVERSATIONS (
CONV_IDINT
);

/*==============================================================*/
/* Index: USER_SENDER_FK_I                                      */
/*==============================================================*/
create  index USER_SENDER_FK_I on CONVERSATIONS (
CONV_SENDER_IDINT
);

/*==============================================================*/
/* Index: USER_RECIPIENT_FK_I                                   */
/*==============================================================*/
create  index USER_RECIPIENT_FK_I on CONVERSATIONS (
CONV_RECIPIENT_IDINT
);

/*==============================================================*/
/* Table: CON_COLI                                              */
/*==============================================================*/
create table CON_COLI (
CON_IDINT            INT4                 not null,
COLI_IDINT           INT4                 not null
);

alter table CON_COLI
   add constraint PK_CON_COLI primary key (CON_IDINT, COLI_IDINT);

/*==============================================================*/
/* Index: CON_COLI_PK_I                                         */
/*==============================================================*/
create unique index CON_COLI_PK_I on CON_COLI (
CON_IDINT,
COLI_IDINT
);

/*==============================================================*/
/* Index: CON_COLI_FK_I                                         */
/*==============================================================*/
create  index CON_COLI_FK_I on CON_COLI (
CON_IDINT
);

/*==============================================================*/
/* Index: COLI_CON_FK_I                                         */
/*==============================================================*/
create  index COLI_CON_FK_I on CON_COLI (
COLI_IDINT
);

/*==============================================================*/
/* Table: FEED_CHANNEL                                          */
/*==============================================================*/
create table FEED_CHANNEL (
FECH_IDINT           INT4                 not null,
FECH_USE_IDINT       INT4                 not null,
FECH_NAME            VARCHAR(50)          not null
);

alter table FEED_CHANNEL
   add constraint PK_FEED_CHANNEL primary key (FECH_IDINT);

/*==============================================================*/
/* Index: FEED_CHANNEL_PK_I                                     */
/*==============================================================*/
create unique index FEED_CHANNEL_PK_I on FEED_CHANNEL (
FECH_IDINT
);

/*==============================================================*/
/* Index: USE_FECH_FK_I                                         */
/*==============================================================*/
create  index USE_FECH_FK_I on FEED_CHANNEL (
FECH_USE_IDINT
);

/*==============================================================*/
/* Index: FEED_CHANNEL_DS_I                                     */
/*==============================================================*/
create unique index FEED_CHANNEL_DS_I on FEED_CHANNEL (
FECH_USE_IDINT,
FECH_NAME
);

/*==============================================================*/
/* Table: FEED_DATA                                             */
/*==============================================================*/
create table FEED_DATA (
FEDA_IDINT           INT4                 not null,
FEDA_FECH_IDINT      INT4                 not null,
FEDA_NAME            VARCHAR(50)          not null,
FEDA_VALUE           TEXT                 not null
);

alter table FEED_DATA
   add constraint PK_FEED_DATA primary key (FEDA_IDINT);

/*==============================================================*/
/* Index: FEED_DATA_PK_I                                        */
/*==============================================================*/
create unique index FEED_DATA_PK_I on FEED_DATA (
FEDA_IDINT
);

/*==============================================================*/
/* Index: FECH_FEDA_FK_I                                        */
/*==============================================================*/
create  index FECH_FEDA_FK_I on FEED_DATA (
FEDA_FECH_IDINT
);

/*==============================================================*/
/* Index: FEED_DATA_UK_I                                        */
/*==============================================================*/
create unique index FEED_DATA_UK_I on FEED_DATA (
FEDA_FECH_IDINT,
FEDA_NAME
);

/*==============================================================*/
/* Table: FILTER                                                */
/*==============================================================*/
create table FILTER (
FIL_IDINT            INT4                 not null,
FIL_LAB_IDINT        INT4                 not null,
FIL_FROM             TEXT                 null,
FIL_TO               TEXT                 null,
FIL_SUBJECT          TEXT                 null,
FIL_HAS_WORDS        TEXT                 null,
FIL_DOESNT_HAVE_WORDS TEXT                 null,
FIL_HAS_ATTACMENT    BOOL                 not null,
FIL_ARCHIVE          BOOL                 not null,
FIL_IMPORTANT        BOOL                 not null,
FIL_TRASH            BOOL                 not null,
FIL_FORWARD_TO       VARCHAR(255)         null,
FIL_OR_OPERATOR      BOOL                 not null
);

alter table FILTER
   add constraint PK_FILTER primary key (FIL_IDINT);

/*==============================================================*/
/* Index: FILTER_PK_I                                           */
/*==============================================================*/
create unique index FILTER_PK_I on FILTER (
FIL_IDINT
);

/*==============================================================*/
/* Index: LAB_FIL_FK_I                                          */
/*==============================================================*/
create  index LAB_FIL_FK_I on FILTER (
FIL_LAB_IDINT
);

/*==============================================================*/
/* Table: IDENTITY                                              */
/*==============================================================*/
create table IDENTITY (
IDE_IDINT            INT4                 not null,
IDE_USE_IDINT        INT4                 not null,
IDE_NAME             VARCHAR(50)          null,
IDE_EMAIL            VARCHAR(255)         not null,
IDE_REPLY_TO         VARCHAR(255)         not null,
IDE_CODE             VARCHAR(25)          not null,
IDE_ACTIVE           BOOL                 not null,
IDE_DEFAULT          BOOL                 not null
);

alter table IDENTITY
   add constraint PK_IDENTITY primary key (IDE_IDINT);

/*==============================================================*/
/* Index: IDENTITY_PK_I                                         */
/*==============================================================*/
create unique index IDENTITY_PK_I on IDENTITY (
IDE_IDINT
);

/*==============================================================*/
/* Index: USE_IDE_FK_I                                          */
/*==============================================================*/
create  index USE_IDE_FK_I on IDENTITY (
IDE_USE_IDINT
);

/*==============================================================*/
/* Index: IDENTITY_UK_I                                         */
/*==============================================================*/
create unique index IDENTITY_UK_I on IDENTITY (
IDE_USE_IDINT,
IDE_EMAIL
);

/*==============================================================*/
/* Table: LABEL                                                 */
/*==============================================================*/
create table LABEL (
LAB_IDINT            INT4                 not null,
LAB_USE_IDINT        INT4                 not null,
LAB_NAME             VARCHAR(50)          not null
);

alter table LABEL
   add constraint PK_LABEL primary key (LAB_IDINT);

/*==============================================================*/
/* Index: LABEL_PK_I                                            */
/*==============================================================*/
create unique index LABEL_PK_I on LABEL (
LAB_IDINT
);

/*==============================================================*/
/* Index: USE_LAB_FK_I                                          */
/*==============================================================*/
create  index USE_LAB_FK_I on LABEL (
LAB_USE_IDINT
);

/*==============================================================*/
/* Index: LABEL_UK_I                                            */
/*==============================================================*/
create unique index LABEL_UK_I on LABEL (
LAB_USE_IDINT,
LAB_NAME
);

/*==============================================================*/
/* Table: LAB_MES                                               */
/*==============================================================*/
create table LAB_MES (
MES_IDINT            INT4                 not null,
LAB_IDINT            INT4                 not null
);

alter table LAB_MES
   add constraint PK_LAB_MES primary key (MES_IDINT, LAB_IDINT);

/*==============================================================*/
/* Index: LAB_MES_PK_I                                          */
/*==============================================================*/
create unique index LAB_MES_PK_I on LAB_MES (
MES_IDINT,
LAB_IDINT
);

/*==============================================================*/
/* Index: LAB_MES_FK_I                                          */
/*==============================================================*/
create  index LAB_MES_FK_I on LAB_MES (
MES_IDINT
);

/*==============================================================*/
/* Index: MES_LAB_FK_I                                          */
/*==============================================================*/
create  index MES_LAB_FK_I on LAB_MES (
LAB_IDINT
);

/*==============================================================*/
/* Table: MAIL_PREFERENCES                                      */
/*==============================================================*/
create table MAIL_PREFERENCES (
MAPR_IDINT           INT4                 not null,
MAPR_USE_IDINT       INT4                 not null,
MAPR_MESSAGES_BY_PAGE INT4                 not null,
MAPR_SIGNATURE       TEXT                 null,
MAPR_HTML_MESSAGE    BOOL                 not null,
MAPR_VACATION_ACTIVE BOOL                 not null,
MAPR_VACATION_SUBJECT TEXT                 null,
MAPR_VACATION_BODY   TEXT                 null,
MAPR_QUOTA_SIZE      INT4                 not null,
MAPR_SPAM_TOLERANCE  INT4                 not null
);

alter table MAIL_PREFERENCES
   add constraint PK_MAIL_PREFERENCES primary key (MAPR_IDINT);

/*==============================================================*/
/* Index: MAIL_PREFERENCES_PK_I                                 */
/*==============================================================*/
create unique index MAIL_PREFERENCES_PK_I on MAIL_PREFERENCES (
MAPR_IDINT
);

/*==============================================================*/
/* Index: USE_MAPR_FK_I                                         */
/*==============================================================*/
create  index USE_MAPR_FK_I on MAIL_PREFERENCES (
MAPR_USE_IDINT
);

/*==============================================================*/
/* Index: MAIL_PREFERENCES_UK_I                                 */
/*==============================================================*/
create unique index MAIL_PREFERENCES_UK_I on MAIL_PREFERENCES (
MAPR_USE_IDINT
);

/*==============================================================*/
/* Table: MESSAGE                                               */
/*==============================================================*/
create table MESSAGE (
MES_IDINT            INT4                 not null,
MES_USE_IDINT        INT4                 not null,
MES_NAME             VARCHAR(200)         not null,
MES_REFERENCES       VARCHAR(200)         null,
MES_BOX              VARCHAR(15)          not null,
MES_FROM             VARCHAR(255)         not null,
MES_TO               TEXT                 not null,
MES_CC               TEXT                 null,
MES_BCC              TEXT                 null,
MES_REPLY_TO         VARCHAR(255)         not null,
MES_SUBJECT          TEXT                 null,
MES_BODY             TEXT                 null,
MES_DATE             TIMESTAMP            not null,
MES_FLAGGED          BOOL                 not null,
MES_RECENT           BOOL                 not null,
MES_SIZE             INT4                 not null,
MES_HEADERS          TEXT                 null
);

alter table MESSAGE
   add constraint PK_MESSAGE primary key (MES_IDINT);

/*==============================================================*/
/* Index: MESSAGE_PK_I                                          */
/*==============================================================*/
create unique index MESSAGE_PK_I on MESSAGE (
MES_IDINT
);

/*==============================================================*/
/* Index: USE_MES_FK_I                                          */
/*==============================================================*/
create  index USE_MES_FK_I on MESSAGE (
MES_USE_IDINT
);

/*==============================================================*/
/* Index: MESSAGE_UK_I                                          */
/*==============================================================*/
create unique index MESSAGE_UK_I on MESSAGE (
MES_USE_IDINT,
MES_NAME
);

/*==============================================================*/
/* Table: MESSAGE_PROPERTIES                                    */
/*==============================================================*/
create table MESSAGE_PROPERTIES (
PRO_IDINT            INT4                 not null,
PRO_MES_IDINT        INT4                 not null,
PRO_SCHEDULE         TIMESTAMP            not null,
PRO_GROUPS           TEXT                 not null,
PRO_LANGUAGE         TEXT                 not null,
PRO_TYPE             INT4                 not null
);

alter table MESSAGE_PROPERTIES
   add constraint PK_MESSAGE_PROPERTIES primary key (PRO_IDINT);

/*==============================================================*/
/* Index: MESSAGE_PROPERTIES_PK_I                               */
/*==============================================================*/
create unique index MESSAGE_PROPERTIES_PK_I on MESSAGE_PROPERTIES (
PRO_IDINT
);

/*==============================================================*/
/* Index: MESSAGE_PROPERTIES_FK_I                               */
/*==============================================================*/
create  index MESSAGE_PROPERTIES_FK_I on MESSAGE_PROPERTIES (
PRO_MES_IDINT
);

/*==============================================================*/
/* Table: ROLES                                                 */
/*==============================================================*/
create table ROLES (
ROL_IDINT            INT4                 not null,
ROL_NAME             VARCHAR(25)          not null
);

alter table ROLES
   add constraint PK_ROLES primary key (ROL_IDINT);

/*==============================================================*/
/* Index: ROLES_PK_I                                            */
/*==============================================================*/
create unique index ROLES_PK_I on ROLES (
ROL_IDINT
);

/*==============================================================*/
/* Index: ROLES_UK_I                                            */
/*==============================================================*/
create unique index ROLES_UK_I on ROLES (
ROL_NAME
);

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS (
USE_IDINT            INT4                 not null,
USE_USERNAME         VARCHAR(200)         not null,
USE_PASSWORD         VARCHAR(15)          not null,
USE_NAME             VARCHAR(255)         null,
USE_EMAIL            VARCHAR(255)         not null,
USE_LANGUAGE         VARCHAR(10)          not null,
USE_ACTIVE           BOOL                 not null,
USE_REGISTER_DATE    TIMESTAMP            not null,
USE_IS_ONLINE        INT4                 null,
USE_LAST_PING        TIMESTAMP            null,
USE_LAST_STATE       INT4                 null,
USE_CUSTOM_MESSAGE   TEXT                 null
);

alter table USERS
   add constraint PK_USERS primary key (USE_IDINT);

/*==============================================================*/
/* Index: USERS_PK_I                                            */
/*==============================================================*/
create unique index USERS_PK_I on USERS (
USE_IDINT
);

/*==============================================================*/
/* Index: USE_USERNAME_UK_I                                     */
/*==============================================================*/
create unique index USE_USERNAME_UK_I on USERS (
USE_USERNAME
);

/*==============================================================*/
/* Table: USER_ROLE                                             */
/*==============================================================*/
create table USER_ROLE (
USE_IDINT            INT4                 not null,
ROL_IDINT            INT4                 not null
);

alter table USER_ROLE
   add constraint PK_USER_ROLE primary key (USE_IDINT, ROL_IDINT);

/*==============================================================*/
/* Index: USER_ROLE_PK_I                                        */
/*==============================================================*/
create unique index USER_ROLE_PK_I on USER_ROLE (
USE_IDINT,
ROL_IDINT
);

/*==============================================================*/
/* Index: USER_ROLE_FK_I                                        */
/*==============================================================*/
create  index USER_ROLE_FK_I on USER_ROLE (
USE_IDINT
);

/*==============================================================*/
/* Index: ROLE_USER_FK_I                                        */
/*==============================================================*/
create  index ROLE_USER_FK_I on USER_ROLE (
ROL_IDINT
);

alter table ALIAS
   add constraint FK_ALIAS_USE_ALI_USERS foreign key (ALI_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table ATTACHMENT
   add constraint FK_ATTACHME_MES_ATT_MESSAGE foreign key (ATT_MES_IDINT)
      references MESSAGE (MES_IDINT)
      on delete restrict on update restrict;

alter table BAYESIAN_HAM
   add constraint FK_BAYESIAN_USE_HAM_USERS foreign key (HAM_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table BAYESIAN_MESSAGECOUNTS
   add constraint FK_BAYESIAN_USE_BACO_USERS foreign key (USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table BAYESIAN_SPAM
   add constraint FK_BAYESIAN_USE_SPAM_USERS foreign key (SPAM_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table BOOKMARK
   add constraint FK_BOOKMARK_USE_BOO_USERS foreign key (BOO_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table BUDDY_LIST
   add constraint FK_BUDDY_LI_USER_BUDD_USERS foreign key (BULI_BUDDY_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table BUDDY_LIST
   add constraint FK_BUDDY_LI_USER_OWNE_USERS foreign key (BULI_OWNER_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table CONTACT
   add constraint FK_CONTACT_USE_CON_USERS foreign key (CON_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table CONTACT_LIST
   add constraint FK_CONTACT__USE_COLI_USERS foreign key (COLI_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table CONVERSATIONS
   add constraint FK_CONVERSA_USER_RECI_USERS foreign key (CONV_RECIPIENT_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table CONVERSATIONS
   add constraint FK_CONVERSA_USER_SEND_USERS foreign key (CONV_SENDER_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table CON_COLI
   add constraint FK_CON_COLI_CON_COLI_CONTACT foreign key (CON_IDINT)
      references CONTACT (CON_IDINT)
      on delete restrict on update restrict;

alter table CON_COLI
   add constraint FK_CON_COLI_CON_COLI2_CONTACT_ foreign key (COLI_IDINT)
      references CONTACT_LIST (COLI_IDINT)
      on delete restrict on update restrict;

alter table FEED_CHANNEL
   add constraint FK_FEED_CHA_USE_FECH_USERS foreign key (FECH_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table FEED_DATA
   add constraint FK_FEED_DAT_FECH_FEDA_FEED_CHA foreign key (FEDA_FECH_IDINT)
      references FEED_CHANNEL (FECH_IDINT)
      on delete restrict on update restrict;

alter table FILTER
   add constraint FK_FILTER_LAB_FIL_LABEL foreign key (FIL_LAB_IDINT)
      references LABEL (LAB_IDINT)
      on delete restrict on update restrict;

alter table IDENTITY
   add constraint FK_IDENTITY_USE_IDE_USERS foreign key (IDE_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table LABEL
   add constraint FK_LABEL_USE_LAB_USERS foreign key (LAB_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table LAB_MES
   add constraint FK_LAB_MES_LAB_MES_MESSAGE foreign key (MES_IDINT)
      references MESSAGE (MES_IDINT)
      on delete restrict on update restrict;

alter table LAB_MES
   add constraint FK_LAB_MES_LAB_MES2_LABEL foreign key (LAB_IDINT)
      references LABEL (LAB_IDINT)
      on delete restrict on update restrict;

alter table MAIL_PREFERENCES
   add constraint FK_MAIL_PRE_USE_MAPR_USERS foreign key (MAPR_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table MESSAGE
   add constraint FK_MESSAGE_USE_MES_USERS foreign key (MES_USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table MESSAGE_PROPERTIES
   add constraint FK_MESSAGE__MESSAGE_P_MESSAGE foreign key (PRO_MES_IDINT)
      references MESSAGE (MES_IDINT)
      on delete restrict on update restrict;

alter table USER_ROLE
   add constraint FK_USER_ROL_USER_ROLE_USERS foreign key (USE_IDINT)
      references USERS (USE_IDINT)
      on delete restrict on update restrict;

alter table USER_ROLE
   add constraint FK_USER_ROL_USER_ROLE_ROLES foreign key (ROL_IDINT)
      references ROLES (ROL_IDINT)
      on delete restrict on update restrict;

