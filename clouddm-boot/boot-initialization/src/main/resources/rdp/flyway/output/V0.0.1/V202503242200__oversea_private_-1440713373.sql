DELIMITER $$

DROP PROCEDURE if EXISTS proc_init;

$$
create procedure proc_init ()
begin
    declare continue handler for 1060 begin end;
    declare continue handler for 1062 begin end;
    declare continue handler for 1050 begin end;
    declare continue handler for 1072 begin end;
    declare continue handler for 1091 begin end;

   -- your DDLs here
   ALTER TABLE `rdp_product` ADD COLUMN `s3_bucket` varchar(64)  NOT NULL;
   ALTER TABLE `rdp_product` ADD COLUMN `s3_object_name` varchar(64)  NOT NULL;
   ALTER TABLE `rdp_product` ADD COLUMN `s3_region` varchar(64) NOT NULL;
   ALTER TABLE `rdp_product` ADD COLUMN `s3_download_site` varchar(255) NOT NULL;

   INSERT INTO `rdp_auth_version_field`(`license_version`, `fields`)
   VALUES ('0.5',
        'iFUup6TgfQdcLCpjGvxxhJU2Y7scexGPORQ4vK7ZWpdQ4MEcWOAA3EVD6nnWyCDpqysA0hOdCNlki5+x5LK7DtWI32ETHXYKeTaaCGCCQNoJHqeHMDxG9kjaLbn9iTalkAV48iLl04wtci809+3kAp5BSa8uNpXJtjG5n0OvsjvQXFUUeA6X9hlTslcS8BT0ab6MpmKlqtviZ8fMHs1cBwd+GzZCpjAoW0oxfQxmLB4jskjKINCT0Ejvw87p0yHcuYksMmoP6D2tXZQgCF8Wy4wm2sYlGBt94DrRmhLSjcVgdG60DmmfSPwVqqOHyslxanyKF9qYH7mImjhfunRVRF3SMmNlERlBapOkgBCwi0ra3qAykB1qMpacQ2euFy5DFXecoBGSoJLICssoHU+gIsVyd1wN8VvrxFb2vi0UilvUT1f6KleHvpNS79OJvHuRwI6FskhOw6Q4TYpUOYu/9Q==');

end
$$

call proc_init();
