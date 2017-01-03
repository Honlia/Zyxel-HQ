package cn.superfw.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseBLogicService<P, R> implements BLogicService<P, R> {

    private static final Logger log = LoggerFactory.getLogger(BaseBLogicService.class);

    /**
     * 业务逻辑执行主方法
     */
    @Override
    @Transactional
	public R execute(P param) {

        doPreExecute(param);
        log.info("---->主处理开始↓↓↓↓↓↓↓↓↓↓");
        R result = doService(param);
        log.info("---->主处理结束↑↑↑↑↑↑↑↑↑↑");
        doPostExecute(param, result);

        return result;

    }

    /**
     * 实际业务执行的模板方法，在子类中实现
     * @param param 参数
     * @return 返回结果
     */
    protected abstract R doService(P param);

    /**
     * 业务逻辑执行前处理
     * @param param 参数
     */
    protected void doPreExecute(P param) {
        log.info("----------前处理执行↓↓↓↓↓↓↓↓↓↓");
    }

    /**
     * 业务逻辑执行后处理
     * @param param 参数
     * @return 返回结果
     */
    protected void doPostExecute(P param, R result) {
        log.info("----------后处理执行↑↑↑↑↑↑↑↑↑↑");
    }

}
