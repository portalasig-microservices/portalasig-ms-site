ALTER TABLE site_party
    ADD COLUMN party_site_title VARCHAR(128) COMMENT 'title for a party in a specific site. e.g: Student Teacher I, II' AFTER party_role;
