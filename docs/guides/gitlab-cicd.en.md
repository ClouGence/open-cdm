# GitLab Self-Managed Database CI/CD Integration

CloudDM can use a privately deployed GitLab instance as a database CI/CD publishing source. GitLab 19.2.x is the current
acceptance baseline. CloudDM uses REST API v4 to read projects, branches, and the repository archive for an exact
commit.

## Configure the publishing source

1. Create a Personal Access Token for a dedicated GitLab publishing account. Grant at least the `read_api` scope and
   read access to the required projects.
2. In CloudDM, open **Integrations > Git Ops** and add **GitLab**.
3. Enter the GitLab web root without `/api/v4`. HTTPS/HTTP, non-standard ports, and subpaths are supported, for example
   `https://gitlab.example.com:8443/gitlab`.
4. Enter the token and test the connection. An HTTP URL requires explicit acknowledgment that the token is transmitted
   in clear text.

For a GitLab instance signed by a private CA, import that CA into the CloudDM Console JVM trust store, for example with
`-Djavax.net.ssl.trustStore=<path>` and `-Djavax.net.ssl.trustStorePassword=<password>`. CloudDM does not provide an
option to skip TLS certificate validation.

## Configure a publishing flow and webhook

1. Create a publishing flow and select the numeric GitLab project ID, branch, SQL script directory, and target database.
2. Enable **Trigger configuration > WebHook** in the flow and copy its URL and compatibility secret.
3. Add that URL under **Settings > Webhooks** in the GitLab project.
4. On GitLab 19.1+, use **Generate signing token** and immediately save the one-time `whsec_...` value in CloudDM's
   **Signing Token** field. For older GitLab versions, enter the CloudDM compatibility secret in GitLab's
   **Secret token** field.
5. Select Push events or Merge request events to match the publishing flow. Keep the standard JSON payload and do not
   configure a custom webhook template.

A Push flow accepts only non-delete pushes to the selected target branch. A Merge Request flow accepts only requests
merged into that target branch. CloudDM uses the webhook's immutable commit SHA through audit, download, and execution,
and deduplicates requests by `webhook-id`/`Idempotency-Key` and by flow plus commit SHA.

## Security and limits

- Access tokens, webhook secrets, and signing tokens are stored in plaintext in the CloudDM metadata database. Restrict
  access to that database; these credentials are not returned in query responses, URLs, or logs.
- A request can list at most 10,000 projects and 10,000 branches.
- A repository archive is limited to 1 GiB compressed, 2 GiB extracted, and 10,000 files. Each SQL file is limited to 50
  MiB.
- SQL must be valid UTF-8. UTF-8 BOM and CRLF are supported.
- Git LFS is supported. Submodules are not downloaded recursively, and the script path cannot be inside a submodule.

See the [CloudDM documentation](https://www.cdmgr.com/docs/integrations/devops/devops_cicd_gitlab) and
the [GitLab webhook documentation](https://docs.gitlab.com/user/project/integrations/webhooks/) for complete setup
details.
