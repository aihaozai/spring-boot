package com.example.myproject.controller.sys;

import com.example.myproject.common.utils.ModelUtil;
import com.example.myproject.common.utils.SystemStringUtil;
import com.example.myproject.common.pojo.Page;
import com.example.myproject.common.pojo.SystemResponse;
import com.example.myproject.service.sys.IModelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-11-27 15:50
 **/
@Api(tags = "模型接口")
@Controller
@RequestMapping("/model")
public class ModelController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IModelService modelServiceImpl;
    @Autowired
    ObjectMapper objectMapper;

    @ApiOperation("模型页面接口")
    @GetMapping("/modelList")
    public String modelList(){
        return "sys/model/modelList";
    }

    @ApiOperation("模型信息页面接口")
    @GetMapping("/modelView")
    public ModelAndView modelView(String id){
        ModelAndView modelAndView = new ModelAndView("sys/model/modelView");
        modelAndView.addObject("model",new ModelUtil());
        return modelAndView;
    }

    @ApiOperation("模型分页接口")
    @ResponseBody
    @GetMapping("/getModelsPage")
    public SystemResponse getModelsPage(Page page){
        try {
            Map map =  modelServiceImpl.findListByPage(page);
            return new SystemResponse().pageData(map.get("count"),map.get("data"));
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().pageDataFail();
        }
    }

    @ApiOperation("添加模型接口:测试")
    @ResponseBody
    @PostMapping("/addModel")
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

    @ApiOperation("导入模型接口")
    @ResponseBody
    @PostMapping("/importModel")
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
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, SystemStringUtil.isEmpty(modelUtil.getDescription()));
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, modelUtil.getRevision());
            model.setName(modelUtil.getName());
            model.setKey(key);
            model.setMetaInfo(modelNode.toString());
            model.setVersion(modelUtil.getRevision());
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(),jsonNode.toString().getBytes("utf-8"));
            return new SystemResponse().success();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().message(e.getMessage());
        }
    }

    @ApiOperation("发布模型接口")
    @ApiImplicitParam(name = "id", value = "模型部署id",required = true)
    @ResponseBody
    @PostMapping("/deployment")
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
            return new SystemResponse().fail().data(e.getMessage());
        }
    }


    @ApiOperation("删除模型接口")
    @ApiImplicitParam(name = "id", value = "模型部署id",required = true)
    @ResponseBody
    @PostMapping("/delModel")
    public SystemResponse delModel(@RequestParam String id) {
        try {
            repositoryService.deleteModel(id);
            return new SystemResponse().success().delMsgSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return new SystemResponse().fail().data(e.getMessage());
        }
    }
}


