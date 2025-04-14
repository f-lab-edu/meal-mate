INSERT INTO meal_mate_db.meetup
(id, title, content, start_datetime, recruitment_deadline_datetime, progress_status, participation_type, recruitment_status, min_participants, max_participants, create_datetime, creator_id, creator_name, modify_datetime, modifier_id, modifier_name)
VALUES(694744727775892459, '신촌에서 같이 밥먹을 사람', '샤브샤브 먹고싶어요.', '2026-01-01 12:00:00', '2026-01-01 11:00:00', 'SCHEDULED', 'AUTO', 'OPEN', 3, 5, '2025-04-01 12:07:30', 0, 'System', '2025-04-01 12:07:30', 0, 'System');

INSERT INTO meal_mate_db.meetup_participant
(id, meetup_id, participation_status, application_message, create_datetime, creator_id, creator_name, modify_datetime, modifier_id, modifier_name)
VALUES(694744741579346775, 694744727775892459, 'APPROVED', NULL, '2025-04-01 12:07:34', 0, 'System', '2025-04-01 12:07:34', 0, 'System');