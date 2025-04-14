USE meal_mate_db;

-- Meetup 테이블
CREATE TABLE IF NOT EXISTS meetup (
                                      id BIGINT PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    start_datetime DATETIME NOT NULL,
    recruitment_deadline_datetime DATETIME NOT NULL,
    progress_status ENUM('SCHEDULED', 'COMPLETED', 'CANCELED') NOT NULL,
    participation_type ENUM('AUTO', 'APPROVAL') NOT NULL,
    recruitment_status ENUM('OPEN', 'CLOSED', 'PAUSED') NOT NULL,
    min_participants INT NOT NULL,
    max_participants INT NOT NULL,
    create_datetime DATETIME NOT NULL,
    creator_id BIGINT NOT NULL,
    creator_name VARCHAR(100) NOT NULL,
    modify_datetime DATETIME,
    modifier_id BIGINT,
    modifier_name VARCHAR(100),
    search_text TEXT GENERATED ALWAYS AS (CONCAT(title, ' ', content)) STORED,
    FULLTEXT INDEX idx_meetup_search_text (search_text)
    );

-- MeetupParticipant 테이블
CREATE TABLE IF NOT EXISTS meetup_participant (
                                                  id BIGINT PRIMARY KEY,
                                                  meetup_id BIGINT NOT NULL,
                                                  participation_status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL,
    application_message TEXT,
    create_datetime DATETIME NOT NULL,
    creator_id BIGINT NOT NULL,
    creator_name VARCHAR(100) NOT NULL,
    modify_datetime DATETIME,
    modifier_id BIGINT,
    modifier_name VARCHAR(100)
    );
