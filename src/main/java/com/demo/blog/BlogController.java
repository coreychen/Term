package com.demo.blog;

import java.util.List;

import com.demo.common.model.Tag;
import com.demo.common.model.Term;
import com.demo.common.model.TermTagRel;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	
	static BlogService service = new BlogService();
	
	public void index() {
		setAttr("blogPage", service.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}
	
	public void add() {
		setAttr("tagList", service.getTags());
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void save() {
		Term term = getBean(Term.class);
		String[] tags = getParaValues("tags") ;
		term.save();
		for (int i = 0; i < tags.length; i++) {
			if (tags[i]==null || tags[i].trim().length()==0) {
				continue;
			}
			TermTagRel ttr = new TermTagRel();
			ttr.setShortName(term.getShortName());
			ttr.setTagName(tags[i]);
			ttr.save();
		}
		redirect("/blog");
	}
	
	public void edit() {
		Term term = service.findById(getPara());
		setAttr("term", term);
		setAttr("tagList", service.getTags());
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void update() {
		Term term = getBean(Term.class);
		term.update();
		int flag = 1;//1: 新的有，库里没有； 2 都有 ；3，新的没有，库里有
		String[] tags = getParaValues("tags") ;
		List<TermTagRel> list = service.getRelTags(term.getShortName());
		for (int i = 0; i < tags.length; i++) {
			flag = 1;
			for (int j = 0; j < list.size(); j++) {
				//两个数组都有的值
				if (tags[i].equals(list.get(j).getTagName())) {
					list.remove(j);
					flag=2;
					break;
				}else{
					if(j==list.size()-1)
						flag=1;
				}
			}
			//2 种情况： 1，2
			if(flag==1){
				//加入
				TermTagRel ttr = new TermTagRel();
				ttr.setShortName(term.getShortName());
				ttr.setTagName(tags[i]);
				ttr.save();
			}
		}
		//剩下的list是情况3的数据
		for (int i = 0; i < list.size(); i++) {
			list.get(i).delete();
		}
		redirect("/blog");
	}
	public void addTag(){
		try {
			Tag tag = getBean(Tag.class);
			tag.save();
			renderText("添加成功！");
		} catch (Exception e) {
			renderText(e.getMessage());
		}
		 
	}
	
	public void delete() {
		service.deleteById(getPara());
		redirect("/blog");
	}
}


