CREATE DATABASE IF NOT EXISTS health_data DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE health_data;

-- 用户表
CREATE TABLE `account` (
                           `account_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账户ID',
                           `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                           `password_hash` VARCHAR(100) NOT NULL COMMENT '加密密码',
                           `account_type` TINYINT NOT NULL DEFAULT 0 COMMENT '账户类型（0-个人，1-管理员）',
                           `email` VARCHAR(100) COMMENT '邮箱',
                           `phone_number` VARCHAR(20) COMMENT '手机号码',
                           `account_status` TINYINT NOT NULL DEFAULT 1 COMMENT '账户状态（0-停用，1-启用）',
                           `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`account_id`),
                           UNIQUE INDEX `udx_username` (`username`),
                           INDEX `idx_account_status` (`account_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

-- 生物特征记录表
CREATE TABLE `biometric_record` (
                                    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
                                    `account_id` BIGINT NOT NULL COMMENT '账户ID',
                                    `height_cm` DECIMAL(5,2) COMMENT '身高(cm)',
                                    `weight_kg` DECIMAL(5,2) COMMENT '体重(kg)',
                                    `systolic_pressure` SMALLINT COMMENT '收缩压(mmHg)',
                                    `diastolic_pressure` SMALLINT COMMENT '舒张压(mmHg)',
                                    `blood_glucose` DECIMAL(4,1) COMMENT '血糖(mmol/L)',
                                    `blood_lipid` DECIMAL(4,1) COMMENT '血脂(mmol/L)',
                                    `measurement_time` DATETIME NOT NULL COMMENT '测量时间',
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    PRIMARY KEY (`record_id`),
                                    INDEX `idx_account_measurement` (`account_id`, `measurement_time`),
                                    FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生物特征记录表';

-- 运动记录表
CREATE TABLE `exercise_log` (
                                `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                                `account_id` BIGINT NOT NULL COMMENT '账户ID',
                                `exercise_type` VARCHAR(20) NOT NULL COMMENT '运动类型',
                                `start_timestamp` DATETIME NOT NULL COMMENT '开始时间',
                                `duration_minutes` INT NOT NULL COMMENT '持续时间(分钟)',
                                `distance_km` DECIMAL(6,2) COMMENT '距离(km)',
                                `calories_burned` INT COMMENT '消耗卡路里',
                                `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                PRIMARY KEY (`log_id`),
                                INDEX `idx_account_exercise` (`account_id`, `start_timestamp`),
                                FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

-- 饮食记录表
CREATE TABLE `diet_log` (
                            `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                            `account_id` BIGINT NOT NULL COMMENT '账户ID',
                            `food_item` VARCHAR(100) NOT NULL COMMENT '食物名称',
                            `quantity_grams` DECIMAL(6,2) NOT NULL COMMENT '数量(克)',
                            `total_calories` INT NOT NULL COMMENT '总热量(kcal)',
                            `consumption_time` DATETIME NOT NULL COMMENT '食用时间',
                            `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`log_id`),
                            INDEX `idx_account_diet` (`account_id`, `consumption_time`),
                            FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='饮食记录表';

-- 睡眠记录表
CREATE TABLE `sleep_log` (
                             `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                             `account_id` BIGINT NOT NULL COMMENT '账户ID',
                             `sleep_start` DATETIME NOT NULL COMMENT '入睡时间',
                             `sleep_end` DATETIME NOT NULL COMMENT '醒来时间',
                             `sleep_quality` TINYINT COMMENT '睡眠质量（1-5级）',
                             `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             PRIMARY KEY (`log_id`),
                             INDEX `idx_account_sleep` (`account_id`, `sleep_start`),
                             FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='睡眠记录表';

-- 健康目标表
CREATE TABLE `health_goal` (
                               `goal_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '目标ID',
                               `account_id` BIGINT NOT NULL COMMENT '账户ID',
                               `goal_category` VARCHAR(20) NOT NULL COMMENT '目标类别',
                               `target_value` DECIMAL(10,2) NOT NULL COMMENT '目标值',
                               `current_value` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '当前值',
                               `start_date` DATE NOT NULL COMMENT '开始日期',
                               `target_date` DATE NOT NULL COMMENT '目标日期',
                               `goal_status` TINYINT NOT NULL DEFAULT 0 COMMENT '目标状态（0-进行中，1-已达成，2-未达成）',
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               PRIMARY KEY (`goal_id`),
                               INDEX `idx_goal_status` (`account_id`, `goal_status`),
                               FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康目标表';

-- 健康提醒表
CREATE TABLE `health_reminder` (
                                   `reminder_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提醒ID',
                                   `account_id` BIGINT NOT NULL COMMENT '账户ID',
                                   `reminder_category` VARCHAR(20) NOT NULL COMMENT '提醒类别',
                                   `reminder_content` VARCHAR(200) NOT NULL COMMENT '提醒内容',
                                   `scheduled_time` DATETIME NOT NULL COMMENT '预定时间',
                                   `completion_status` TINYINT NOT NULL DEFAULT 0 COMMENT '完成状态（0-待处理，1-已完成）',
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   PRIMARY KEY (`reminder_id`),
                                   INDEX `idx_reminder_schedule` (`account_id`, `scheduled_time`),
                                   FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康提醒表';