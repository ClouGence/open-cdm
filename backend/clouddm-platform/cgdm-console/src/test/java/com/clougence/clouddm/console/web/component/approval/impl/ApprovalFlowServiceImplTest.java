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
package com.clougence.clouddm.console.web.component.approval.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.clougence.clouddm.console.web.component.approval.ApprovalPersonService;
import com.clougence.clouddm.console.web.component.approval.ApprovalStateService;
import com.clougence.clouddm.console.web.component.approval.handler.ChangeApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.handler.QueryApprovalHandler;
import com.clougence.clouddm.console.web.component.approval.model.ApprovalStageMO;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalMapper;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalPersonMapper;
import com.clougence.clouddm.platform.dal.mapper.approval.DmApprovalProcessMapper;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthResMapper;
import com.clougence.clouddm.platform.dal.mapper.auth.DmAuthUserMapper;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.auth.RsAuthPersonObj;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.utils.JsonUtils;

public class ApprovalFlowServiceImplTest {

    private DmApprovalMapper        approvalMapper;
    private DmApprovalPersonMapper  personMapper;
    private DmApprovalProcessMapper processMapper;
    private DmAuthResMapper         authResMapper;
    private DmAuthUserMapper        authUserMapper;
    private ApprovalStateService    approvalStateService;
    private ApprovalFlowServiceImpl flowService;

    @Before
    public void setUp() {
        ApprovalDal approvalDal = mock(ApprovalDal.class);
        approvalMapper = mock(DmApprovalMapper.class);
        personMapper = mock(DmApprovalPersonMapper.class);
        processMapper = mock(DmApprovalProcessMapper.class);
        when(approvalDal.approvalMapper()).thenReturn(approvalMapper);
        when(approvalDal.personMapper()).thenReturn(personMapper);
        when(approvalDal.processMapper()).thenReturn(processMapper);

        AuthDal authDal = mock(AuthDal.class);
        authResMapper = mock(DmAuthResMapper.class);
        authUserMapper = mock(DmAuthUserMapper.class);
        when(authDal.resMapper()).thenReturn(authResMapper);
        when(authDal.userMapper()).thenReturn(authUserMapper);

        ChangeApprovalHandler handler = new ChangeApprovalHandler();
        ReflectionTestUtils.setField(handler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(handler, "authDal", authDal);
        QueryApprovalHandler queryHandler = new QueryApprovalHandler();
        ReflectionTestUtils.setField(queryHandler, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(queryHandler, "authDal", authDal);

        ApprovalPersonService personService = new ApprovalPersonService();
        ReflectionTestUtils.setField(personService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(personService, "authDal", authDal);

        approvalStateService = mock(ApprovalStateService.class);
        flowService = new ApprovalFlowServiceImpl(Arrays.asList(handler, queryHandler));
        ReflectionTestUtils.setField(flowService, "approvalDal", approvalDal);
        ReflectionTestUtils.setField(flowService, "authDal", authDal);
        ReflectionTestUtils.setField(flowService, "approvalStateService", approvalStateService);
        ReflectionTestUtils.setField(flowService, "approvalPersonService", personService);
    }

    @Test
    public void shouldInitializeCompleteChangeApprovalUsersBeforeCreatingApprovalProcess() {
        assertCompleteApprovalUsers(ApprovalBiz.DM_CHANGE);
    }

    @Test
    public void shouldInitializeCompleteQueryApprovalUsersBeforeCreatingApprovalProcess() {
        assertCompleteApprovalUsers(ApprovalBiz.DM_QUERY);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void assertCompleteApprovalUsers(ApprovalBiz approvalBiz) {
        DmApprovalDO ticket = new DmApprovalDO();
        ticket.setId(11L);
        ticket.setBizId("change-101");
        ticket.setOwnerUid("requester");
        ticket.setPrimaryUid("primary");
        ticket.setBindDsId(91L);
        ticket.setLevels(Collections.singletonList("schema_a"));
        ticket.setApproBiz(approvalBiz);
        ticket.setApproType(ApprovalType.Internal);
        when(approvalMapper.queryById(11L)).thenReturn(ticket);

        DmAuthUserDO primary = user("primary", "admin");
        primary.setId(1L);
        DmAuthUserDO eligible = user("eligible", "owner");
        DmAuthUserDO global = user("global", "global-user");
        DmAuthUserDO requester = user("requester", "requester-user");
        when(authUserMapper.queryByUid(anyString())).thenAnswer(invocation -> {
            String uid = invocation.getArgument(0);
            return switch (uid) {
                case "primary" -> primary;
                case "eligible" -> eligible;
                case "global" -> global;
                case "requester" -> requester;
                default -> null;
            };
        });

        RsAuthPersonObj eligiblePerson = person("eligible", "owner", //
            Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE), //
            Collections.singletonList(SecDataAuthLabel.DM_DAUTH_TICKET));
        RsAuthPersonObj missingTicketRole = person("no-role", "no-role-user", //
            Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_READ), //
            Collections.singletonList(SecDataAuthLabel.DM_DAUTH_TICKET));
        RsAuthPersonObj missingDatasourceAuth = person("no-ds-auth", "no-ds-auth-user", //
            Collections.singletonList(SecRoleAuthLabel.RDP_WORKER_ORDER_APPROVE), //
            Collections.singletonList(SecDataAuthLabel.DM_DAUTH_QUERY));
        when(authUserMapper.queryApproPerson(AccountType.SUB_ACCOUNT, 1L, 91L, "/schema_a/"))
            .thenReturn(Arrays.asList(eligiblePerson, missingTicketRole, missingDatasourceAuth));
        when(authResMapper.listEffectiveGlobalAuthUsersByPrimaryUid("primary", AuthKind.DataSource))
            .thenReturn(Collections.singletonList(global));
        when(personMapper.queryByTicketBzId("change-101")).thenReturn(Collections.emptyList());

        List<DmApprovalProcessDO> initialized = new ArrayList<>();
        when(approvalStateService.initializeProcess(eq(11L), any(), eq(ApprovalProcessStatus.INIT), any()))
            .thenAnswer(invocation -> {
                DmApprovalProcessDO process = new DmApprovalProcessDO();
                process.setId((long) initialized.size() + 1);
                process.setTicketId(11L);
                process.setTicketStage(invocation.getArgument(1));
                process.setProcessStatus(ApprovalProcessStatus.INIT);
                process.setStageContext(invocation.getArgument(3));
                initialized.add(process);
                return process;
            });

        flowService.createProcess(11L, approvalBiz, true);

        verify(authUserMapper).queryApproPerson(AccountType.SUB_ACCOUNT, 1L, 91L, "/schema_a/");
        verify(authResMapper).listEffectiveGlobalAuthUsersByPrimaryUid("primary", AuthKind.DataSource);

        ArgumentCaptor<List> personCaptor = ArgumentCaptor.forClass(List.class);
        verify(personMapper).insertPersonBatch(personCaptor.capture());
        List<DmApprovalPersonDO> inserted = personCaptor.getValue();
        assertEquals(Arrays.asList("primary", "eligible", "global"), inserted.stream().map(DmApprovalPersonDO::getPersonUid).toList());

        DmApprovalProcessDO approvalProcess = initialized.stream()//
            .filter(process -> process.getTicketStage() == ApprovalStage.APPROVAL)
            .findFirst()
            .orElseThrow();
        ApprovalStageMO stage = JsonUtils.toObj(approvalProcess.getStageContext(), ApprovalStageMO.class);
        assertEquals(Arrays.asList("admin", "owner", "global-user"), stage.getExecUserName());
    }

    private DmAuthUserDO user(String uid, String username) {
        DmAuthUserDO user = new DmAuthUserDO();
        user.setUid(uid);
        user.setUsername(username);
        return user;
    }

    private RsAuthPersonObj person(String uid, String username, List<String> roleLabels, List<String> dataLabels) {
        RsAuthPersonObj person = new RsAuthPersonObj();
        person.setUid(uid);
        person.setUsername(username);
        person.setRoleAuthLabels(roleLabels);
        person.setResAuthLabel(dataLabels);
        return person;
    }
}
