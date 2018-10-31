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

		return sqlSession.selectList("po.mapper.tableMapper.tableConstruct", params_tableConstruct);
	}

	public List<String> allTables() {

		return sqlSession.selectList("po.mapper.tableMapper.allTables");
	}

	public Map<String, String> createSql(String table) {

		return sqlSession.selectOne("po.mapper.tableMapper.createSql", table);
	}

	public int createOneTable(String createSql) {

		return sqlSession.insert("po.mapper.tableMapper.createOneTable", createSql);
	}

}
