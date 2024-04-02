/*
 Navicat Premium Data Transfer

 Source Server         : 私有
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 47.103.156.64:3307
 Source Schema         : instabook

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 02/04/2024 22:17:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `file_id` bigint(20) NOT NULL COMMENT 'key',
  `channel` int(11) NULL DEFAULT NULL COMMENT '1:head_img 2:message',
  `url` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `path` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'path',
  `outer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'outer id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `message_id` bigint(20) NOT NULL COMMENT 'key',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user name',
  `user_head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user head img',
  `another_user_id` bigint(20) NULL DEFAULT NULL COMMENT 'another one',
  `another_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'another name',
  `another_user_head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'another one head img',
  `chat_id` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'mark a chat',
  `message` json NULL COMMENT 'message object',
  `type` int(11) NULL DEFAULT 0 COMMENT '0:text 1:img 2:video 3:file',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
  `del_flag` int(11) NULL DEFAULT 0 COMMENT 'user del it',
  `another_del_flag` int(11) NULL DEFAULT 0 COMMENT 'another one del it',
  `request_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'request id, for data synchronization in different endpoints or result reply(timeout, not friend, blocked etc.)',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `com_date`(`create_time`, `del_flag`, `chat_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `news_id` bigint(20) NOT NULL COMMENT 'key',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user_name',
  `head_img` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'head img url',
  `photos` json NULL COMMENT 'photo urls',
  `message` varchar(8191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'messages',
  `like_num` int(11) NULL DEFAULT NULL COMMENT 'like num',
  `unlike_num` int(11) NULL DEFAULT NULL COMMENT 'unlike num',
  `like_users_meta_info` json NULL COMMENT 'like users',
  `unlike_users_meta_info` json NULL COMMENT 'unlike users',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `version` bigint(20) NULL DEFAULT 0 COMMENT 'version',
  PRIMARY KEY (`news_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news_comment
-- ----------------------------
DROP TABLE IF EXISTS `news_comment`;
CREATE TABLE `news_comment`  (
  `news_comment_id` bigint(20) NOT NULL COMMENT 'key',
  `comment` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'comment',
  `news_id` bigint(20) NULL DEFAULT NULL COMMENT 'news_id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'comment user id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'user name',
  `reply_user_id` bigint(20) NULL DEFAULT NULL COMMENT 'reply for one\'s user id',
  `reply_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'one\'s name',
  `reply_comment_id` bigint(20) NULL DEFAULT NULL COMMENT 'reply comment id',
  `reply_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'reply comment',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
  PRIMARY KEY (`news_comment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL COMMENT 'primary_key',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'name',
  `head_img` varchar(511) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'head img',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'password_md5_salt',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'encrypted password',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_application
-- ----------------------------
DROP TABLE IF EXISTS `user_application`;
CREATE TABLE `user_application`  (
  `application_id` bigint(20) NOT NULL COMMENT 'key',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user id',
  `another_user_id` bigint(20) NULL DEFAULT NULL COMMENT 'another one',
  `status` int(11) NULL DEFAULT NULL COMMENT '0:apply 1:pass -1:disapprove',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'update time',
  PRIMARY KEY (`application_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_relationship
-- ----------------------------
DROP TABLE IF EXISTS `user_relationship`;
CREATE TABLE `user_relationship`  (
  `user_relationship_id` bigint(20) NOT NULL COMMENT 'primary key',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT 'user id',
  `another_user_id` bigint(20) NULL DEFAULT NULL COMMENT 'another one',
  `relation_status` int(11) NULL DEFAULT 0 COMMENT '0:normal;-1:block',
  `friend_status` int(11) NULL DEFAULT 0 COMMENT '0:false 1:true',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  PRIMARY KEY (`user_relationship_id`) USING BTREE,
  UNIQUE INDEX `uk_relation`(`user_id`, `another_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
