package com.everypicfound.common.util;

// @author hgj
public class DefaultTraceIdGenerator implements TraceIdGenerator {

    @Override
    // 生成单次请求 ID。
    public String generateRequestId() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    // 生成链路追踪 ID。
    public String generateTraceId() {
        throw new UnsupportedOperationException("TODO");
    }
}
