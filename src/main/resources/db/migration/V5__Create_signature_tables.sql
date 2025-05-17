CREATE TABLE signatures
(
    id                UUID PRIMARY KEY,
    threat_name       VARCHAR(255) NOT NULL,
    first_bytes       BYTEA        NOT NULL,
    remainder_hash    VARCHAR(64)  NOT NULL,
    remainder_length  INT          NOT NULL,
    file_type         VARCHAR(50)  NOT NULL,
    offset_start      INT          NOT NULL,
    offset_end        INT          NOT NULL,
    digital_signature TEXT,
    updated_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    status            VARCHAR(20) DEFAULT 'ACTUAL'
);

CREATE TABLE signature_history
(
    history_id         BIGSERIAL PRIMARY KEY,
    signature_id       UUID REFERENCES signatures (id) ON DELETE CASCADE,
    version_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    threat_name        VARCHAR(255),
    first_bytes        BYTEA,
    remainder_hash     VARCHAR(64),
    remainder_length   INT,
    file_type          VARCHAR(50),
    offset_start       INT,
    offset_end         INT,
    digital_signature  TEXT,
    status             VARCHAR(20),
    updated_at         TIMESTAMP
);

CREATE TABLE audit_log
(
    audit_id       BIGSERIAL PRIMARY KEY,
    signature_id   UUID REFERENCES signatures (id) ON DELETE CASCADE,
    changed_by     VARCHAR(255) NOT NULL,
    change_type    VARCHAR(20)  NOT NULL,
    changed_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fields_changed TEXT
);