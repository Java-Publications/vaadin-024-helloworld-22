CREATE SEQUENCE comp_math_basic_id_seq;
CREATE TABLE comp_math_basic (
  ID             SMALLINT PRIMARY KEY DEFAULT nextval('comp_math_basic_id_seq'),
  OP_A           FLOAT       NOT NULL,
  OP             VARCHAR(15) NOT NULL,
  OP_B           CHARACTER   NOT NULL,
  RESULT_MACHINE FLOAT       NOT NULL,
  RESULT_HUMAN   VARCHAR(15),
  RESULT_OK      BOOLEAN     NOT NULL,
  CREATED        TIMESTAMP   NOT NULL
);
COMMIT;