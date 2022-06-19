package com.sunshine.utils.web;

import cn.hutool.extra.spring.SpringUtil;
import com.sunshine.utils.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;

@Slf4j
public class WebAsyncUtils {

    private static final ThreadPoolTaskExecutor executor;

    static {
        executor = SpringUtil.getBean("taskExecutor",ThreadPoolTaskExecutor.class);
    }

    /**
     * 移除初始化
     *
     * @param callable
     * @return
     */
    public static final WebAsyncTask<Result> instance(Callable callable) {
        WebAsyncTask<Result> asyncTask = new WebAsyncTask(2000L, executor, callable);
        defaultConfiguration(asyncTask);
        return asyncTask;
    }

    private static void defaultConfiguration(WebAsyncTask asyncTask) {
        asyncTask.onCompletion(() -> log.info("任务执行完成"));
        asyncTask.onError(() -> Result.fail("任务超失败"));
        asyncTask.onTimeout(() -> Result.fail("任务超时"));
    }

    /**
     * 移除初始化
     *
     * @param callable
     * @return
     */
    public static final WebAsyncTask<String> instanceString(Callable callable) {
        WebAsyncTask<String> asyncTask = new WebAsyncTask(2000L, executor, callable);
        defaultConfiguration(asyncTask);
        return asyncTask;
    }

}
