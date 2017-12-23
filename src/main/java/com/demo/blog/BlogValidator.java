package com.demo.blog;

import com.demo.common.model.Term;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogValidator.
 */
public class BlogValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("term.shortName", "shortNameMsg", "请输入简称!");
		validateRequiredString("term.fullName", "fullNameMsg", "请输入!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Term.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/blog/save"))
			controller.render("add.html");
		else if (actionKey.equals("/blog/update"))
			controller.render("edit.html");
	}
}
