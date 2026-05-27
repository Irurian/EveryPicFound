package com.everypicfound.common.executor;

import java.util.concurrent.Executor;
public class DefaultExecutorProvider implements ExecutorProvider {


    // 根据业务类型返回 Executor；MVP 可统一返回公共线程池。
    @Override
    public Executor getExecutor(ExecutorBizType bizType) {
        throw new UnsupportedOperationException("TODO");
    }
}
