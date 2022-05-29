DROP TABLE IF EXISTS `role`;
	CREATE TABLE `role` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(32) DEFAULT NULL,
	`nameZh` varchar(32) DEFAULT NULL,
	PRIMARY KEY (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
	-- ----------------------------
	-- Records of role
	-- ----------------------------
	INSERT INTO `role` VALUES ('1', 'dba', '数据库管理员');
	INSERT INTO `role` VALUES ('2', 'admin', '系统管理员');
	INSERT INTO `role` VALUES ('3', 'user', '用户');
	-- ----------------------------
	-- Table structure for user
	-- ----------------------------
	DROP TABLE IF EXISTS `user`;
	CREATE TABLE `user` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`username` varchar(32) DEFAULT NULL,
	`password` varchar(255) DEFAULT NULL,
	`enabled` tinyint(1) DEFAULT NULL,
	`locked` tinyint(1) DEFAULT NULL,
	PRIMARY KEY (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
	-- ----------------------------
	-- Records of user
	-- ----------------------------
	INSERT INTO `user` VALUES ('1', 'root', '$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq', '1', '0');
	INSERT INTO `user` VALUES ('2', 'admin', '$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq', '1', '0');
	INSERT INTO `user` VALUES ('3', 'sang', '$2a$10$RMuFXGQ5AtH4wOvkUqyvuecpqUSeoxZYqilXzbz50dceRsga.WYiq', '1', '0');
	-- ----------------------------
	-- Table structure for user_role
	-- ----------------------------
	DROP TABLE IF EXISTS `user_role`;
	CREATE TABLE `user_role` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`uid` int(11) DEFAULT NULL,
	`rid` int(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
	-- ----------------------------
	-- Records of user_role
	-- ----------------------------
	INSERT INTO `user_role` VALUES ('1', '1', '1');
	INSERT INTO `user_role` VALUES ('2', '1', '2');
	INSERT INTO `user_role` VALUES ('3', '2', '2');
	INSERT INTO `user_role` VALUES ('4', '3', '3');



	CREATE TABLE `dept` (
      `id` bigint(20) NOT NULL,
      `dept_name` varchar(100) NOT NULL,
      `status` smallint(6) NOT NULL,
      `sort` int(11) NOT NULL,
      `describe` varchar(100) DEFAULT NULL,
      `parent_id` varchar(100) DEFAULT NULL,
      `level` int(11) NOT NULL,
      `create_id` bigint(20) NOT NULL,
      `create_time` timestamp NOT NULL,
      `update_id` bigint(20) NOT NULL,
      `update_time` timestamp NOT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci