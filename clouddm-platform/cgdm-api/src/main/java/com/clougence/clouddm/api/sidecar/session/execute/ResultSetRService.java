package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.resultset.file.DmFileType;

@RSocketApiClass
public interface ResultSetRService {

    String convertFile(RSocketSendDTO dto, String puid, String userId, String srcFileId, String exportId, DmFileType dmFileType, String srcFile, String dstFile, String formatName,
                       String option);

    void deleteFile(RSocketSendDTO sendDTO, String filePath, boolean tempFile);

    long fileSize(RSocketSendDTO sendDTO, String filePath);

    ResultFileReadDTO fileRead(RSocketSendDTO sendDTO, String filePath, long offset, int length);

    ResultPageDTO resultPageRead(RSocketSendDTO sendDTO, String filePath, long rowOffset, int pageSize);

    ResultColDTO resultDataRead(RSocketSendDTO sendDTO, String filePath, long rowNumber, long colNumber, long offset, int length);
}
