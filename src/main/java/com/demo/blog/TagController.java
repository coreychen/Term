package com.demo.blog;

import com.demo.common.model.Term;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */

public class TagController extends Controller {
	
	static BlogService service = new BlogService();
		
	public void add() {
		getBean(Term.class).save();
		redirect("/blog");
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void save() {
		getBean(Term.class).save();
		//save tags;
		
		redirect("/blog");
	}
	
	public void edit() {
		setAttr("blog", service.findById(getPara()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void update() {
		getBean(Term.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		service.deleteById(getPara());
		redirect("/blog");
	}
}


