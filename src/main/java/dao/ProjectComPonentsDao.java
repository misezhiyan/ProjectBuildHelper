package dao;

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

	public Table tableConstruct(String tableName) {

		return sqlSession.selectOne("po.mapper.tableConstructMapper.tableConstruct", tableName);
	}

}
