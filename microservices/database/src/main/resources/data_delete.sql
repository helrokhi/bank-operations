DELETE FROM person WHERE user_id IN ('1000','1001','1002','1003','1004','1005','1006','1007','1008','1009');

-----
DELETE FROM user_auth WHERE id IN ('1000','1001','1002','1003','1004','1005','1006','1007','1008','1009');

-----
DELETE FROM phone WHERE person_id IN ('10001','10002','10003','10004','10005','10006','10007','10008','10009','10010');

-----
DELETE FROM email WHERE person_id IN ('10001','10002','10003','10004','10005','10006','10007','10008','10009','10010');

-----
DELETE FROM deposit WHERE person_id IN ('10001','10002','10003','10004','10005','10006','10007','10008','10009','10010');

-----
COMMIT;