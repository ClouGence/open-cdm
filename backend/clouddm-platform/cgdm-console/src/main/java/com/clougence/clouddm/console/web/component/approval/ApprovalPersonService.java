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
package com.clougence.clouddm.console.web.component.approval;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.model.vo.PrimaryUserVO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalPersonDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import jakarta.annotation.Resource;

@Service
public class ApprovalPersonService {

    @Resource
    private AuthDal     authDal;
    @Resource
    private ApprovalDal approvalDal;

    public List<String> replacePersons(DmApprovalDO ticketDO, List<PrimaryUserVO> persons) {
        LinkedHashMap<String, String> users = new LinkedHashMap<>();
        persons.forEach(person -> users.put(person.getUid(), person.getUsername()));

        List<DmAuthUserDO> globalUsers = this.authDal.resMapper().listEffectiveGlobalAuthUsersByPrimaryUid(ticketDO.getPrimaryUid(), AuthKind.DataSource);
        globalUsers.forEach(user -> users.putIfAbsent(user.getUid(), user.getUsername()));

        List<String> newUids = new ArrayList<>(users.keySet());
        List<DmApprovalPersonDO> oldPersons = this.approvalDal.personMapper().queryByTicketBzId(ticketDO.getBizId());
        List<String> oldUids = oldPersons.stream().map(DmApprovalPersonDO::getPersonUid).collect(Collectors.toList());
        if (!new HashSet<>(newUids).equals(new HashSet<>(oldUids))) {
            this.approvalDal.personMapper().deleteByTicketBzId(ticketDO.getBizId());
            List<DmApprovalPersonDO> newPersons = new ArrayList<>();
            newUids.forEach(personUid -> {
                DmApprovalPersonDO personDO = new DmApprovalPersonDO();
                personDO.setTicketBzId(ticketDO.getBizId());
                personDO.setPersonUid(personUid);
                newPersons.add(personDO);
            });
            this.approvalDal.personMapper().insertPersonBatch(newPersons);
        }
        return new ArrayList<>(users.values());
    }
}
