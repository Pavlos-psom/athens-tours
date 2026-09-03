-- Seed data: the three roles and their capabilities.

INSERT INTO role (name) VALUES ('ADMIN'), ('GUIDE'), ('CUSTOMER');

INSERT INTO capability (name, description) VALUES
    ('MANAGE_TOURS',       'Create, edit and delete tours and categories'),
    ('MANAGE_GUIDES',      'Create, edit and delete tour guide profiles'),
    ('MANAGE_SCHEDULES',   'Create, edit and delete tour schedules'),
    ('MANAGE_BOOKINGS',    'View and manage all bookings'),
    ('VIEW_OWN_SCHEDULE',  'View own assigned tour schedules'),
    ('CREATE_BOOKING',     'Create a new booking'),
    ('VIEW_OWN_BOOKINGS',  'View and cancel own bookings');

-- ADMIN: full management access
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id FROM role r JOIN capability c
WHERE r.name = 'ADMIN'
  AND c.name IN ('MANAGE_TOURS', 'MANAGE_GUIDES', 'MANAGE_SCHEDULES', 'MANAGE_BOOKINGS');

-- GUIDE: can see their own schedule
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id FROM role r JOIN capability c
WHERE r.name = 'GUIDE'
  AND c.name IN ('VIEW_OWN_SCHEDULE');

-- CUSTOMER: can book and manage their own bookings
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id FROM role r JOIN capability c
WHERE r.name = 'CUSTOMER'
  AND c.name IN ('CREATE_BOOKING', 'VIEW_OWN_BOOKINGS');
