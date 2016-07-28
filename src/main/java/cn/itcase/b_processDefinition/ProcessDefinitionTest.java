package cn.itcase.b_processDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;


/**
 * 流程定义的测试类
 * 说明:这个主要是关于流程定义图的crud，就是说那个流程图部署后，要进行的crud的一系列操作
 * 2016年7月22日14:17:34
 * @author zhengYu
 *
 */
public class ProcessDefinitionTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); 
	
	/**部署流程定义（从classpath）*/
	@Test
	public void deploymentProcessDefinition_classpath(){
		Deployment deployment = processEngine.getRepositoryService()  //与流程定义和部署对象相关的Service
				.createDeployment() //创建一个部署对象
				.name("流程定义实例")  // //添加部署的名称
				.addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件
				.addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件
				.deploy();  //完成部署
	
	System.out.println("部署ID："+deployment.getId());   //1
	System.out.println("部署名字:"+deployment.getName());  // helloworld入门程序
	}
	
	
	/**部署流程定义（从zip）*/
	@Test
	public void deploymentProcessDefinition_zip(){
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()  //与流程定义和部署对象相关的Service
				.createDeployment() //创建一个部署对象
				.name("流程定义实例2")  // //添加部署的名称
				.addZipInputStream(zipInputStream)
				.deploy();  //完成部署
	
	System.out.println("部署ID："+deployment.getId());   //1
	System.out.println("部署名字:"+deployment.getName());  // helloworld入门程序
	}
	
	/**
	 * 查询流程定义
	 * 详细说明 :流程定义的查询，就是查询部署的流程定义(就是说你部署了好几个流程图，现在想来查询它，其实就是这个意思)
	 * 其实说到底,这个就是对act_re_procdef这个表的查询(包括了where和排序，最后还有那个分页哦，功能非常强大)
	 */
	@Test
	public void findProcessDefinition(){
		
		//查询流程部署的信息
//		List<Deployment> list = processEngine.getRepositoryService()  //这个服务就是关于流程定义和部署相关的Service
//					.createDeploymentQuery()  //流程部署查询 (这个是查询的部署表)
//					.deploymentNameLike("流程定义实例")    //部署的名字 （通过模糊查询） ？这里这个模糊查询好像有问题？
//					.list();  //返回成List
		
		
		String deploymentId = "601";
		//查询流程定义的信息 （其实和部署的差不多，只是用的那个查询要修改一下）
/*		ProcessDefinition processDefinition= processEngine.getRepositoryService()
					.createProcessDefinitionQuery()   //查询流程定义
					*//**指定查询条件,where条件*//*
					.deploymentId(deploymentId)  //流程部署id
					
					*//**排序*//*
					//.orderByDeploymentId()
					
					*//**返回结果集*//*
					//.list();  //其实这个只能是一个的就不能用list???
					.singleResult();
					
		System.out.println(processDefinition.getName());*/

		//下面的一个查询是比较全面的查询哈
		List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
				.createProcessDefinitionQuery()//创建一个流程定义的查询
				/**指定查询条件,where条件*/
//				.deploymentId(deploymentId)//使用部署对象ID查询
//				.processDefinitionId(processDefinitionId)//使用流程定义ID查询
//				.processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
//				.processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询
				
				/**排序*/
				.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
//				.orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列
				
				/**返回的结果集*/
				.list();//返回一个集合列表，封装流程定义
//				.singleResult();//返回惟一结果集
//				.count();//返回结果集数量
//				.listPage(firstResult, maxResults);//分页查询
				if(list!=null && list.size()>0){
					for(ProcessDefinition pd:list){
						System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
						System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
						System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
						System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
						System.out.println("资源名称bpmn文件:"+pd.getResourceName());
						System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
						System.out.println("部署对象ID："+pd.getDeploymentId());
						System.out.println("#########################################################");
					}
				}		
		
	}
	
	/**
	 * 流程定义的删除  - 晕死了，这个
	 * 不应该叫流程的定义的删除，应该叫流程部署的删除，其实没关心，这个在删除流程部署的时候，也会把流程定义里面的数据给删除掉
	 */
	@Test
	public void delProcessDefinition(){		//使用部署ID，完成删除
		String deploymentId = "601";
		/**
		 * 不带级联的删除
		 *    只能删除没有启动的流程，如果流程启动，就会抛出异常
		 */
//		processEngine.getRepositoryService()//
//						.deleteDeployment(deploymentId);
		
		/**
		 * 级联删除
		 * 	  不管流程是否启动，都能可以删除-这个还是很有用处的哈,这个类似于有点强制删除的意味
		 */
		processEngine.getRepositoryService()//
						.deleteDeployment(deploymentId,true);
		System.out.println("删除成功！");
	}
	
	/**
	 * 查看流程图
	 * 说明:这里的查看指的是从数据库中把流程定义的图片资源，现在到自己的本地
	 * @throws IOException 
	 */
	@Test
	public void viewPic() throws IOException{
	
		String deploymentId = "1";
		
		//得到流程定义的资源名
		List<String> list = processEngine.getRepositoryService()
					.getDeploymentResourceNames(deploymentId);
		
		//定义图片资源的名称
		String resourceName = "";
		if(list!=null && list.size()>0){
			for(String name:list){
				if(name.indexOf(".png") >=0){
					resourceName = name;
				}
			}
		}
		
		
		//火腿流程定人的输入流
		InputStream  in = processEngine.getRepositoryService()
					.getResourceAsStream(deploymentId, resourceName);
		
		//将图片生成到d盘
		File file = new File("D:/"+resourceName);
		
		//将输出流的图片写到D盘下
		FileUtils.copyInputStreamToFile(in, file);
	}
	
	/***附加功能：查询最新版本的流程定义*/
	@Test
	public void findLastVersionProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()   //与流程部署和定义相关的service
					.createProcessDefinitionQuery()   //关于流程定义的查询
					.orderByProcessDefinitionVersion().asc()  //根据版本来啊进行升序查询
					.list();   //现在这个里面就有多个版本的helloworld存在哈
		
		/**
		 * Map<String,ProcessDefinition>
  map集合的key：流程定义的key
  map集合的value：流程定义的对象
  map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
		 */
		Map<String, ProcessDefinition> map = new HashMap<String, ProcessDefinition>();  //这里的map都是可以用的，因为上面已经进行了排序了
		if(list !=null && list.size()>0){
			for(ProcessDefinition pd:list){
				map.put(pd.getKey(), pd);  //这里这个key是helloworld.bpmn中的id，我们一般取得是这个数据
			}
		}
		
		
		//把map 的value转换为list(这里我们目前只是需要map中的value值哈)
		List<ProcessDefinition> pdlist = new ArrayList<>(map.values());
		
		
		
		//打印输出结果
		if(pdlist!=null && pdlist.size()>0){
			for(ProcessDefinition pd:pdlist){
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:"+pd.getResourceName());
				System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID："+pd.getDeploymentId());
				System.out.println("#########################################################");
			}
		}	
	}
	
	/**附加功能：删除流程定义（删除key相同的所有不同版本的流程定义）
	 * 可以直接删除流程定义表的key(就是流程图中的id),没有那种操作，所有只有通过循环，如何再去删除哈
	 * */
	@Test
	public void deleteProcessDefinitionByKey(){
		
		//定义流程定义的key
		String processDefinitionKey = "helloworld";  //这个是确定的，就是流程图中的那个id哈
		
		//首先通过流程定义的Key,查询所有的流程定义版本
		List<ProcessDefinition> list = processEngine.getRepositoryService()
					.createProcessDefinitionQuery()
					.processDefinitionKey(processDefinitionKey)
					.list();
		
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				
				String deploymentId = pd.getDeploymentId();
				
				//如何得到那个部署id如何去删除哈
				processEngine.getRepositoryService()
							.deleteDeployment(deploymentId);  //这个部署id
			}
		}
	}
	
}
