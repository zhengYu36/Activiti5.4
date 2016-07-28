package cn.itcase.d_processVariables;

import java.io.Serializable;

/**
 * 关于流程变量的时候用实体来测试看看是否也可以参数哈
 * @author zhengYu
 *
 */
public class Person implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7362489874722930948L;  //加了这个版本后，后面我在添加其他的字段也是没有影响的哈
	
		private String like;
		
		
	
		public String getLike() {
			return like;
		}
		public void setLike(String like) {
			this.like = like;
		}
		private Integer id;  //编号
		private String name;  //姓名
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
}
