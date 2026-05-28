-- Add points fields to user table
ALTER TABLE t_user
ADD COLUMN total_points INT NOT NULL DEFAULT 0,
ADD COLUMN available_points INT NOT NULL DEFAULT 0,
ADD COLUMN member_level VARCHAR(20) DEFAULT 'BRONZE';

-- Create points record table
CREATE TABLE t_points_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(200),
    order_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_points_record_user_id ON t_points_record(user_id);
CREATE INDEX idx_points_record_created_at ON t_points_record(created_at);
