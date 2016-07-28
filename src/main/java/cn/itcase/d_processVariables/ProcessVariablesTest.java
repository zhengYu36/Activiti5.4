package cn.itcase.d_processVariables;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 关于流程变量的测试类
 * @author zhengYu
 *
 */

public class ProcessVariablesTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();   //通过这个方式就可以得到一个流程引擎
	/**
	 * 部署流程定义 (从inputstream)
	 */
	@Test
	public void deloymentProcessDefinition(){
		
		/**
		 * 这个是充classpath路径下面去获取资源的
		 * 注意这个classpath就是从src/main/resources下面去开始寻找资源的
		 */
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		
		Deployment deployment = processEngine.getRepositoryService()  //与流程定义和部署对象相关的Service
					.createDeployment() //创建一个部署对象
					.name("流程变量3")  // //添加部署的名称
					.addInputStream("processVariables.bpmn", inputStreamBpmn) //使用资源文件的名称（要求：与资源文件的名称要一致），和输入流完成部署
					.addInputStream("processVariables.png", inputStreamPng) //使用资源文件的名称（要求：与资源文件的名称要一致），和输入流完成部署
					.deploy();  //完成部署
		
		System.out.println("部署ID："+deployment.getId());   //
		System.out.println("部署名字:"+deployment.getName());  // 
	}
	
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		//流程定义的key
		String processDefinitionKey = "processVariables";  
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
						.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:"+pi.getId());//流程实例ID    
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID  
	}
	
	/**设置流程变量*/
	@Test
	public void setVariables(){
		/**与任务（正在执行）*/
		//任务id
		String taskId = "2302";
		/**一：设置流程变量，使用基本数据类型*/
		TaskService taskService = processEngine.getTaskService();
  /*	taskService.setVariableLocal(taskId, "请假天数", 3);  //与任务ID绑定
		taskService.setVariable(taskId, "请假日期",new Date());
		taskService.setVariable(taskId, "请假原因", "回家探亲");*/
		
		/**二：javaben的方式*/
		/**
		 * 当一个javabean（实现序列号）放置到流程变量中，要求javabean的属性不能再发生变化
		 *    * 如果发生变化，再获取的时候，抛出异常
		 *  
		 * 解决方案：在Person对象中添加：
		 * 		private static final long serialVersionUID = 6757393795687480331L;
		 *      同时实现Serializable 
		 * */
		Person p = new Person();
		p.setId(1002);
		p.setName("小宇2");
		taskService.setVariable(taskId, "人员信息", p);
		
		System.out.println("设置流程变量成功！");
	}
	
	/**获取流程变量*/
	@Test
	public void getVariables(){
		/**与任务（正在执行）*/
		TaskService taskService = processEngine.getTaskService();
		//任务id
		//String taskId = "1604";  //第一个任务节点，执行后这个就不存在了，如果要查找以前的数据，那么就需要去查询流程变量的历史表了
		//String taskId = "1802";   //1802是第二个任务节点任务id，这里看看这个是否可以得到数据哈，恩，是可以得到的哈
		String taskId = "2302";
		/**一：获取流程变量，使用基本数据类型*/
/*		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		Date date = (Date) taskService.getVariable(taskId, "请假日期");
		String reason = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+reason);*/
		/**二：获取流程变量，使用javabean类型*/
		Person person = (Person) taskService.getVariable(taskId, "人员信息");
		System.out.println("id:"+person.getId());
		System.out.println("name:"+person.getName());
	}
	
	/**模拟流程变量设置和获取场景*/
	public void setAndGetVariables(){
		
		/**与流程实例，执行对象（正在执行）*/
		RuntimeService runtimeService = processEngine.getRuntimeService();	
		/**与任务（正在执行）*/
		TaskService taskService = processEngine.getTaskService();
		
		/**设置流程变量*/
//		runtimeService.setVariable(executionId, variableName, value)//表示使用执行对象ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
//		runtimeService.setVariables(executionId, variables)//表示使用执行对象ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
		
//		taskService.setVariable(taskId, variableName, value)//表示使用任务ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
//		taskService.setVariables(taskId, variables)//表示使用任务ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）
		
//		runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时，可以设置流程变量，用Map集合
//		taskService.complete(taskId, variables)//完成任务的同时，设置流程变量，用Map集合
		
		/**获取流程变量*/
//		runtimeService.getVariable(executionId, variableName);//使用执行对象ID和流程变量的名称，获取流程变量的值
//		runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//		runtimeService.getVariables(executionId, variableNames);//使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
		
//		taskService.getVariable(taskId, variableName);//使用任务ID和流程变量的名称，获取流程变量的值
//		taskService.getVariables(taskId);//使用任务ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
//		taskService.getVariables(taskId, variableNames);//使用任务ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中
	
	}
	
	/**完成我的任务
	 * 这里可以测试下，完成任务后，看看下一个人是否可以得到流程变量
	 * */
	@Test
	public void completeMyPersonalTask(){
		//任务ID
		String taskId = "2104";
		processEngine.getTaskService()//与正在执行的任务管理相关的Service
					.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	/**查询流程变量的历史表
	 * 
	 * 所有说，在一个流程里面一些，如果是一个任务节点特有的变量，那么最好就用setVariableLocal
	 * 如果不用这个，如果下一个任务节点的变量名和上一个重复了，那么就会被覆盖掉的，那么就找不回来了哈
	 * 当然这个呢，是到时候根据需求来定哈
	 * */
	@Test
	public void findHistoryVariables(){
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
					.createHistoricVariableInstanceQuery() //创建一个历史的流程变量查询对象
					.variableName("请假天数")
					.list();
		
		if(list !=null && list.size()>0){
			for(HistoricVariableInstance hvi:list){
				System.out.println("id:"+hvi.getId());
				System.out.println("taskId:"+hvi.getTaskId());
				System.out.println("value:"+hvi.getValue());
				System.out.println("###########################");
			}
		}
	}
}
