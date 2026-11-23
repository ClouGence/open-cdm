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
package com.clougence.clouddm.console.web.controller.system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth;
import com.clougence.clouddm.console.web.global.jwtsession.RequestAuth.AuthStrategy;

import jakarta.annotation.Resource;

@RestController
public class HealthController {
    @Resource
    private ConsoleClusterService cluster;

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @RequestMapping(value = "/healthcheck")
    public String healthCheck() {
        return "ok";
    }

    @RequestAuth(strategy = AuthStrategy.Ignore)
    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {
        if (cluster.isAcceptingWork()) {
            return ResponseEntity.ok("ready");
        }
        return ResponseEntity.status(503).body("not ready");
    }

}
