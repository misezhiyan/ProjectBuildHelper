package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import frame.SqlSessionFactoryUtil;
import po.Table;

/**
 * @discription
 * @author kimmy
 * @date 2018年9月29日 下午5:14:32
 */
public class ProjectComPonentsDao {

	private SqlSession sqlSession = SqlSessionFactoryUtil.getSqlSession();

	public List<Table> tableConstruct(Map<String, String> params_tableConstruct) {

		return sqlSession.selectList("po.mapper.tableConstructMapper.tableConstruct", params_tableConstruct);
	}

}
