CREATE DATABASE __FIXTURE_DB__;

CREATE TABLE __FIXTURE_DB__.motor_samples (
    line STRING TAG,
    device STRING TAG,
    model STRING ATTRIBUTE,
    temperature FLOAT FIELD,
    pressure INT32 FIELD
);

CREATE TABLE __FIXTURE_DB__.motor_private (
    device STRING TAG,
    temperature FLOAT FIELD
);

INSERT INTO __FIXTURE_DB__.motor_samples(time, line, device, model, temperature, pressure)
    VALUES (1, 'line1', 'motor01', 'model_a', 21.5, 101),
           (2, 'line1', 'motor01', 'model_a', 22.5, 102),
           (1, 'line2', 'motor02', 'model_b', 31.5, 201),
           (2, 'line2', 'motor02', 'model_b', 32.5, 202);

INSERT INTO __FIXTURE_DB__.motor_private(time, device, temperature)
    VALUES (1, 'motor01', 91.5);
