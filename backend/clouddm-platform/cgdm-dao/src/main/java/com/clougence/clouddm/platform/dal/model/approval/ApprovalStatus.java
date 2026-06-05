/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.model.approval;

/**
 * @author Ekko
 * @date 2024/6/28 14:45
*/
public enum ApprovalStatus {

    /** Wait to explain */
    PRE_INIT,

    /** Wait to approval */
    WAIT_APPROVAL,

    /** Wait dba to confirm */
    WAIT_CONFIRM,

    /** Thread will scan out ticket that in this status and exec sql for them */
    WAIT_EXEC,

    /** Means sqls in the ticket is running */
    RUNNING,

    /** If the related sql exec failed with lead ticket to this status */
    EXEC_FAIL,

    /** If the relate sql exec pause with lead ticket to this status **/
    EXEC_PAUSE,

    /** (end status) The db owner can reject exec the sql in the last stage */
    REJECTED,

    /** (end status) the sql run finish.*/
    FINISHED,

    /** (end status) Failed execution can be closed */
    CLOSED,

    /** (end status) Failed during the process ,such as  delete datasource close third party approval...**/
    FAILED,

    /** (end status) Ticket owner can cancel ticket before sql executing */
    CANCELED;

    public static boolean isEndStatus(ApprovalStatus status) {
        return status == REJECTED || status == FINISHED || status == CLOSED || status == CANCELED || status == FAILED;
    }
}
