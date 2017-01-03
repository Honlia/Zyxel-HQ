package cn.superfw.framework.activiti;

import org.activiti.spring.SpringProcessEngineConfiguration;

public class ProcessEngineConfiguration
        extends SpringProcessEngineConfiguration {

    @Override
    protected void initSessionFactories() {
        super.initSessionFactories();
        super.addSessionFactory(new ProcessHistoryManagerSessionFactory());
    }
}
