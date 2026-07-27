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
package com.clougence.clouddm.team.provider.gitlab.devops;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.clougence.clouddm.sdk.scm.*;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;

final class GitlabWebhookParser {

    /**
     * Maximum allowed clock difference, in seconds, for a signed webhook.
     */
    private static final long     SIGNATURE_MAX_AGE_SECONDS = 300;

    private final GitlabApiClient apiClient;

    GitlabWebhookParser(GitlabApiClient apiClient){
        this.apiClient = apiClient;
    }

    ScmEvent readEvent(String serviceUrl, String accessToken, String password, String signingToken, Map<String, List<String>> headers, String jsonBody) {
        if (StringUtils.isBlank(jsonBody)) {
            throw new ScmWebhookException(400, "empty webhook body");
        }
        verifyWebhook(headers, jsonBody, password, signingToken, Instant.now());
        try {
            JsonNode root = JsonUtils.defaultObjectMapper().readTree(jsonBody);
            String kind = root.path("object_kind").asText();
            if ("push".equals(kind) || "tag_push".equals(kind)) {
                return readPushEvent(root, headers, "tag_push".equals(kind));
            }
            if ("merge_request".equals(kind)) {
                return readMergeRequestEvent(serviceUrl, accessToken, root, headers);
            }
            return null;
        } catch (ScmWebhookException e) {
            throw e;
        } catch (Exception e) {
            throw new ScmWebhookException(400, "malformed GitLab webhook payload");
        }
    }

    static void verifyWebhook(Map<String, List<String>> headers, String body, String secretToken, String signingToken, Instant now) {
        String signature = firstHeader(headers, "webhook-signature");
        if (StringUtils.isNotBlank(signature)) {
            verifySignedWebhook(headers, body, signingToken, now, signature);
            return;
        }
        verifySecretToken(headers, secretToken);
    }

    private static void verifySignedWebhook(Map<String, List<String>> headers, String body, String signingToken, Instant now, String signature) {
        String messageId = firstHeader(headers, "webhook-id");
        String timestamp = firstHeader(headers, "webhook-timestamp");
        if (StringUtils.isBlank(signingToken) || StringUtils.isBlank(messageId) || StringUtils.isBlank(timestamp)) {
            throw new ScmWebhookException(401, "invalid GitLab webhook signature headers");
        }
        try {
            validateSignatureTimestamp(timestamp, now);
            byte[] signingKey = decodeSigningKey(signingToken);
            byte[] expected = calculateSignature(signingKey, messageId, timestamp, body);
            if (!signatureMatches(signature, expected)) {
                throw new ScmWebhookException(401, "invalid GitLab webhook signature");
            }
        } catch (ScmWebhookException e) {
            throw e;
        } catch (Exception e) {
            throw new ScmWebhookException(401, "invalid GitLab webhook signing token");
        }
    }

    private static void validateSignatureTimestamp(String timestamp, Instant now) {
        long epochSeconds = Long.parseLong(timestamp);
        long earliest = now.getEpochSecond() - SIGNATURE_MAX_AGE_SECONDS;
        long latest = now.getEpochSecond() + SIGNATURE_MAX_AGE_SECONDS;
        if (epochSeconds < earliest || epochSeconds > latest) {
            throw new ScmWebhookException(401, "expired GitLab webhook signature");
        }
    }

    private static byte[] decodeSigningKey(String signingToken) {
        String keyValue = signingToken;
        if (signingToken.startsWith("whsec_")) {
            keyValue = signingToken.substring("whsec_".length());
        }
        byte[] key = Base64.getDecoder().decode(keyValue);
        if (key.length != 32) {
            throw new ScmWebhookException(401, "invalid GitLab webhook signing token");
        }
        return key;
    }

    private static byte[] calculateSignature(byte[] key, String messageId, String timestamp, String body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        String payload = messageId + "." + timestamp + "." + body;
        byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String signature = "v1," + Base64.getEncoder().encodeToString(digest);
        return signature.getBytes(StandardCharsets.US_ASCII);
    }

    private static boolean signatureMatches(String suppliedSignatures, byte[] expected) {
        for (String candidate : suppliedSignatures.split(" ")) {
            if (MessageDigest.isEqual(expected, candidate.getBytes(StandardCharsets.US_ASCII))) {
                return true;
            }
        }
        return false;
    }

    private static void verifySecretToken(Map<String, List<String>> headers, String secretToken) {
        String supplied = firstHeader(headers, "X-Gitlab-Token");
        if (StringUtils.isBlank(secretToken) || StringUtils.isBlank(supplied)
            || !MessageDigest.isEqual(secretToken.getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8))) {
            throw new ScmWebhookException(401, "invalid GitLab webhook secret token");
        }
    }

    private ScmEvent readPushEvent(JsonNode root, Map<String, List<String>> headers, boolean tag) {
        ScmEvent event = baseEvent(root, headers);
        event.setEventType(tag ? ScmEventType.Tag : ScmEventType.Push);
        event.setTarget(ScmEventTarget.Repository);
        event.setEventId(resolvePushCommitId(root));
        event.setTarRepoBranch(resolvePushBranch(root, tag));
        event.setStatus(resolvePushStatus(root));
        return event;
    }

    private String resolvePushCommitId(JsonNode root) {
        String commitId = root.path("checkout_sha").asText();
        if (StringUtils.isBlank(commitId) || "null".equals(commitId)) {
            return root.path("after").asText();
        }
        return commitId;
    }

    private String resolvePushBranch(JsonNode root, boolean tag) {
        String ref = root.path("ref").asText();
        String prefix = tag ? "refs/tags/" : "refs/heads/";
        return ref.startsWith(prefix) ? ref.substring(prefix.length()) : ref;
    }

    private ScmEventStatus resolvePushStatus(JsonNode root) {
        String zero = "0000000000000000000000000000000000000000";
        if (zero.equals(root.path("after").asText())) {
            return ScmEventStatus.Delete;
        }
        if (zero.equals(root.path("before").asText())) {
            return ScmEventStatus.Create;
        }
        return ScmEventStatus.Update;
    }

    private ScmEvent readMergeRequestEvent(String serviceUrl, String accessToken, JsonNode root, Map<String, List<String>> headers) {
        ScmEvent event = baseEvent(root, headers);
        JsonNode attributes = root.path("object_attributes");
        event.setEventType(ScmEventType.PullRequest);
        event.setTarget(ScmEventTarget.PullRequest);
        event.setTarRepoBranch(attributes.path("target_branch").asText());
        event.setSrcRepoBranch(attributes.path("source_branch").asText());
        populateSourceRepository(event, attributes);
        event.setTitle(attributes.path("title").asText());
        event.setBody(attributes.path("description").asText());
        event.setStatus(resolveMergeRequestStatus(attributes));
        event.setEventId(resolveMergeRequestCommit(serviceUrl, accessToken, event, attributes));
        return event;
    }

    private void populateSourceRepository(ScmEvent event, JsonNode attributes) {
        JsonNode source = attributes.path("source");
        event.setSrcRepoId(firstNonBlank(source.path("id").asText(), attributes.path("source_project_id").asText()));
        event.setSrcRepoPath(source.path("path_with_namespace").asText());
        event.setSrcRepoName(firstNonBlank(source.path("path").asText(), repoNameOf(event.getSrcRepoPath())));
    }

    private ScmEventStatus resolveMergeRequestStatus(JsonNode attributes) {
        String action = attributes.path("action").asText();
        String state = attributes.path("state").asText();
        if ("merge".equals(action) && "merged".equals(state)) {
            return ScmEventStatus.Merged;
        }
        if ("open".equals(action) || "reopen".equals(action)) {
            return ScmEventStatus.Create;
        }
        if ("close".equals(action) || "closed".equals(state)) {
            return ScmEventStatus.Closed;
        }
        return ScmEventStatus.Update;
    }

    private String resolveMergeRequestCommit(String serviceUrl, String accessToken, ScmEvent event, JsonNode attributes) {
        String commitId = firstNonBlank(attributes.path("merge_commit_sha").asText(), attributes.path("squash_commit_sha").asText());
        if (event.getStatus() == ScmEventStatus.Merged && StringUtils.isBlank(commitId)) {
            return apiClient.fetchMergedCommitId(serviceUrl, accessToken, event.getTarRepoId(), attributes.path("iid").asText());
        }
        if (StringUtils.isBlank(commitId)) {
            return attributes.path("last_commit").path("id").asText();
        }
        return commitId;
    }

    private ScmEvent baseEvent(JsonNode root, Map<String, List<String>> headers) {
        ScmEvent event = new ScmEvent();
        event.setDeliveryId(firstNonBlank(firstHeader(headers, "webhook-id"), firstHeader(headers, "Idempotency-Key"), firstHeader(headers, "X-Gitlab-Event-UUID")));
        event.setHookId(firstHeader(headers, "X-Gitlab-Webhook-UUID"));
        event.setEventTime(parseEventTime(root.path("event_created_at").asText()));
        populateEventUser(event, root);
        populateTargetRepository(event, root.path("project"));
        return event;
    }

    private void populateEventUser(ScmEvent event, JsonNode root) {
        JsonNode user = root.path("user");
        event.setUserId(firstNonBlank(user.path("id").asText(), root.path("user_id").asText()));
        event.setUserNick(firstNonBlank(user.path("name").asText(), root.path("user_name").asText()));
        event.setUserName(firstNonBlank(user.path("username").asText(), root.path("user_username").asText()));
        event.setUserEmail(firstNonBlank(user.path("email").asText(), root.path("user_email").asText()));
    }

    private void populateTargetRepository(ScmEvent event, JsonNode project) {
        String fullPath = project.path("path_with_namespace").asText();
        event.setTarRepoId(project.path("id").asText());
        event.setTarRepoPath(namespaceOf(fullPath));
        event.setTarRepoName(firstNonBlank(project.path("path").asText(), repoNameOf(fullPath)));
        event.setTarRepoUrl(project.path("web_url").asText());
    }

    private static String firstHeader(Map<String, List<String>> headers, String name) {
        if (headers == null) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            boolean headerMatches = entry.getKey() != null && entry.getKey().equalsIgnoreCase(name);
            boolean hasValues = entry.getValue() != null && !entry.getValue().isEmpty();
            if (headerMatches && hasValues) {
                return entry.getValue().get(0);
            }
        }
        return null;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value) && !"null".equalsIgnoreCase(value)) {
                return value;
            }
        }
        return null;
    }

    private static String namespaceOf(String fullPath) {
        int index = fullPath == null ? -1 : fullPath.lastIndexOf('/');
        return index < 0 ? "" : fullPath.substring(0, index);
    }

    private static String repoNameOf(String fullPath) {
        int index = fullPath == null ? -1 : fullPath.lastIndexOf('/');
        return index < 0 ? fullPath : fullPath.substring(index + 1);
    }

    private static Date parseEventTime(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                return new Date();
            }
            return Date.from(OffsetDateTime.parse(value).toInstant());
        } catch (Exception ignored) {
            return new Date();
        }
    }
}
