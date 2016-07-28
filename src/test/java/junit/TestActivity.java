package junit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;


/**
 * 关于生成activity的23张表，通过流程引擎
 * @author zhengYu
 *
 */
public class TestActivity {
	
	@Test
	public void createProcessEnginee(){
		System.out.println("生成流程引擎");
		//首先得到流程引擎的配置
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/itcase160720?useUnicode=true&characterEncoding=utf8");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("root");
		
		/**
		 * 关于通过java去操作数据库中表
		 * 	public static final String DB_SCHEMA_UPDATE_FALSE = "false";
		 *  public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
		 *  public static final String DB_SCHEMA_UPDATE_TRUE = "true";
		 */
		//对数据库表中的操作
		processEngineConfiguration.setDatabaseSchemaUpdate(processEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		//然后构建流程引擎
		//工作流的核心对象，ProcessEnginee对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("processEngine is:"+processEngine);
	}
	
	
	//下面这个是通过spring的方式，也就是说吧那样用户名密码什么的都通过spring来注入了
	@Test
	public void createProcessEnginee_2(){
		
/*		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		//工作流的核心对象，ProcessEnginee对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();*/
		
		//然后下面通过多个链接的方式哈，就是上面那两步，现在只用一步的方式
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		System.out.println("processEngine 2 is:"+processEngine);
	}
}
