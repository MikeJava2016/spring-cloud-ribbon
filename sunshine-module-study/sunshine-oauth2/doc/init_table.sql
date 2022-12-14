-- https://cloud.tencent.com/developer/article/1554411
CREATE SCHEMA IF NOT EXISTS `oauth2` DEFAULT CHARACTER SET utf8 ;
USE `oauth2` ;

-- -----------------------------------------------------
-- Table `oauth2`.`clientdetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`clientdetails` (
  `appId` VARCHAR(128) NOT NULL comment '客户段id',
  `resourceIds` VARCHAR(256) NULL DEFAULT NULL comment '资源id',
  `appSecret` VARCHAR(256) NULL DEFAULT NULL comment '',
  `scope` VARCHAR(256) NULL DEFAULT NULL,
  `grantTypes` VARCHAR(256) NULL DEFAULT NULL,
  `redirectUrl` VARCHAR(256) NULL DEFAULT NULL,
  `authorities` VARCHAR(256) NULL DEFAULT NULL,
  `access_token_validity` INT(11) NULL DEFAULT NULL,
  `refresh_token_validity` INT(11) NULL DEFAULT NULL,
  `additionalInformation` VARCHAR(4096) NULL DEFAULT NULL,
  `autoApproveScopes` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`appId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_access_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_access_token` (
  `token_id` VARCHAR(256) NULL DEFAULT NULL comment 'md5加密的access_token的值',
  `token` BLOB NULL DEFAULT NULL comment 'Oauth2AccessToken.java对象序列化后的二进制数据',
  `authentication_id` VARCHAR(128) NOT NULL comment 'md5加密过的username，clienti_id,scope',
  `user_name` VARCHAR(256) NULL DEFAULT NULL comment '登录的用户名',
  `client_id` VARCHAR(256) NULL DEFAULT NULL comment '客户端id',
  `authentication` BLOB NULL DEFAULT NULL comment 'Oauth2Authentication.java对象序列化后的二进制数据',
  `refresh_token` VARCHAR(256) NULL DEFAULT NULL ,
  PRIMARY KEY (`authentication_id`))
ENGINE = InnoDB comment '访问令牌'
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_approvals`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_approvals` (
  `userId` VARCHAR(256) NULL DEFAULT NULL,
  `clientId` VARCHAR(256) NULL DEFAULT NULL,
  `scope` VARCHAR(256) NULL DEFAULT NULL,
  `status` VARCHAR(10) NULL DEFAULT NULL,
  `expiresAt` DATETIME NULL DEFAULT NULL,
  `lastModifiedAt` DATETIME NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_client_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_client_details` (
  `client_id` VARCHAR(128) NOT NULL,
  `resource_ids` VARCHAR(256) NULL DEFAULT NULL,
  `client_secret` VARCHAR(256) NULL DEFAULT NULL,
  `scope` VARCHAR(256) NULL DEFAULT NULL,
  `authorized_grant_types` VARCHAR(256) NULL DEFAULT NULL,
  `web_server_redirect_uri` VARCHAR(256) NULL DEFAULT NULL,
  `authorities` VARCHAR(256) NULL DEFAULT NULL,
  `access_token_validity` INT(11) NULL DEFAULT NULL,
  `refresh_token_validity` INT(11) NULL DEFAULT NULL,
  `additional_information` VARCHAR(4096) NULL DEFAULT NULL,
  `autoapprove` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_client_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_client_token` (
  `token_id` VARCHAR(256) NULL DEFAULT NULL,
  `token` BLOB NULL DEFAULT NULL,
  `authentication_id` VARCHAR(128) NOT NULL,
  `user_name` VARCHAR(256) NULL DEFAULT NULL,
  `client_id` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_code` (
  `code` VARCHAR(256) NULL DEFAULT NULL,
  `authentication` BLOB NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `oatuh2`.`oauth_refresh_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth2`.`oauth_refresh_token` (
  `token_id` VARCHAR(256) NULL DEFAULT NULL,
  `token` BLOB NULL DEFAULT NULL,
  `authentication` BLOB NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;