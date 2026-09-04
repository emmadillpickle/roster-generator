PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS person (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    cooldown INTEGER NOT NULL DEFAULT 0
        CHECK (cooldown >= 0)
);

CREATE TABLE IF NOT EXISTS unavailable_date (
    person_id INTEGER NOT NULL,
    date TEXT NOT NULL,

    PRIMARY KEY (person_id, date),

    FOREIGN KEY (person_id) 
        REFERENCES person(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role_type TEXT NOT NULL
        CHECK (role_type IN ('SOLO', 'PAIRED')),

    UNIQUE (name, role_type)
);

CREATE TABLE IF NOT EXISTS event (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS person_role (
    person_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,

    PRIMARY KEY (person_id, role_id),

    FOREIGN KEY (person_id) 
        REFERENCES person(id)
        ON DELETE CASCADE,

    FOREIGN KEY (role_id) 
        REFERENCES role(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS role_count (
    event_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    count INTEGER NOT NULL DEFAULT 1
        CHECK (count >= 0),

    PRIMARY KEY (event_id, role_id),

    FOREIGN KEY (event_id) 
        REFERENCES event(id)
        ON DELETE CASCADE,

    FOREIGN KEY (role_id) 
        REFERENCES role(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pairing (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_id INTEGER NOT NULL,

    FOREIGN KEY (role_id) 
        REFERENCES role(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pairing_member (
    pairing_id INTEGER NOT NULL,
    person_id INTEGER NOT NULL,

    PRIMARY KEY (pairing_id, person_id),

    FOREIGN KEY (pairing_id) 
        REFERENCES pairing(id)
        ON DELETE CASCADE,

    FOREIGN KEY (person_id) 
        REFERENCES person(id)
        ON DELETE CASCADE
);