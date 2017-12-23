package com.demo.blog;

import java.util.List;

import com.demo.common.model.Tag;
import com.demo.common.model.Term;
import com.demo.common.model.TermTagRel;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class BlogService {
	
	/**
	 * 所有的 dao 对象也放在 Service 中
	 */
	private static final Term dao = new Term().dao();
	private static final Tag tagDao = new Tag().dao();
	private static final TermTagRel ttrDao = new TermTagRel().dao();
	
	
	public Page<Term> paginate(int pageNumber, int pageSize) {
		return dao.paginate(pageNumber, pageSize, "select t.short_name,t.full_name,t.descb ,group_concat(tag_name) as tags ", "from term t join term_tag_rel tag on t.short_name = tag.short_name group by t.short_name,t.full_name,t.descb");
	}
	
	public Term findById(String id) {
		return dao.findFirst("select t.short_name,t.full_name,t.descb ,group_concat(tag_name) as tags from term t join term_tag_rel tag on t.short_name = tag.short_name where t.short_name='"+ id +"' group by t.short_name,t.full_name,t.descb ");
	}
	public void deleteById(String id) {
		delTags(id);
		dao.deleteById(id);
		//删除关系表
	}
	public List<Tag> getTags(){
		return tagDao.find("select * from tag ");
	}
	
	public List<TermTagRel> getRelTags(String shortName){
		return ttrDao.find("select * from term_tag_rel t where t.short_name='"+ shortName +"' ");
	}
	
	public int delTags(String termId){
		return Db.update("delete  from term_tag_rel where short_name = '"+ termId +"'");
	}
}
