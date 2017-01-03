package cn.superfw.application.workflow.service;

import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import cn.superfw.application.workflow.dto.WorkflowInfo;
import cn.superfw.framework.web.BaseBLogicService;

@Service
public class GetProcessPicService extends BaseBLogicService<WorkflowInfo, byte[]> {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    protected byte[] doService(WorkflowInfo workflowInfo) {

        byte[] processPic = null;

        String processDefinitionId = workflowInfo.getProcessDefinitionId();

        processPic = getProcessPicByteArray(processDefinitionId);

        return processPic;
    }

    private byte[] getProcessPicByteArray(String processDefinitionId) {
        byte[] imageByteArray = null;
        InputStream imageStream = null;

        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        String diagramResourceName = procDef.getDiagramResourceName();

        try {
            imageStream = repositoryService.getResourceAsStream(
                    procDef.getDeploymentId(), diagramResourceName);

            imageByteArray = StreamUtils.copyToByteArray(imageStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return imageByteArray;
    }
}
