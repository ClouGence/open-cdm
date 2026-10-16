## Fixed

- Fixed premature validation of lazily loaded SSL files so CA certificates, truststores, keystores, and client certificates are validated after loading completes ([#278](https://github.com/ClouGence/open-cdm/issues/278), [#311](https://github.com/ClouGence/open-cdm/issues/311)).
- Fixed WebSocket reconnection after a service restart so the SQL workspace reconnects automatically when the backend recovers, while authentication failures still stop reconnection ([#303](https://github.com/ClouGence/open-cdm/issues/303)).
- Fixed table-editor DDL generation losing column default options, Dameng column defaults being unavailable for editing, and qualified identifiers being escaped twice ([#313](https://github.com/ClouGence/open-cdm/issues/313), [#314](https://github.com/ClouGence/open-cdm/issues/314)).
- Removed the obsolete Message Center entry, legacy Console Job pages, and polling logic so users are not shown a non-functional feature, while preserving the current async-task dock behavior ([#315](https://github.com/ClouGence/open-cdm/issues/315)).
- Fixed standalone initialization tasks missing required platform services, which could prevent startup recovery with an existing configuration ([#317](https://github.com/ClouGence/open-cdm/issues/317)).
