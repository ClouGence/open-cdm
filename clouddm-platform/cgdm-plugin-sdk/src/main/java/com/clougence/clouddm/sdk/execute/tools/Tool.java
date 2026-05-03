package com.clougence.clouddm.sdk.execute.tools;

public interface Tool extends AutoCloseable {

    void init(ToolSessionContextDTO contextDTO) throws Exception;

    ToolResultDTO tailLog(ToolRequestDTO requestDTO);

    ToolResultDTO tailStatus(ToolRequestDTO requestDTO);

    ToolResultDTO invoke(String methodKey, ToolRequestDTO requestDTO);
}
