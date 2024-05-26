/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : bili

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 12/05/2023 13:08:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `aid` bigint NOT NULL COMMENT '视频aid',
  `videos` int NULL DEFAULT NULL COMMENT '视频分P总数，默认为1',
  `tid` int NULL DEFAULT NULL COMMENT '分区tid',
  `tname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子分区名称',
  `copyright` int NULL DEFAULT NULL,
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频封面图片url',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频标题',
  `pubdate` bigint NULL DEFAULT NULL COMMENT '稿件发布时间',
  `ctime` bigint NULL DEFAULT NULL,
  `description` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '视频简介',
  `state` int NULL DEFAULT NULL,
  `duration` int NULL DEFAULT NULL COMMENT '视频总计持续时长（所有分P）',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'json存储 Owner对象',
  `dynamic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cid` bigint NULL DEFAULT NULL COMMENT '视频1P cid',
  `short_link_v2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'bv号短链接',
  `up_from_v2` int NULL DEFAULT NULL,
  `first_frame` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pub_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `favorite` tinyint(1) NULL DEFAULT NULL COMMENT '是否已收藏',
  `type` int NULL DEFAULT NULL COMMENT '视频属性 3普通视频 4剧集 5课程',
  `sub_type` int NULL DEFAULT NULL COMMENT '附视频属性',
  `device` int NULL DEFAULT NULL COMMENT '观看平台代码',
  `page` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'json存储 Page对象',
  `count` int NULL DEFAULT NULL COMMENT '分P数',
  `progress` int NULL DEFAULT NULL COMMENT '观看进度',
  `view_at` bigint NULL DEFAULT NULL COMMENT '观看时间',
  `kid` bigint NULL DEFAULT NULL COMMENT '稿件avid',
  `business` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `redirect_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向url',
  `bvid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '稿件bvid',
  PRIMARY KEY (`aid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
