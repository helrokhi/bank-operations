INSERT INTO person (id,
                    user_id,
                    first_name,
                    last_name,
                    sur_name,
                    balance,
                    reg_date,
                    birth_date)
VALUES
        ('10001',
        '1000',
        'Thomas',
        'Cox',
        NULL,
        '12300.00',
        '2023-02-25 14:05',
        '2001-11-03'
       ),
        ('10002',
        '1001',
        'Matthew',
        'Martin',
        NULL,
        '234.00',
        '2023-03-11 11:15',
        '2000-08-14'
        ),
        ('10003',
        '1002',
        'Arnold',
        'Collier',
        NULL,
        '3450.00',
        '2023-10-13 23:26',
        '2005-06-12'
        ),
        ('10004',
        '1003',
        'John',
        'Evans',
        NULL,
        '45600.00',
        '2023-01-15 00:01',
        '2011-11-25'
        ),
        ('10005',
        '1004',
        'Andrea',
        'Barnes',
        NULL,
        '56780.00',
        '2023-10-18 02:05',
        '2001-12-10'
        ),
        ('10006',
        '1005',
        'Jennifer',
        'Hicks',
        NULL,
        '8901.00',
        '2023-09-19 18:45',
        '2003-03-09'
        ),
        ('10007',
        '1006',
        'Kenneth',
        'Figueroa',
        NULL,
        '9120.00',
        '2023-11-01 16:41',
        '2004-09-18'
        ),
        ('10008',
        '1007',
        'Timothy',
        'Jackson',
        NULL,
        '365800.00',
        '2023-10-12 20:26',
        '2008-08-02'
        ),
        ('10009',
        '1008',
        'Curtis',
        'Ford',
        NULL,
        '8450.00',
        '2023-03-18 23:14',
        '2012-05-30'
        ),
        ('10010',
        '1009',
        'Linda',
        'Rodgers',
        NULL,
        '9000.00',
        '2023-08-25 00:11',
        '1995-10-01'
        );

------------------
INSERT INTO phone (id,
                    person_id,
                    phone_number)
VALUES
        ('101',
        '10001',
        '81(3)984-62-07'
        ),
        ('102',
        '10002',
        '61(28)752-56-71'
        ),
        ('103',
        '10003',
        '82(02)085-83-21'
        ),
        ('104',
        '10004',
        '48(22)084-61-02'
        ),
        ('105',
        '10005',
        '39(02)731-32-45'
        ),
        ('106',
        '10006',
        '212(37)15-02-53'
        ),
        ('107',
        '10007',
        '351(21)318-73-1'
        ),
        ('108',
        '10008',
        '20(02)546-96-18'
        ),
        ('109',
        '10009',
        '1(343)243-08-50'
        ),
        ('110',
        '10008',
        '20(02)546-96-19'
        ),
        ('111',
        '10009',
        '1(343)243-08-51'
        ),
        ('112',
        '10010',
        '358(9)114-07-60'
        );
------------------

INSERT INTO user_auth (id,
                    login,
                    password)
VALUES
        ('1000',
        'thuel.yahoo',
        '$2a$12$CxhZISH7nROqSPsUAqx2aueh4KnSV2syzKB4Sb4zKQ0SArQvJ31lu' -- 00000000
        ),
        ('1001',
        'bmetz.gmail',
        '$2a$12$UE9CIvCIgZSGHe4yDDjaMuCqv4taZt9rkbt2Vxk0kjyeqS72ZAjWm' -- 11111111
        ),
        ('1002',
        'shayne75.fay',
        '$2a$12$hGiyFH9VX4.WvayHHBlvEuDh7.YjVS3NvKNsKLe03rGEtsBVXpMNq' -- 22222222
        ),
        ('1003',
        'iblock.hot',
        '$2a$12$Wu5qfAVjr48nYZHBOuaite.OVmyRx77hufx5yD/rQ40Rr6eTmYPtG' -- 33333333
        ),
        ('1004',
        'ernie04.heller',
        '$2a$12$byYX6T9xNt5lFqtwh4o9A.Tm980wwjvVHZK8cVNJmLdPjgLM3gANa' -- 44444444
        ),
        ('1005',
        'beth.kunde',
        '$2a$12$2jtDLy0gr1svGAmU3tFAhu8qab8qegIGdWpYIyWsF5hre3LdMwBcW' -- 55555555
        ),
        ('1006',
        'toney39.berge',
        '$2a$12$6lbWnnBJVmlIoG38VV.CPO5NnETHGX5/lPQG4JXCHUkF8SBHhup6.' -- 66666666
        ),
        ('1007',
        'yhickle.nikolaus',
        '$2a$12$phNfh3pM8yaXoGgn9as3WOHvv7YwE5fmWCxpWjfH5V7hc5ADm4SmW' -- 77777777
        ),
        ('1008',
        'aufderhar.cary',
        '$2a$12$P.dlsHFXFOOC2qNHVnYOP.vgid0gLfO/JfvihhLOksAevkDz/1baK' -- 88888888
        ),
        ('1009',
        'xwiegand.hotmail',
        '$2a$12$gyEVpo94MvHO0FpH3JMCMe5zva3GNse6b/jqu1Roj/5yIKCQxU9fS'-- 99999999
        );

------------------

INSERT INTO email (id,
                    person_id,
                    email_address)
VALUES
        ('101',
        '10001',
        'thuel@yahoo.com'
        ),
        ('102',
        '10002',
        'bmetz@gmail.com'
        ),
        ('103',
        '10003',
        'shayne75@fay.com'
        ),
        ('104',
        '10004',
        'iblock@hotmail.com'
        ),
        ('105',
        '10005',
        'ernie04@heller.biz'
        ),
        ('106',
        '10006',
        'beth.kunde@yahoo.com'
        ),
        ('107',
        '10007',
        'toney39@berge.net'
        ),
        ('108',
        '10008',
        'yhickle.exp@nikolaus.com'
        ),
        ('109',
        '10009',
        'aufderhar25.cary@harber.com'
        ),
        ('110',
        '10008',
        'yhickle@nikolaus.com'
        ),
        ('111',
        '10009',
        'aufderhar13.cary@harber.com'
        ),
        ('112',
        '10010',
        'xwiegand@hotmail.com'
        );
-------
INSERT INTO deposit (id,
                    person_id,
                    initial_deposit,
                    deposit_limit,
                    deposit_percent,
                    is_done)
VALUES
        ('101', '10001',
        '12300.00',
        '24600.00',
        '0.00',FALSE
        ),
        ('102', '10002',
        '234.00',
        '438.00',
        '0.00', FALSE
        ),
        ('103', '10003',
        '3450.00',
        '6900.00',
        '0.00', FALSE
        ),
        ('104', '10004',
        '45600.00',
        '91200.00',
        '0.00', FALSE
        ),
        ('105', '10005',
        '56780.00',
        '113560.00',
        '0.00', FALSE
        ),
        ('106', '10006',
        '8901.00',
        '17802.00',
        '0.00', FALSE
        ),
        ('107', '10007',
        '9120.00',
        '18240.00',
        '0.00', FALSE
        ),
        ('108', '10008',
        '365800.00',
        '731600.00',
        '0.00', FALSE
        ),
        ('109', '10009',
        '8450.00',
        '16900.00',
        '0.00', FALSE
        ),
        ('110', '10010',
        '8450.00',
        '16900.00',
        '0.00', FALSE
        );

------------------

COMMIT;