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
package com.clougence.clouddm.platform.dal.mapper.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;

public interface DmSysConsoleMapper extends BaseMapper<DmSysConsoleDO> {

    int insertConsole(DmSysConsoleDO console);

    int replaceExpiredConsole(DmSysConsoleDO console);

    DmSysConsoleDO queryAliveByConsoleInstanceId(String consoleInstanceId);

    int updateStateAndLeaseUntil(@Param("instanceId") String instanceId, @Param("state") String state);

    int updateStateAndLeaseUntilExcludingState(@Param("instanceId") String instanceId, @Param("state") String state, @Param("excludedState") String excludedState);

    DmSysConsoleDO queryByInstanceIdAndLeaseUntilAfterNow(String instanceId);

    List<DmSysConsoleDO> listByStateAndLeaseUntilAfterNow(String state);

    List<DmSysConsoleDO> listConsolesWithStatus();

    String querySchedulerOwnerInstanceId();

    int updateSchedulerOwnerInstanceId(@Param("expectedOwnerInstanceId") String expectedOwnerInstanceId, @Param("ownerInstanceId") String ownerInstanceId);

    int updateStateAndLeaseUntilToNow(@Param("instanceId") String instanceId, @Param("state") String state);
}
