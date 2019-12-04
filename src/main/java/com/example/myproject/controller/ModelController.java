package com.example.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myproject.common.utils.ModelUtil;
import com.example.myproject.entity.ActReModel;
import com.example.myproject.entity.Page;
import com.example.myproject.entity.SystemResponse;
import com.example.myproject.service.IProcessService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.svg.SVGRenderingIntent;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-27 15:50
 **/
@Controller
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IProcessService processServiceImpl;
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/processList")
    public String processList(){
        return "process/processList";
    }

    @ResponseBody
    @RequestMapping("/getModelsPage")
    public SystemResponse getModelsPage(Page page){
        try {
            Map map =  processServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @PostMapping("/addModel")
    @ResponseBody
    public void addModel(){
        try {
        Model model = repositoryService.newModel();
        //设置一些默认信息
        String name = "newProcess";
        String description = "测试";
        int revision = 1;
        String key = "new_id";
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);

        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(model.getId(),editorNode.toString().getBytes("utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/importModel")
    @ResponseBody
    public SystemResponse importModel(@RequestBody ModelUtil modelUtil){
        try {
            Model model = repositoryService.newModel();
            InputStream in_nocode = new ByteArrayInputStream(modelUtil.getXml().getBytes("UTF-8"));
            XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlFactory.createXMLStreamReader(in_nocode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(reader);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode jsonNode =jsonConverter.convertToJson(bpmnModel);
            //设置一些默认信息
            String key = bpmnModel.getMainProcess().getId();
            ObjectNode modelNode = objectMapper.createObjectNode();
            modelNode.put(ModelDataJsonConstants.MODEL_NAME, modelUtil.getName());
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, modelUtil.getDescription());
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, modelUtil.getRevision());
            model.setName(modelUtil.getName());
            model.setKey(key);
            model.setMetaInfo(modelNode.toString());
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(),jsonNode.toString().getBytes("utf-8"));
            return new SystemResponse().success();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @PostMapping("/deployment")
    @ResponseBody
    public SystemResponse deployment(@RequestParam String id) {
        try {
            //获取模型
            Model modelData = repositoryService.getModel(id);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

            if (bytes == null) {
                return new SystemResponse().fail().message("模型数据为空，请先设计流程并成功保存，再进行发布。");
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (model.getProcesses().size() == 0) {
                return new SystemResponse().fail().message("数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            //发布流程
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            return new SystemResponse().success().message("发布流程成功");
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().error();
        }
    }
}


