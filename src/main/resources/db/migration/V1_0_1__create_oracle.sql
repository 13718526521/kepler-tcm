/**
 内置系统用户表
 */
CREATE TABLE SYS_USER
(
  ID                   VARCHAR2(32) DEFAULT SYS_GUID() NOT NULL,
  LOGIN_NAME           VARCHAR2(50) NOT NULL,
  PASSWORD             VARCHAR2(50) NOT NULL,
  REAL_NAME            VARCHAR2(50),
  ORG_ID               VARCHAR2(50),
  SEX                  NUMBER(1) DEFAULT 1,
  EMAIL                VARCHAR2(50),
  BIRTHDAY             VARCHAR2(25),
  AGE                  NUMBER(5),
  DEPT_ID              VARCHAR2(50),
  POSTAL_CODE          VARCHAR2(15),
  COUNTRY              VARCHAR2(100),
  PROVINCE             VARCHAR2(100),
  CITY                 VARCHAR2(100),
  MOBILE               VARCHAR2(20),
  CREATE_USER          VARCHAR2(50),
  CREATE_DATE          date default sysdate not null,
  STATUS               NUMBER(1) DEFAULT 0,
  IS_DELETED           NUMBER(1) DEFAULT 0,
  UPDATE_BY            VARCHAR2(50),
  IS_ADMIN             VARCHAR2(2) DEFAULT '0',
  IS_USABLE            NUMBER(8) DEFAULT 0,
  PASSWORD_HINT        VARCHAR2(100),
  PASSWORD_ERROR_TIMES VARCHAR2(2) DEFAULT '0',
  LAST_LOGIN_TIME      date default sysdate not null,
  PASSWORD_ERROR_LOCK  VARCHAR2(2) DEFAULT '0',
  PASSWORD_ERROR_COUNT NUMBER(5) DEFAULT 0,
  REMARK               VARCHAR2(500),
  LAST_LOGIN_FAIL_TIME  DATE,
  PRIMARY KEY (ID),
  UNIQUE (LOGIN_NAME)
)
;
COMMENT ON TABLE SYS_USER
  IS '系统用户基本信息';
COMMENT ON COLUMN SYS_USER.ID
  IS '主键唯一标识';
COMMENT ON COLUMN SYS_USER.LOGIN_NAME
  IS '登录用户名';
COMMENT ON COLUMN SYS_USER.PASSWORD
  IS '登录密码';
COMMENT ON COLUMN SYS_USER.REAL_NAME
  IS '真实用户名';
COMMENT ON COLUMN SYS_USER.ORG_ID
  IS '组织机构ID';
COMMENT ON COLUMN SYS_USER.SEX
  IS '用户性别；1、男；0、女；';
COMMENT ON COLUMN SYS_USER.EMAIL
  IS '用户邮箱';
COMMENT ON COLUMN SYS_USER.BIRTHDAY
  IS '生日';
COMMENT ON COLUMN SYS_USER.AGE
  IS '年龄';
COMMENT ON COLUMN SYS_USER.DEPT_ID
  IS '部门ID';
COMMENT ON COLUMN SYS_USER.POSTAL_CODE
  IS '邮政编码';
COMMENT ON COLUMN SYS_USER.COUNTRY
  IS '用户所属国家';
COMMENT ON COLUMN SYS_USER.PROVINCE
  IS '用户所属省份';
COMMENT ON COLUMN SYS_USER.CITY
  IS '所属城市';
COMMENT ON COLUMN SYS_USER.MOBILE
  IS '手机号';
COMMENT ON COLUMN SYS_USER.CREATE_USER
  IS '创建人';
COMMENT ON COLUMN SYS_USER.CREATE_DATE
  IS '创建时间';
COMMENT ON COLUMN SYS_USER.STATUS
  IS '用户状态';
COMMENT ON COLUMN SYS_USER.IS_DELETED
  IS '删除状态';
COMMENT ON COLUMN SYS_USER.UPDATE_BY
  IS '更新者';
COMMENT ON COLUMN SYS_USER.IS_ADMIN
  IS '是否管理，1是，0不是';
COMMENT ON COLUMN SYS_USER.IS_USABLE
  IS '设置用户是否可用  0启用   1禁用';
COMMENT ON COLUMN SYS_USER.PASSWORD_HINT
  IS '密码提示';
COMMENT ON COLUMN SYS_USER.PASSWORD_ERROR_TIMES
  IS '密码错误次数';
COMMENT ON COLUMN SYS_USER.LAST_LOGIN_TIME
  IS '最后一次登录时间';
COMMENT ON COLUMN SYS_USER.PASSWORD_ERROR_LOCK
  IS '密码被锁，0为正常，1为锁定';
COMMENT ON COLUMN SYS_USER.PASSWORD_ERROR_COUNT
  IS '记录密码输错次数';
COMMENT ON COLUMN SYS_USER.REMARK
  IS '备注信息';
COMMENT ON COLUMN SYS_USER.LAST_LOGIN_FAIL_TIME
  IS '用户最近一次登录失败的时间';


/**
 * 本地关系数据源信息表
 */
create table SEA_LOCAL_SOURCE 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   SOURCE_TYPE          varchar2(2),
   SOURCE_NAME          varchar2(50),
   DB_NAME              varchar2(50),
   DB_PWD               varchar2(50),
   MAXIDLE              varchar2(50) DEFAULT '0',
   MAX_ACTIVE           varchar2(50) DEFAULT '20',
   MAX_WAIT             varchar2(50) DEFAULT '-1',
   INITIAL_SIZE         varchar2(50) DEFAULT '2',
   MINIDLE              varchar2(50),
   SOURCE_URL           varchar2(1000),
   CREATE_USER          varchar2(50),
   CREATE_TIME          date default sysdate not null,
   STATUS               varchar2(2) DEFAULT '0',
   UPDATE_BY            varchar2(32),
   primary key (ID)
);

comment on table SEA_LOCAL_SOURCE is 
'本地数据源表';
comment on column SEA_LOCAL_SOURCE.ID is 
'主键id';
comment on column SEA_LOCAL_SOURCE.SOURCE_TYPE is 
'1：oracle、2：mysql、3：hive、4：xcloud、5：hbase、6：excel';
comment on column SEA_LOCAL_SOURCE.SOURCE_NAME is 
'数据源名称';
comment on column SEA_LOCAL_SOURCE.DB_NAME is 
'数据库用户名';
comment on column SEA_LOCAL_SOURCE.DB_PWD is 
'密码';
comment on column SEA_LOCAL_SOURCE.MAXIDLE is 
'最大空闲数 默认是0 无限制';
comment on column SEA_LOCAL_SOURCE.MAX_ACTIVE is 
'最大连接数 默认0无限制';
comment on column SEA_LOCAL_SOURCE.MAX_WAIT is 
'最大等待时间 默认-1 无限制';
comment on column SEA_LOCAL_SOURCE.INITIAL_SIZE is 
'初始化数量 默认是10 最大不能超过50';
comment on column SEA_LOCAL_SOURCE.MINIDLE is 
'最小空闲数';
comment on column SEA_LOCAL_SOURCE.SOURCE_URL is 
'数据源链接字符串';
comment on column SEA_LOCAL_SOURCE.CREATE_USER is 
'创建人';
comment on column SEA_LOCAL_SOURCE.CREATE_TIME is 
'创建时间用字符串类型，时间选择当前时间';
comment on column SEA_LOCAL_SOURCE.STATUS is 
'0:启用、1禁用';
comment on column SEA_LOCAL_SOURCE.UPDATE_BY is 
'修改人';

/**
 * 远程接口数据源表
 */
create table SEA_REMOTE_SOURCE 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   REMOTE_NAME          varchar2(50),
   REMOTE_CODE          varchar2(50),
   REQUEST_TYPE         varchar2(16),
   DATA_STYLE           varchar2(16),
   SOURCE_STYLE         varchar2(2),
   REMOTE_URL           varchar2(500),
   PARAMS               varchar2(500),
   CREATE_USER          varchar2(32),
   CREATE_TIME          date default sysdate not null,
   UPDATE_BY            varchar2(32),
   primary key (ID)
);
comment on table SEA_REMOTE_SOURCE is 
'远程数据源表';
comment on column SEA_REMOTE_SOURCE.ID is 
'主键id 用数据库生成序列uuid';
comment on column SEA_REMOTE_SOURCE.REMOTE_NAME is 
'接口名称';
comment on column SEA_REMOTE_SOURCE.REMOTE_CODE is 
'唯一标识';
comment on column SEA_REMOTE_SOURCE.REQUEST_TYPE is 
'接口请求类型POST/GET 1：get 、2：post ';
comment on column SEA_REMOTE_SOURCE.DATA_STYLE is 
'数据风格XML/JSON 1：xml 、2：json';
comment on column SEA_REMOTE_SOURCE.SOURCE_STYLE is 
'数据源风格XML/JSON 1：xml 、2：json';
comment on column SEA_REMOTE_SOURCE.REMOTE_URL is 
'远程接口路径';
comment on column SEA_REMOTE_SOURCE.PARAMS is 
'请求参数';
comment on column SEA_REMOTE_SOURCE.CREATE_USER is 
'创建人';
comment on column SEA_REMOTE_SOURCE.CREATE_TIME is 
'创建时间';
comment on column SEA_REMOTE_SOURCE.UPDATE_BY is 
'修改人';

/**
 * 接口信息表
 */
create table SEA_INTERFACE_INFO 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   INTERFACE_NAME       varchar2(100),
   INTERFACE_CODE       varchar2(50),
   DATA_STYLE           varchar2(8),
   STATUS               varchar2(2),
   CACHE_TYPE           varchar2(2),
   REMARK               varchar2(500),
   PARAMS               varchar2(1024),
   SOURCE_TYPE          varchar2(2),
   CREATE_TIME          date default sysdate not null,
   CREATE_USER          varchar2(50),
   UPDATE_BY            varchar2(50),
   primary key (ID)
);

comment on table SEA_INTERFACE_INFO is 
'接口基本信息表';
comment on column SEA_INTERFACE_INFO.ID is 
'主键id';
comment on column SEA_INTERFACE_INFO.INTERFACE_NAME is 
'接口名称';
comment on column SEA_INTERFACE_INFO.DATA_STYLE is 
'XML/JSON';
comment on column SEA_INTERFACE_INFO.STATUS is 
'1：启用、2：禁用';
comment on column SEA_INTERFACE_INFO.CACHE_TYPE is 
'0;不缓存、1;一分钟、2;一小时、3;一天';
comment on column SEA_INTERFACE_INFO.REMARK is 
'接口描述信息';
comment on column SEA_INTERFACE_INFO.PARAMS is 
'接口预览参数';
comment on column SEA_INTERFACE_INFO.SOURCE_TYPE is 
'接口数据源类型 (保留此字段并未做处理)';
comment on column SEA_INTERFACE_INFO.CREATE_TIME is 
'创建时间';
comment on column SEA_INTERFACE_INFO.CREATE_USER is 
'创建用户';
comment on column SEA_INTERFACE_INFO.UPDATE_BY is 
'修改人';

/**
 * 客户基本信息表
 */
create table SEA_CUSTOM 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   LOGIN_ID             varchar2(50),
   NICK_NAME            varchar2(50),
   LOGIN_NAME           varchar2(50),
   PASSWORD             varchar2(32),
   USER_TYPE            varchar2(2),
   REAL_NAME            VARCHAR2(50),
   ORG_ID               VARCHAR2(50),
   SEX                  NUMBER(1) DEFAULT 1,
   EMAIL                VARCHAR2(50),
   BIRTHDAY             VARCHAR2(25),
   AGE                  NUMBER(5),
   DEPT_ID              VARCHAR2(50),
   POSTAL_CODE          VARCHAR2(15),
   COUNTRY              VARCHAR2(100),
   PROVINCE             VARCHAR2(100),
   CITY                 VARCHAR2(100),
   LNG                  VARCHAR2(50),
   LAT                  VARCHAR2(50),
   QQ_NO                VARCHAR2(20),
   CARD_ID              VARCHAR2(32),
   STATUS               NUMBER(1) DEFAULT 0,
   MOBILE               VARCHAR2(20),
   CONTACT_NAME         varchar2(32),
   ISCLOSE              varchar2(2) DEFAULT '0',
   MAX_NUM              varchar2(8),
   CONCURRENT_NUM       varchar2(8),
   START_TIME			varchar2(50),
   END_TIME				varchar2(50),
   CREATE_USER          varchar2(50),
   CREATE_TIME          date default sysdate not null,
   IS_DELETED           NUMBER(1)  DEFAULT 1,
   UPDATE_BY            VARCHAR2(50),
   IS_CUSTOM            NUMBER(1)  DEFAULT 0,
   IS_ADMIN             VARCHAR2(2) DEFAULT '0',
   PASSWORD_HINT        VARCHAR2(100),
   PASSWORD_ERROR_TIMES VARCHAR2(2) DEFAULT '0',
   LAST_LOGIN_TIME      date default sysdate not null,
   PASSWORD_ERROR_LOCK  VARCHAR2(2) DEFAULT '0',
   PASSWORD_ERROR_COUNT NUMBER(5) DEFAULT 0,
   REMARK               VARCHAR2(500),
   primary key (ID)
);

comment on table SEA_CUSTOM is 
'客户中心配置表';
comment on column SEA_CUSTOM.ID is 
'主键id';
comment on column SEA_CUSTOM.LOGIN_ID is 
'登录名';
comment on column SEA_CUSTOM.NICK_NAME is 
'用户昵称';
comment on column SEA_CUSTOM.LOGIN_NAME is 
'组织机构名称';
comment on column SEA_CUSTOM.PASSWORD is 
'密码';
comment on column SEA_CUSTOM.USER_TYPE is 
'用户类型，0.企业用户1.个人用户';
COMMENT ON COLUMN SEA_CUSTOM.REAL_NAME
  IS '真实用户名';
COMMENT ON COLUMN SEA_CUSTOM.ORG_ID
  IS '组织机构ID';
COMMENT ON COLUMN SEA_CUSTOM.SEX
  IS '用户性别；1、男；0、女；';
COMMENT ON COLUMN SEA_CUSTOM.EMAIL
  IS '用户邮箱';
COMMENT ON COLUMN SEA_CUSTOM.BIRTHDAY
  IS '生日';
COMMENT ON COLUMN SEA_CUSTOM.AGE
  IS '年龄';
COMMENT ON COLUMN SEA_CUSTOM.DEPT_ID
  IS '部门ID';
COMMENT ON COLUMN SEA_CUSTOM.POSTAL_CODE
  IS '邮政编码';
COMMENT ON COLUMN SEA_CUSTOM.COUNTRY
  IS '用户所属国家';
COMMENT ON COLUMN SEA_CUSTOM.PROVINCE
  IS '用户所属省份';
COMMENT ON COLUMN SEA_CUSTOM.CITY
  IS '所属城市';
COMMENT ON COLUMN SEA_CUSTOM.LNG
  IS '经度';
COMMENT ON COLUMN SEA_CUSTOM.LAT
  IS '纬度';
COMMENT ON COLUMN SEA_CUSTOM.QQ_NO
  IS 'QQ';
COMMENT ON COLUMN SEA_CUSTOM.CARD_ID
  IS '身份证号';
comment on column SEA_CUSTOM.STATUS is 
'状态，0.未审核1.审核通过 默认未审核';
comment on column SEA_CUSTOM.MOBILE is 
'联系人电话';
comment on column SEA_CUSTOM.CONTACT_NAME is 
'联系人名称';
comment on column SEA_CUSTOM.ISCLOSE is 
'是否启用 0.启用1.禁用  默认启用';
comment on column SEA_CUSTOM.MAX_NUM is 
'访问接口的封顶次数';
comment on column SEA_CUSTOM.CONCURRENT_NUM is 
'接口访问的每秒并发数';
comment on column SEA_CUSTOM.START_TIME is 
'开始时间';
comment on column SEA_CUSTOM.END_TIME is 
'结束时间';
comment on column SEA_CUSTOM.CREATE_USER is 
'创建人';
comment on column SEA_CUSTOM.CREATE_TIME is 
'创建时间';
COMMENT ON COLUMN SEA_CUSTOM.IS_DELETED
  IS '删除状态';
comment on column SEA_CUSTOM.UPDATE_BY is 
'修改人';
comment on column SEA_CUSTOM.IS_CUSTOM is 
'是否为客户登录 0 是  1 否';
COMMENT ON COLUMN SEA_CUSTOM.IS_ADMIN
  IS '是否管理，1是，0不是';
COMMENT ON COLUMN SEA_CUSTOM.PASSWORD_HINT
  IS '密码提示';
COMMENT ON COLUMN SEA_CUSTOM.PASSWORD_ERROR_TIMES
  IS '密码错误次数';
COMMENT ON COLUMN SEA_CUSTOM.LAST_LOGIN_TIME
  IS '最后一次登录时间';
COMMENT ON COLUMN SEA_CUSTOM.PASSWORD_ERROR_LOCK
  IS '密码被锁，0为正常，1为锁定';
COMMENT ON COLUMN SEA_CUSTOM.PASSWORD_ERROR_COUNT
  IS '记录密码输错次数';
COMMENT ON COLUMN SEA_CUSTOM.REMARK
  IS '备注信息';

/**
 * 接口sql节点基本信息表
 */
create table SEA_INTERFACE_SQL 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   PID                  varchar2(32),
   INTERFACE_ID         varchar2(32),
   SQL_NAME             varchar2(50),
   ATTR_NAME            varchar2(32),
   ATTR_VALUE           varchar2(512),
   QUERY_INFO           varchar2(255),
   DATA_TYPE            varchar2(2),
   SOURCE_ID            varchar2(32),
   SQL_SCRIPT           varchar2(1024),
   SQL_REMARK           varchar2(512),
   SQL_ORD              varchar2(2),
   SQL_TYPE             varchar2(2),
   CREATE_USER          varchar2(50),
   CREATE_TIME          date default sysdate not null,
   UPDATE_BY            varchar2(50),
   primary key (ID)
);

comment on table SEA_INTERFACE_SQL is 
'接口数据sql配置表';
comment on column SEA_INTERFACE_SQL.ID is 
'主键id';
comment on column SEA_INTERFACE_SQL.PID is 
'父id';
comment on column SEA_INTERFACE_SQL.INTERFACE_ID is 
'接口主键id';
comment on column SEA_INTERFACE_SQL.SQL_NAME is 
'属性名称';
comment on column SEA_INTERFACE_SQL.ATTR_NAME is 
'属性标识';
comment on column SEA_INTERFACE_SQL.ATTR_VALUE is 
'属性值';
comment on column SEA_INTERFACE_SQL.QUERY_INFO is 
'配置节点的描述信息';
comment on column SEA_INTERFACE_SQL.DATA_TYPE is 
'接口返回的数据类型1：json数组、2：json对象、3：单个结果集、4：空对象';
comment on column SEA_INTERFACE_SQL.SOURCE_ID is 
'数据源主键';
comment on column SEA_INTERFACE_SQL.SQL_SCRIPT is 
'查询配置的sql语句';
comment on column SEA_INTERFACE_SQL.SQL_REMARK is 
'sql语句的描述';
comment on column SEA_INTERFACE_SQL.SQL_ORD is 
'多个结果集配置返回结果的顺序';
comment on column SEA_INTERFACE_SQL.SQL_TYPE is 
'0：使用查询脚本、1：属性值';
comment on column SEA_INTERFACE_SQL.CREATE_USER is 
'创建人';
comment on column SEA_INTERFACE_SQL.CREATE_TIME is 
'创建时间';
comment on column SEA_INTERFACE_SQL.UPDATE_BY is 
'修改人';

/**
 * 系统动态参数配置表
 */
create table SYS_CONFIG 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   CONFIG_NAME          varchar2(50),
   CONFIG_TYPE          varchar2(2),
   CONFIG_KEY           varchar2(1024) not null,
   CONFIG_VALUE         varchar2(1024),
   CREATE_TIME          date default sysdate not null,
   CREATE_USER          varchar2(50),
   UPDATE_BY            varchar2(50),
   primary key (ID)
);
comment on table SYS_CONFIG is 
'系统的动态配置表';
comment on column SYS_CONFIG.ID is 
'主键id';
comment on column SYS_CONFIG.CONFIG_NAME is 
'站点名称';
comment on column SYS_CONFIG.CONFIG_TYPE is 
'0：静态配置、1：动态配置';
comment on column SYS_CONFIG.CONFIG_KEY is 
'key';
comment on column SYS_CONFIG.CONFIG_VALUE is 
'value';
comment on column SYS_CONFIG.CREATE_TIME is 
'创建时间';
comment on column SYS_CONFIG.CREATE_USER is 
'创建用户';
comment on column SYS_CONFIG.UPDATE_BY is 
'修改人';

/**
 * 远程接口有效字段配置表
 */
create table SEA_REMOTE_FIELD_SET 
(
   ID                   varchar2(32) DEFAULT SYS_GUID() not null,
   REMOTE_ID            varchar2(32),
   FIELD_NAME           varchar2(50),
   FIELD_CODE           varchar2(50),
   FIELD_TYPE           varchar2(16),
   REMARK               varchar2(500),
   CREATE_USER          varchar2(50),
   CREATE_TIME          date default sysdate not null,
   UPDATE_BY            varchar2(32),
   primary key (ID)
);
comment on table SEA_REMOTE_FIELD_SET is 
'远程数据源字段配置的表';
comment on column SEA_REMOTE_FIELD_SET.ID is 
'主键id 用数据库生成序列uuid';
comment on column SEA_REMOTE_FIELD_SET.REMOTE_ID is 
'远程接口id';
comment on column SEA_REMOTE_FIELD_SET.FIELD_NAME is 
'字段名称';
comment on column SEA_REMOTE_FIELD_SET.FIELD_CODE is 
'字段编码';
comment on column SEA_REMOTE_FIELD_SET.FIELD_TYPE is 
'字段类型，例''string''';
comment on column SEA_REMOTE_FIELD_SET.REMARK is 
'字段描述';
comment on column SEA_REMOTE_FIELD_SET.CREATE_USER is 
'创建人';
comment on column SEA_REMOTE_FIELD_SET.CREATE_TIME is 
'创建时间';
comment on column SEA_REMOTE_FIELD_SET.UPDATE_BY is 
'修改人';

/**
 * 客户应用基本信息表
 */
CREATE TABLE SEA_APP_INFO
(
  ID           VARCHAR2(32)  DEFAULT SYS_GUID() NOT NULL,
  COUTOM_ID    VARCHAR2(32)  NOT NULL,
  PUBLIC_ID    VARCHAR2(32)  NOT NULL,
  APP_ID       VARCHAR2(32) NOT NULL,
  APP_SECRET   VARCHAR2(128)  NOT NULL,
  APP_NAME	   VARCHAR2(50),
  ACCESS_TOKEN VARCHAR2(128),
  APP_TYPE     NUMBER(1),
  IP_LIST	   VARCHAR2(255),
  CREATE_TIME  date default sysdate not null,
  EFFECT_TIME  DATE,
  EXPIRES      NUMBER(20),
  IS_ENABLE    NUMBER(1)   DEFAULT 1 ,
  CREATE_USER  VARCHAR2(50),
  REMARK      VARCHAR2(500),
  REQUEST_CHECK  VARCHAR2(8),
  PRIMARY KEY (ID)
)

;
COMMENT ON TABLE SEA_APP_INFO
  IS 'OAUTH2认证信息表';
COMMENT ON COLUMN SEA_APP_INFO.ID
  IS '主键唯一标识';
COMMENT ON COLUMN SEA_APP_INFO.COUTOM_ID
  IS '客户关联外键';
COMMENT ON COLUMN SEA_APP_INFO.PUBLIC_ID
  IS '对外公开，应用唯一标识';
COMMENT ON COLUMN SEA_APP_INFO.APP_ID
  IS '应用唯一标识';
COMMENT ON COLUMN SEA_APP_INFO.APP_SECRET
  IS '应用密钥';
COMMENT ON COLUMN SEA_APP_INFO.APP_NAME
  IS '应用名称';
COMMENT ON COLUMN SEA_APP_INFO.ACCESS_TOKEN
  IS '应用接口调用票据信息access_token';
COMMENT ON COLUMN SEA_APP_INFO.APP_TYPE
  IS '应用类型';
COMMENT ON COLUMN SEA_APP_INFO.IP_LIST
  IS 'IP白名单';
COMMENT ON COLUMN SEA_APP_INFO.CREATE_TIME
  IS '创建时间';
COMMENT ON COLUMN SEA_APP_INFO.EFFECT_TIME
  IS '生效时间';
COMMENT ON COLUMN SEA_APP_INFO.EXPIRES
  IS '过期时间，当前时间-生效时间(EFFECT_TIME)  < 过期时间(EXPIRES) ,ACCESS_TOKEN有效';
COMMENT ON COLUMN SEA_APP_INFO.IS_ENABLE
  IS '数据是否可用，如果采用更新模式，此字段可以忽略，默认：1';
COMMENT ON COLUMN SEA_APP_INFO.CREATE_USER
  IS '创建人';
COMMENT ON COLUMN SEA_APP_INFO.REMARK
  IS '备注信息';
COMMENT ON COLUMN SEA_APP_INFO.REQUEST_CHECK
  IS '请求校验方式';

/**
 * 接口组服务基本信息表
 */
CREATE TABLE SEA_SERVER_INFO
(
  ID               VARCHAR2(32)  DEFAULT SYS_GUID() NOT NULL,
  SERVER_NAME	   VARCHAR2(50),
  SERVER_TYPE      NUMBER(1)   DEFAULT 0,
  UPPER_LIMIT      NUMBER(10)  DEFAULT 0,
  IS_ENABLE	       NUMBER(1)   DEFAULT 1 ,
  CREATE_TIME      date default sysdate not null,
  CREATE_USER      VARCHAR2(50),
  REMARK      VARCHAR2(500),
  PRIMARY KEY (ID)
)
;
COMMENT ON TABLE SEA_SERVER_INFO
  IS '接口服务信息表';
COMMENT ON COLUMN SEA_SERVER_INFO.ID
  IS '主键唯一标识';
COMMENT ON COLUMN SEA_SERVER_INFO.SERVER_NAME
  IS '应用名称';
COMMENT ON COLUMN SEA_SERVER_INFO.SERVER_TYPE
  IS '服务类型';
COMMENT ON COLUMN SEA_SERVER_INFO.UPPER_LIMIT
  IS '配置上限次数';
COMMENT ON COLUMN SEA_SERVER_INFO.IS_ENABLE
  IS '服务是否可用，默认：1、可用；0 、不可用';
COMMENT ON COLUMN SEA_SERVER_INFO.CREATE_TIME
  IS '创建时间';
COMMENT ON COLUMN SEA_SERVER_INFO.CREATE_USER
  IS '创建人';
COMMENT ON COLUMN SEA_SERVER_INFO.REMARK
  IS '备注信息';
  
/**
 * 应用服务关系表
 */
CREATE TABLE SEA_APP_SERVER
(
  APP_ID            VARCHAR2(32) NOT NULL,
  SERVER_ID	        VARCHAR2(32) NOT NULL
)
;
COMMENT ON TABLE SEA_APP_SERVER
  IS '应用与服务关系表';
COMMENT ON COLUMN SEA_APP_SERVER.APP_ID
  IS '应用外键唯一标识';
COMMENT ON COLUMN SEA_APP_SERVER.SERVER_ID
  IS '服务外键唯一标识';

/**
 * 服务接口关系表
 */
CREATE TABLE SEA_SERVER_INTERFACE
(
  INTERFACE_ID            VARCHAR2(32) NOT NULL,
  SERVER_ID	        VARCHAR2(32) NOT NULL
)
;
COMMENT ON TABLE SEA_SERVER_INTERFACE
  IS '接口与服务关系表';
COMMENT ON COLUMN SEA_SERVER_INTERFACE.INTERFACE_ID
  IS '接口外键唯一标识';
COMMENT ON COLUMN SEA_SERVER_INTERFACE.SERVER_ID
  IS '服务外键唯一标识';
/**
 * 应用client
 */
create table SYS_CLIENT
(
  id            VARCHAR2(32) not null,
  client_id     VARCHAR2(255) not null,
  client_secret VARCHAR2(255) not null,
  client_name   VARCHAR2(255) not null,
  PRIMARY KEY (id)
)




  
  
  
  
  
  
  
  