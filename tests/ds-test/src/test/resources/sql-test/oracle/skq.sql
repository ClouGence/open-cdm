SELECT
    T.*
FROM
    M_RET_SALE T,
    C_STORE T1,
    C_STORETYPE_JZ T2,
    C_STORE T3
WHERE
    T.ISACTIVE = 'Y'
  AND T.STATUS = 2
  AND T.OUT_STATUS = 2
  AND NVL(T.IS_TOZT_OUTER, 'N') = 'N'
  AND T.C_STORE_ID = T1.ID
  AND T.C_ORIG_ID = T3.ID
  AND (
    NVL(T1.IS_ZT, 'N') = 'Y'
        OR NVL(T3.IS_ZT, 'N') = 'Y'
    )
  AND T1.C_STORETYPE_JZ_ID = T2.ID
  AND T.M_RA_SALE_ID IS NULL
  AND T.DATEOUT >= 20240101
  AND TO_CHAR(T.MODIFIEDDATE, 'yyyymmdd') >= 20221231
  AND T.ZT_DOCNO IS NULL
  AND NVL(T.ISTOR3, 'Y') = 'Y'
  AND NVL(T.DESCRIPTION, ' ') NOT LIKE '%差异处理生成%'
  AND NVL(T.DESCRIPTION, ' ') NOT LIKE '%由云仓调货业务单据%'
  AND T3.CODE <> 'STM10'
UNION ALL
SELECT
    T.*
FROM
    M_RET_SALE T,
    C_STORE T1,
    C_STORETYPE_JZ T2,
    C_STORE T3,
    M_RA_SALE T4
WHERE
    T.ISACTIVE = 'Y'
  AND T.STATUS = 2
  AND T.OUT_STATUS = 2
  AND NVL(T.IS_TOZT_OUTER, 'N') = 'N'
  AND T.C_STORE_ID = T1.ID
  AND T.C_ORIG_ID = T3.ID
  AND (
    NVL(T1.IS_ZT, 'N') = 'Y'
        OR NVL(T3.IS_ZT, 'N') = 'Y'
    )
  AND T1.C_STORETYPE_JZ_ID = T2.ID
  AND T.M_RA_SALE_ID = T4.ID (+)
  AND T.M_RA_SALE_ID IS NOT NULL
  AND T4.DOCNO NOT LIKE 'REM%'
  AND T.DATEOUT >= 20240101
  AND TO_CHAR(T.MODIFIEDDATE, 'yyyymmdd') >= 20221231
  AND T.ZT_DOCNO IS NULL
  AND NVL(T.ISTOR3, 'Y') = 'Y'
  AND NVL(T.DESCRIPTION, ' ') NOT LIKE '%差异处理生成%'
  AND NVL(T.DESCRIPTION, ' ') NOT LIKE '%由云仓调货业务单据%'
  AND T3.CODE <> 'STM10';

SELECT
    /*+ index (a idx_m_sale240428)*/ A.ID,
                                     A.DOCNO
FROM
    M_SALE A,
    C_STORE B,
    C_STORE C
WHERE
    A.DATEOUT >= 20250101
  AND A.C_STORE_ID = B.ID
  AND A.C_DEST_ID = C.ID
  AND A.ISACTIVE = 'Y'
  AND A.STATUS = 2
  AND A.OUT_STATUS = 2
  AND A.IS_TOZT_DB = 'Y'
  AND IS_TOZT_OUT = 'N'
  AND EXISTS (
    SELECT
        1
    FROM
        ZT_SALECONFIRM_EZ D
    WHERE
        D.BILL_NO = A.DOCNO
      AND D.EDIFLAG = 99
)
  AND (
    NVL(B.IS_ZT, 'N') = 'Y'
        OR NVL(C.IS_ZT, 'N') = 'Y'
    )
  AND (
    NVL(B.IS_ZT, 'N') = 'Y'
        OR NVL(C.IS_ZT, 'N') = 'Y'
    )
  AND NVL(A.DESCRIPTION, ' ') NOT LIKE '%差异处理生成%'
  AND NVL(A.ISTOR3, 'Y') = 'Y'
  AND A.ZT_DOCNO IS NULL
UNION ALL
SELECT
    /*+ index (a idx_m_sale240428)*/ A.ID,
                                     A.DOCNO
FROM
    M_SALE A
WHERE
    A.DATEOUT >= 20250101
  AND A.ISACTIVE = 'Y'
  AND A.STATUS = 2
  AND A.OUT_STATUS = 2
  AND A.ZT_DOCNO IS NOT NULL
  AND IS_TOZT_OUT = 'N'
  AND NVL(A.DESCRIPTION, ' ') NOT LIKE '%差异处理生成%'
  AND NVL(A.ISTOR3, 'Y') = 'Y';

CREATE OR REPLACE procedure            AD_TABLE_AM(p_id in number) as
    /**
    * 判断修改的合理性
    *
    */
    v_pk_column_id      number(10);
    v_summary_column_id number(10);
    v_parent_column_id  number(10);
    v_istree            char(1);
    v_ref_column_id     number(10);
    v_cnt               number(10);
begin
    --同步目录的描述和表的描述一致

update directory
set description = (select description from ad_table where id = p_id)
where ad_table_id = p_id;

update directory
set AD_TABLECATEGORY_ID = (select AD_TABLECATEGORY_ID
                           from ad_table
                           where id = p_id)
where ad_table_id = p_id;
-- 如果为树型结构，必须设置parent_column_id 和 summary_column_id
-- parent_column_id 必须FK指向本表PK,
-- summary_column_id 必须是YESNO 型

select istree, summary_column_id, parent_column_id, pk_column_id
into v_istree, v_summary_column_id, v_parent_column_id, v_pk_column_id
from ad_table
where id = p_id;

if v_istree = 'Y' then
        if v_summary_column_id is null or v_parent_column_id is null then
            raise_application_error(-20201, '必须设置汇总标识字段和父节点字段');
end if;

select REF_COLUMN_ID
into v_ref_column_id
from ad_column
where id = v_parent_column_id;
-- parent_column_id 必须FK指向本表PK,
if v_ref_column_id is null or v_ref_column_id <> v_pk_column_id then
            raise_application_error(-20201, '父节点字段应该以本表的PK为FK');
end if;
        -- summary_column_id 必须是YESNO 型
select count(*)
into v_cnt
from ad_column
where id = v_summary_column_id and exists
    (select 1
     from ad_LIMITVALUE_GROUP
     where ad_LIMITVALUE_GROUP.id = ad_column.ad_LIMITVALUE_GROUP_id and
         ad_LIMITVALUE_GROUP.name = 'YESNO');
if v_cnt = 0 then
            raise_application_error(-20201,
                                    '汇总标识字段字段应该是选项型(YESNO)');
end if;

end if;
end AD_TABLE_AM;

CREATE OR REPLACE PROCEDURE            autotask_endlog(logtxt   IN autotask_log.log%TYPE,
                                            targetid IN autotask_log.id%TYPE) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN

UPDATE autotask_log
SET (log, etime, modifieddate, ifsuccess) = (SELECT log || chr(13) || chr(9) ||
                                                    to_char(SYSDATE,
                                                            'yyyy/mm/dd hh24:mi:ss') ||
                                                    logtxt, SYSDATE, SYSDATE, 'Y'
                                             FROM autotask_log
                                             WHERE id = targetid)
WHERE id = targetid;

COMMIT;

END autotask_endlog;