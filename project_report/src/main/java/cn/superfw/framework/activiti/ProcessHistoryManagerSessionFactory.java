package cn.superfw.framework.activiti;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.persistence.DefaultHistoryManagerSessionFactory;

public class ProcessHistoryManagerSessionFactory
        extends DefaultHistoryManagerSessionFactory {

    @Override
    public Session openSession() {
        return new ProcessHistoryManager();
    }

}
