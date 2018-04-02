Select Count(*) FROM HistoryEvents;
Select Count(*) FROM Topics;
Select Count(*) FROM Topic_Events;
Select Count(*) FROM Resources;
Select Count(*) FROM Log;

Select u.username, r.role_name
From app_user as u
inner join user_role ur on u.id = ur.user_id
inner join app_role r on ur.role_id = r.id
Order by u.username;

Select he.*, t.word 
FROM history_events he
INNER JOIN history_events_topics te on he.id = te.history_event_id
INNER JOIN topics t on t.id = te.topic_id
WHERE t.word = "Gates";


Select he.*
From HistoryEvents he
JOIN Topic_Events te ON he.idHistoryEvents = te.idHistoryEvents
JOIN Topics t ON te.idTopic = t.idTopic
Where t.Name like "%Jochi%";

Select he.*
From history_events he
Where he.phrase like "%Ptolemy%";

Select he.*, t.Name
From HistoryEvents he
JOIN Topic_Events te ON he.idHistoryEvents = te.idHistoryEvents
JOIN Topics t ON te.idTopic = t.idTopic
Where he.Validity = 0
Order By he.CreatedDT ASC
Limit 1;

-- Clear history events for reprocessing
-- DELETE te.*
-- FROM Topic_Events te
-- INNER JOIN HistoryEvents he on te.idHistoryEvents = he.idHistoryEvents
-- WHERE he.idResource = 1;
-- DELETE FROM HistoryEvents WHERE idResource = 1;

-- Clean tables for new test run
-- Delete From Topic_Events WHERE idTopic > 0;
-- Delete From Topics WHERE idTopic > 0;
-- Delete From HistoryEvents WHERE idHistoryEvents > 0;
-- Delete From Resource WHERE idResource > 0;
-- Delete From Log WHERE idLog > 0;


-- Inser security into app
insert into user_role (user_id, role_id) values (2,2);
insert into user_role (user_id, role_id) values (2,3);
insert into user_role (user_id, role_id) values (2,4);
insert into user_role (user_id, role_id) values (3,3);
insert into user_role (user_id, role_id) values (3,4);