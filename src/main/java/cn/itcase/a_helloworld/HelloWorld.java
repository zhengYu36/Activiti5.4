package cn.itcase.a_helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 这个是通过一个helloworld的例子来进行测试，
 * 主要是通过部署，实例化、和得到任务、最后是完成任务的方式来进行操作
 * @author zhengYu
 *
 */

public class HelloWorld {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();   //通过这个方式就可以得到一个流程引擎
	/**
	 * 部署流程定义
	 */
	@Test
	public void deloymentProcessDefinition(){
		Deployment deployment = processEngine.getRepositoryService()  //与流程定义和部署对象相关的Service
					.createDeployment() //创建一个部署对象
					.name("helloworld入门程序")  // //添加部署的名称
					.addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
					.addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件
					.deploy();  //完成部署
		
		System.out.println("部署ID："+deployment.getId());   //1
		System.out.println("部署名字:"+deployment.getName());  // helloworld入门程序
	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessDefinition(){
		String processDefinitionKey = "helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()  //与正在执行的流程实例和执行对象相关的Service
					.startProcessInstanceByKey(processDefinitionKey); //使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
		
		System.out.println("流程实例ID:"+processInstance.getId());   //101
		System.out.println("流程定义ID:"+processInstance.getProcessDefinitionId());  //helloworld:1:4
	}
	
	/**查询当前人的个人任务*/
	@Test
	public void findPersonalTask(){
		String assignee = "王五";  //这个是节点中的任务指定人
		List<Task> list = processEngine.getTaskService()    //与正在执行的任务管理相关的Service
					.createTaskQuery()  //创建任务查询对象
					.taskAssignee(assignee)  //指定个人任务查询，指定办理人
					.list();
		System.out.println("list size:"+list.size());
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	
	/**完成我的任务*/
	@Test
	public void completePersonalTask(){
		String taskId = "302";  //这个任务id是上面经办人的任务Id
		processEngine.getTaskService() //与正在执行的任务管理相关的Service
					.complete(taskId);
		
		System.out.println("完成任务:任务id:"+taskId);
	}
	
	/**
	注意：这里的执行顺序都是确定的，首先是张三执行，然后是张三完成自己的任务，
						   这个时候任务节点就到了李四那里了，然后是李四完成自己的任务，
						  最后是任务节点到了王五那里，然后王五完成自己的任务
						  此时该流程实例结束了,其实这就是一个流程就操作完了
	*/
}
