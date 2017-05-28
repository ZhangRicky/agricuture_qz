/*
Navicat MySQL Data Transfer

Source Server         : qingzhen
Source Server Version : 50628
Source Host           : 120.25.158.69:3306
Source Database       : agriculture

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2016-06-21 17:34:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` bigint(20) DEFAULT NULL,
  `area_level` varchar(255) DEFAULT NULL,
  `area_name` varchar(255) DEFAULT NULL,
  `in_use` bit(1) DEFAULT NULL,
  `parent_code` varchar(255) DEFAULT NULL,
  `area` tinyblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=374 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for checks
-- ----------------------------
DROP TABLE IF EXISTS `checks`;
CREATE TABLE `checks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_name` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `user` bigint(20) DEFAULT NULL,
  `sub_project` bigint(20) DEFAULT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  `is_success` int(11) DEFAULT NULL,
  `projects` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mlpwqw2qoj40h8j5pvu2i759o` (`user`),
  KEY `FK_5ejvl7847eaewrptoccucm41y` (`sub_project`),
  KEY `FK_5cbqjp8jvblthnfithy` (`projects`),
  CONSTRAINT `FK_5cbqjp8jvblthnfithy` FOREIGN KEY (`projects`) REFERENCES `projects` (`id`),
  CONSTRAINT `FK_5ejvl7847eaewrptoccucm41y` FOREIGN KEY (`sub_project`) REFERENCES `sub_project` (`id`),
  CONSTRAINT `FK_mlpwqw2qoj40h8j5pvu2i759o` FOREIGN KEY (`user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for constant
-- ----------------------------
DROP TABLE IF EXISTS `constant`;
CREATE TABLE `constant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descriptions` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for constants
-- ----------------------------
DROP TABLE IF EXISTS `constants`;
CREATE TABLE `constants` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `last_update_time` date DEFAULT NULL,
  `last_update_user` varchar(255) DEFAULT NULL,
  `order_by` bigint(20) DEFAULT NULL,
  `parent_code` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dept_code` varchar(255) DEFAULT NULL,
  `dept_level` int(11) NOT NULL,
  `dept_name` varchar(255) DEFAULT NULL,
  `flag` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `parent_code` bigint(20) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for farmer
-- ----------------------------
DROP TABLE IF EXISTS `farmer`;
CREATE TABLE `farmer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `farmer_name` varchar(255) DEFAULT NULL,
  `farmer_number` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `farmer_import` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rr98s530xhhd07ymm4fp6arfv` (`farmer_import`),
  CONSTRAINT `FK_rr98s530xhhd07ymm4fp6arfv` FOREIGN KEY (`farmer_import`) REFERENCES `farmer_import` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=787 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for farmer_import
-- ----------------------------
DROP TABLE IF EXISTS `farmer_import`;
CREATE TABLE `farmer_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_count` int(11) NOT NULL,
  `fail_count` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `handle_date` datetime DEFAULT NULL,
  `handle_status` int(11) NOT NULL,
  `import_date` datetime DEFAULT NULL,
  `import_status` int(11) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_update_date` datetime DEFAULT NULL,
  `success_count` int(11) NOT NULL,
  `update_count` int(11) NOT NULL,
  `operator` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9tysgd2k40vo99e9jyi6av8sa` (`operator`),
  CONSTRAINT `FK_9tysgd2k40vo99e9jyi6av8sa` FOREIGN KEY (`operator`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file_upload
-- ----------------------------
DROP TABLE IF EXISTS `file_upload`;
CREATE TABLE `file_upload` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_count` int(11) NOT NULL,
  `fail_count` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `handle_date` datetime DEFAULT NULL,
  `handle_status` int(11) NOT NULL,
  `import_date` datetime DEFAULT NULL,
  `import_status` int(11) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_update_date` datetime DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `success_count` int(11) NOT NULL,
  `update_count` int(11) NOT NULL,
  `operator` bigint(20) DEFAULT NULL,
  `project` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_55rfmmr27h7qlsrnw4pohetyn` (`operator`),
  KEY `FK_sjjbtucp9aiod9uprcg08kh4f` (`project`),
  CONSTRAINT `FK_55rfmmr27h7qlsrnw4pohetyn` FOREIGN KEY (`operator`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_sjjbtucp9aiod9uprcg08kh4f` FOREIGN KEY (`project`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `sub_porject` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iuf9eg8xwvdx0t9n58pp7tw4b` (`sub_porject`),
  CONSTRAINT `FK_iuf9eg8xwvdx0t9n58pp7tw4b` FOREIGN KEY (`sub_porject`) REFERENCES `sub_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for finance
-- ----------------------------
DROP TABLE IF EXISTS `finance`;
CREATE TABLE `finance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` double DEFAULT NULL,
  `appropriation` double DEFAULT NULL,
  `bk_date` varchar(255) DEFAULT NULL,
  `bk_user` varchar(255) DEFAULT NULL,
  `bz_date` varchar(255) DEFAULT NULL,
  `bz_user` varchar(255) DEFAULT NULL,
  `certificate_num` varchar(255) DEFAULT NULL,
  `projects_id` bigint(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow
-- ----------------------------
DROP TABLE IF EXISTS `flow`;
CREATE TABLE `flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `flow_name` varchar(255) DEFAULT NULL,
  `is_in_use` bit(1) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  `flow_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6mesg9vh4eyusedwarcalcn36` (`flow_type`),
  CONSTRAINT `FK_6mesg9vh4eyusedwarcalcn36` FOREIGN KEY (`flow_type`) REFERENCES `flow_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_node
-- ----------------------------
DROP TABLE IF EXISTS `flow_node`;
CREATE TABLE `flow_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `flag` int(11) NOT NULL,
  `indexs` int(11) NOT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  `flow` bigint(20) DEFAULT NULL,
  `roles` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8eenydkab5gss477meljc2re3` (`roles`),
  KEY `FK_3uby6pdv26145uqnv69nno9xy` (`flow`),
  CONSTRAINT `FK_3uby6pdv26145uqnv69nno9xy` FOREIGN KEY (`flow`) REFERENCES `flow` (`id`),
  CONSTRAINT `FK_8eenydkab5gss477meljc2re3` FOREIGN KEY (`roles`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_type
-- ----------------------------
DROP TABLE IF EXISTS `flow_type`;
CREATE TABLE `flow_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_desc` varchar(255) DEFAULT NULL,
  `type_key` int(11) NOT NULL,
  `type_namecn` varchar(255) DEFAULT NULL,
  `type_nameen` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for icon
-- ----------------------------
DROP TABLE IF EXISTS `icon`;
CREATE TABLE `icon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `point` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rp9mxke0yt55pdiugahj8ok9f` (`point`),
  CONSTRAINT `FK_rp9mxke0yt55pdiugahj8ok9f` FOREIGN KEY (`point`) REFERENCES `point` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for marker
-- ----------------------------
DROP TABLE IF EXISTS `marker`;
CREATE TABLE `marker` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `offset` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `icon` bigint(20) DEFAULT NULL,
  `label` bigint(20) DEFAULT NULL,
  `point` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pt3vnjehn5n9kigjexklayw7r` (`icon`),
  KEY `FK_k29u2ob69asb3onlxfam82vcp` (`label`),
  KEY `FK_tr5hso5vmuy67p5yay459e7qs` (`point`),
  CONSTRAINT `FK_k29u2ob69asb3onlxfam82vcp` FOREIGN KEY (`label`) REFERENCES `label` (`id`),
  CONSTRAINT `FK_pt3vnjehn5n9kigjexklayw7r` FOREIGN KEY (`icon`) REFERENCES `icon` (`id`),
  CONSTRAINT `FK_tr5hso5vmuy67p5yay459e7qs` FOREIGN KEY (`point`) REFERENCES `point` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for picture_upload
-- ----------------------------
DROP TABLE IF EXISTS `picture_upload`;
CREATE TABLE `picture_upload` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_count` int(11) NOT NULL,
  `fail_count` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `handle_date` datetime DEFAULT NULL,
  `handle_status` int(11) NOT NULL,
  `import_date` datetime DEFAULT NULL,
  `import_status` int(11) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_update_date` datetime DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `success_count` int(11) NOT NULL,
  `update_count` int(11) NOT NULL,
  `operator` bigint(20) DEFAULT NULL,
  `project` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k0nqhloxps0cn5a10ca8kjvjj` (`operator`),
  KEY `FK_7yo5y1hngylqj1o6x6irvgdak` (`project`),
  CONSTRAINT `FK_7yo5y1hngylqj1o6x6irvgdak` FOREIGN KEY (`project`) REFERENCES `project` (`id`),
  CONSTRAINT `FK_k0nqhloxps0cn5a10ca8kjvjj` FOREIGN KEY (`operator`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for point
-- ----------------------------
DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `checks` bigint(20) DEFAULT NULL,
  `projects` bigint(20) DEFAULT NULL,
  `range_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6a5cd05vpnxrwb7bfnqxhh25f` (`checks`),
  KEY `fk_point_porjects` (`projects`),
  CONSTRAINT `FK_6a5cd05vpnxrwb7bfnqxhh25f` FOREIGN KEY (`checks`) REFERENCES `checks` (`id`),
  CONSTRAINT `fk_point_porjects` FOREIGN KEY (`projects`) REFERENCES `projects` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10453 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for point_baidu
-- ----------------------------
DROP TABLE IF EXISTS `point_baidu`;
CREATE TABLE `point_baidu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` varchar(255) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `loc_time` bigint(20) NOT NULL,
  `radius` double DEFAULT NULL,
  `speed` double DEFAULT NULL,
  `checks` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ksumxjow9tn9e17al2yb1c4xh` (`checks`),
  KEY `baidu_points_loc_time_index` (`loc_time`),
  CONSTRAINT `FK_ksumxjow9tn9e17al2yb1c4xh` FOREIGN KEY (`checks`) REFERENCES `checks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approval_number` varchar(255) DEFAULT NULL,
  `approve_state` varchar(255) DEFAULT NULL,
  `approving_department` varchar(255) DEFAULT NULL,
  `carry_out_unit` varchar(255) DEFAULT NULL,
  `charge_person` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `county_level_city` varchar(255) DEFAULT NULL,
  `covered_farmer_number` varchar(255) DEFAULT NULL,
  `covering_number` varchar(255) DEFAULT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `deadline` varchar(255) DEFAULT NULL,
  `finance_fund` varchar(255) DEFAULT NULL,
  `fund_to_country` varchar(255) DEFAULT NULL,
  `fund_type` varchar(255) DEFAULT NULL,
  `fund_year` varchar(255) DEFAULT NULL,
  `input_status` varchar(255) DEFAULT NULL,
  `integrate_fund` varchar(255) DEFAULT NULL,
  `poverty_stricken_farmer_number` varchar(255) DEFAULT NULL,
  `poverty_stricken_people_number` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `project_number` bigint(20) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `scale_and_content` varchar(255) DEFAULT NULL,
  `self_financing` varchar(255) DEFAULT NULL,
  `standby_number` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `total_fund` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `village` varchar(255) DEFAULT NULL,
  `project_type` int(11) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sub_project_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project_import
-- ----------------------------
DROP TABLE IF EXISTS `project_import`;
CREATE TABLE `project_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_count` int(11) NOT NULL,
  `fail_count` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `handle_date` datetime DEFAULT NULL,
  `handle_status` int(11) NOT NULL,
  `import_date` datetime DEFAULT NULL,
  `import_status` int(11) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_update_date` datetime DEFAULT NULL,
  `success_count` int(11) NOT NULL,
  `update_count` int(11) NOT NULL,
  `operator` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ih3xckutfxdte4rm4eo462px4` (`operator`),
  CONSTRAINT `FK_ih3xckutfxdte4rm4eo462px4` FOREIGN KEY (`operator`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project_import_detail
-- ----------------------------
DROP TABLE IF EXISTS `project_import_detail`;
CREATE TABLE `project_import_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approval_number` varchar(255) DEFAULT NULL,
  `approve_state` varchar(255) DEFAULT NULL,
  `approving_department` varchar(255) DEFAULT NULL,
  `carry_out_unit` varchar(255) DEFAULT NULL,
  `charge_person` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `county_level_city` varchar(255) DEFAULT NULL,
  `cover_status` int(11) NOT NULL,
  `covered_farmer_number` varchar(255) DEFAULT NULL,
  `covering_number` varchar(255) DEFAULT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `deadline` varchar(255) DEFAULT NULL,
  `finance_fund` double DEFAULT NULL,
  `fund_to_country` double DEFAULT NULL,
  `fund_type` varchar(255) DEFAULT NULL,
  `fund_year` varchar(255) DEFAULT NULL,
  `import_status` int(11) NOT NULL,
  `input_status` varchar(255) DEFAULT NULL,
  `integrate_fund` double DEFAULT NULL,
  `poverty_stricken_farmer_number` varchar(255) DEFAULT NULL,
  `poverty_stricken_people_number` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `project_number` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `repeat_status` int(11) NOT NULL,
  `row_number` int(11) NOT NULL,
  `scale_and_content` varchar(255) DEFAULT NULL,
  `self_financing` double DEFAULT NULL,
  `standby_number` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `total_fund` double DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `validate_description` varchar(255) DEFAULT NULL,
  `validate_status` int(11) NOT NULL,
  `village` varchar(255) DEFAULT NULL,
  `project_import` bigint(20) DEFAULT NULL,
  `compare_description` varchar(255) DEFAULT NULL,
  `poverty_general_farmer` varchar(255) DEFAULT NULL,
  `poverty_general_people` varchar(255) DEFAULT NULL,
  `poverty_low_income_farmer` varchar(255) DEFAULT NULL,
  `poverty_low_income_people` varchar(255) DEFAULT NULL,
  `poverty_poor_farmer` varchar(255) DEFAULT NULL,
  `poverty_poor_people` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_68isntts9sbib3t1yvv3k6iia` (`project_import`),
  CONSTRAINT `FK_68isntts9sbib3t1yvv3k6iia` FOREIGN KEY (`project_import`) REFERENCES `project_import` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=489 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL,
  `approval_number` varchar(255) DEFAULT NULL,
  `approve_state` int(11) DEFAULT NULL,
  `approving_department` varchar(255) DEFAULT NULL,
  `carry_out_unit` varchar(255) DEFAULT NULL,
  `charge_person` varchar(255) DEFAULT NULL,
  `check_status` int(11) DEFAULT NULL,
  `construction_mode` int(11) DEFAULT NULL,
  `covered_farmer_number` int(11) DEFAULT NULL,
  `covering_number` int(11) DEFAULT NULL,
  `create_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `deadline` int(11) DEFAULT NULL,
  `farmer_name` varchar(255) DEFAULT NULL,
  `finance_fund` double(10,2) DEFAULT NULL,
  `financebi_lv` varchar(255) DEFAULT '0',
  `flag` int(11) DEFAULT NULL,
  `fund_to_country` double(10,2) DEFAULT NULL,
  `fund_type` varchar(255) DEFAULT NULL,
  `fund_year` varchar(255) DEFAULT NULL,
  `input_status` varchar(255) DEFAULT NULL,
  `integrate_fund` double(10,2) DEFAULT NULL,
  `is_level` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `poverty_stricken_farmer_number` int(11) DEFAULT NULL,
  `poverty_stricken_people_number` int(11) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `project_number` varchar(255) DEFAULT NULL,
  `project_type` int(11) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `scale_and_content` varchar(255) DEFAULT NULL,
  `self_financing` double(10,2) DEFAULT NULL,
  `standby_number` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `total_fund` double(10,2) DEFAULT NULL,
  `area` bigint(20) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `county_level_city` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `village` varchar(255) DEFAULT NULL,
  `bz_total` double(10,2) DEFAULT NULL,
  `bk_total` double DEFAULT NULL,
  `explain` varchar(255) DEFAULT NULL,
  `shuoming` varchar(255) DEFAULT NULL,
  `tree_level` varchar(255) DEFAULT NULL,
  `should_account` double DEFAULT NULL,
  `adjustment_reason` varchar(2000) DEFAULT NULL,
  `projects_status` int(11) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `adjustment_status` int(11) NOT NULL,
  `balance_forword` double DEFAULT NULL,
  `approver_status` int(11) NOT NULL,
  `project_process` varchar(255) DEFAULT NULL,
  `county_level_cityid` bigint(20) DEFAULT NULL,
  `townid` bigint(20) DEFAULT NULL,
  `villageid` bigint(20) DEFAULT NULL,
  `poverty_general_farmer` int(11) DEFAULT NULL,
  `poverty_low_income_farmer` int(11) DEFAULT NULL,
  `poverty_poor_farmer` int(11) DEFAULT NULL,
  `poverty_general_people` int(11) DEFAULT NULL,
  `poverty_low_income_people` int(11) DEFAULT NULL,
  `poverty_poor_people` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s0lsx5fvo1xxul35qqhwnyro` (`area`),
  KEY `FK_hxtwlt9f7m1mld3qt3jdq32ty` (`county_level_cityid`),
  KEY `FK_ld46xlw8uox9xj6mwdim7fjtv` (`townid`),
  KEY `FK_r33b3aj7upvwvgboftox1bcl6` (`villageid`),
  KEY `flag_projects_status_adjustment_status` (`flag`,`projects_status`,`adjustment_status`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=542 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `resource_code` varchar(255) DEFAULT NULL,
  `resource_name` varchar(255) DEFAULT NULL,
  `resource_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_44tvqee3k83o7haoqpgcyp3ak` (`resource_code`),
  UNIQUE KEY `UK_7vh3yrsm7oyl3lljngbasr6qj` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1171 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_role_name` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `role_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iubw515ff0ugtm28p8g3myt0h` (`role_name`),
  KEY `FK_8nhufvk7ufr23s4xoqglqtbdx` (`role_type`),
  CONSTRAINT `FK_8nhufvk7ufr23s4xoqglqtbdx` FOREIGN KEY (`role_type`) REFERENCES `role_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_resources
-- ----------------------------
DROP TABLE IF EXISTS `role_resources`;
CREATE TABLE `role_resources` (
  `role` bigint(20) NOT NULL,
  `resources` bigint(20) NOT NULL,
  PRIMARY KEY (`role`,`resources`),
  KEY `FK_ld1npqlnq3l7kjkp4it8p7tf` (`resources`),
  CONSTRAINT `FK_d8ucru4mbaec4xyt2n1s4d16x` FOREIGN KEY (`role`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_ld1npqlnq3l7kjkp4it8p7tf` FOREIGN KEY (`resources`) REFERENCES `resources` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_type
-- ----------------------------
DROP TABLE IF EXISTS `role_type`;
CREATE TABLE `role_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_desc` varchar(255) DEFAULT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `type_key` int(11) NOT NULL,
  `type_level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sub_project
-- ----------------------------
DROP TABLE IF EXISTS `sub_project`;
CREATE TABLE `sub_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_status` int(4) DEFAULT NULL,
  `construction_mode` int(4) DEFAULT NULL,
  `farmer_name` varchar(255) DEFAULT NULL,
  `implementation_unit` varchar(255) DEFAULT NULL,
  `jy` double DEFAULT NULL,
  `jz` double DEFAULT NULL,
  `project` bigint(20) DEFAULT '0',
  `project_scale_and_content` varchar(255) DEFAULT NULL,
  `reimbursement_rate` varchar(255) DEFAULT NULL,
  `should_account` double DEFAULT NULL,
  `sub_project_area` varchar(255) DEFAULT NULL,
  `sub_project_name` varchar(255) DEFAULT NULL,
  `sub_project_number` varchar(255) DEFAULT NULL,
  `total_capital` double DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `financebi_lv` varchar(255) DEFAULT NULL,
  `flag` int(4) NOT NULL,
  `poverty_general_farmer` int(11) DEFAULT NULL,
  `poverty_general_people` int(11) DEFAULT NULL,
  `poverty_low_income_farmer` int(11) DEFAULT NULL,
  `poverty_low_income_people` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_72i9mxfw4ks7udve012ous8me` (`project`),
  CONSTRAINT `FK_72i9mxfw4ks7udve012ous8me` FOREIGN KEY (`project`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for supervision
-- ----------------------------
DROP TABLE IF EXISTS `supervision`;
CREATE TABLE `supervision` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correct_suggestions` varchar(255) DEFAULT NULL,
  `correct_time` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `existing_problems` varchar(255) DEFAULT NULL,
  `farmer_number` int(11) NOT NULL,
  `fund` float NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  `project_process` varchar(255) DEFAULT NULL,
  `videos` varchar(255) DEFAULT NULL,
  `checks` bigint(20) DEFAULT NULL,
  `farmer` bigint(20) DEFAULT NULL,
  `point` bigint(20) DEFAULT NULL,
  `projects` bigint(20) DEFAULT NULL,
  `user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n34027o7vob7311u9o6sa89r8` (`checks`),
  KEY `FK_lbll70n4p6icjhqq1hy2rk0dt` (`farmer`),
  KEY `FK_9ix3c7dfrc81cwyvo3qfaa5d9` (`point`),
  KEY `FK_7gmpuult8ho7bfyjemderx87` (`user`),
  KEY `FK_5d8t689knbmu15mdsq1y0cw9x` (`projects`),
  CONSTRAINT `FK_5d8t689knbmu15mdsq1y0cw9x` FOREIGN KEY (`projects`) REFERENCES `projects` (`id`),
  CONSTRAINT `FK_7gmpuult8ho7bfyjemderx87` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_9ix3c7dfrc81cwyvo3qfaa5d9` FOREIGN KEY (`point`) REFERENCES `point` (`id`),
  CONSTRAINT `FK_lbll70n4p6icjhqq1hy2rk0dt` FOREIGN KEY (`farmer`) REFERENCES `farmer` (`id`),
  CONSTRAINT `FK_n34027o7vob7311u9o6sa89r8` FOREIGN KEY (`checks`) REFERENCES `checks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `system_operation_log`;
CREATE TABLE `system_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app` varchar(255) DEFAULT NULL,
  `app_module` varchar(255) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL,
  `op_text` varchar(255) DEFAULT NULL,
  `op_type` varchar(255) DEFAULT NULL,
  `sip` varchar(255) DEFAULT NULL,
  `sub_user` varchar(255) DEFAULT NULL,
  `detailed` varchar(10240) DEFAULT NULL,
  `is_rol_back` bit(1) DEFAULT NULL,
  `oper_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46842 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_id` varchar(255) DEFAULT NULL,
  `template_name` varchar(255) DEFAULT NULL,
  `template_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fsni42yk7b2yf24wgid23eoju` (`template_type`),
  CONSTRAINT `FK_fsni42yk7b2yf24wgid23eoju` FOREIGN KEY (`template_type`) REFERENCES `template_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for template_type
-- ----------------------------
DROP TABLE IF EXISTS `template_type`;
CREATE TABLE `template_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_desc` varchar(255) DEFAULT NULL,
  `type_key` int(11) NOT NULL,
  `type_namecn` varchar(255) DEFAULT NULL,
  `type_nameen` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_role_name` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `is_first_login` bit(1) DEFAULT NULL,
  `is_in_use` bit(1) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `dept_level` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7sp2rf73ucrffynlawjx8b1s6` (`dept_level`),
  CONSTRAINT `FK_7sp2rf73ucrffynlawjx8b1s6` FOREIGN KEY (`dept_level`) REFERENCES `constants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users_area
-- ----------------------------
DROP TABLE IF EXISTS `users_area`;
CREATE TABLE `users_area` (
  `users` bigint(20) NOT NULL,
  `area` bigint(20) NOT NULL,
  PRIMARY KEY (`users`,`area`),
  KEY `FK_i9aoynopgbeq2tw2drbakabv2` (`area`),
  CONSTRAINT `FK_8d5854fwn55qf9ew3362d9j8v` FOREIGN KEY (`users`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_i9aoynopgbeq2tw2drbakabv2` FOREIGN KEY (`area`) REFERENCES `area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users_department
-- ----------------------------
DROP TABLE IF EXISTS `users_department`;
CREATE TABLE `users_department` (
  `users` bigint(20) NOT NULL,
  `department` bigint(20) NOT NULL,
  PRIMARY KEY (`users`,`department`),
  KEY `FK_80fikoojw4db3a2yl8nat6emo` (`department`),
  CONSTRAINT `FK_80fikoojw4db3a2yl8nat6emo` FOREIGN KEY (`department`) REFERENCES `department` (`dept_id`),
  CONSTRAINT `FK_iljrkex6o5u2ygkg1q7vur0tt` FOREIGN KEY (`users`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users_role
-- ----------------------------
DROP TABLE IF EXISTS `users_role`;
CREATE TABLE `users_role` (
  `users` bigint(20) NOT NULL,
  `role` bigint(20) NOT NULL,
  PRIMARY KEY (`users`,`role`),
  KEY `FK_e7fenc9s4ojq3ag8kg672xlej` (`role`),
  CONSTRAINT `FK_e7fenc9s4ojq3ag8kg672xlej` FOREIGN KEY (`role`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_exmu3uw7yru4br5me93s0rkkw` FOREIGN KEY (`users`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
