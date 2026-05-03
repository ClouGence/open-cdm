package com.clougence.rdp.service.openapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.rdp.service.openapi.model.*;

public interface RdpDsOpenApiService {

    List<ApiDataSourceVO> listDs(String requestId, String puid, ApiListDsFO fo);

    ApiDataSourceVO queryDs(String puid, ApiQueryDsFO fo);

    ResWebData<Long> addDs(String data, MultipartFile securityFile, MultipartFile secretFile, String uid, String puid);

    void deleteDs(String puid, ApiDeleteDsFO fo);

    void updateDsDesc(String puid, ApiUpdateDsDescFO fo);

    void updateAccountAndPasswd(String data, MultipartFile securityFile, MultipartFile secretFile, String puid);

    void updateAliyunRdsAkSk(String puid, ApiUpdateAliyunRdsAkSkFO fo);

    void updatePrivateHost(String puid, ApiUpdatePriHostFO fo);

    void updatePublicHost(String puid, ApiUpdatePubHostFO fo);

    void cleanDsAccount(String puid, ApiDeleteAccountFO fo);

    List<ApiDsKvConfigVo> listDsKvConfs(String puid, ApiListDsKvConfigsByDsIdFO fo);

    void upsertDsKvConfs(String puid, ApiUpsertDsKvConfigFO fo);
}
