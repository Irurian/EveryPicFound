package com.everypicfound.common.util;

// @author hgj
public interface TraceIdGenerator {

    // 生成单次请求 ID。
    String generateRequestId();

    // 生成链路追踪 ID。
    String generateTraceId();
}
