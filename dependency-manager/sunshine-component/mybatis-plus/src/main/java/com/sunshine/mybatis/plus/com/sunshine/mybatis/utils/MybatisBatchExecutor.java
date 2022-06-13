package com.sunshine.mybatis.plus.com.sunshine.mybatis.utils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

import static org.mybatis.spring.SqlSessionUtils.closeSqlSession;

public class MybatisBatchExecutor {

    /**
     * 拼写成SQL的最大数据量
     * 比如： 如果insert，把batchCount条数的数据拼写成一个大SQL
     * 如果update，把batchCount条数的数据拼写成case when方式的大SQL
     */
    private static final int batchCountToSQL = 100;

    /**
     * 每多少次时开始commit
     */
    private static final int batchCountToSubmit = 100;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;


    /**
     * 批量更新、新增
     *
     * @param dbList
     * @param mapperClass
     * @param func
     * @param <T>
     * @param <M>
     */
    public <T, M> void insertOrUpdateBatch(List<T> dbList, Class<M> mapperClass, BiConsumer<M, List<T>> func) {
        int batchLastIndex = batchCountToSQL;
        int batchLastIndexToSubmit = 0;
        int total = dbList.size();

        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        M modelMapper = sqlSession.getMapper(mapperClass);
        try {
            if (total > batchCountToSQL) {
                for (int index = 0; index < total; ) {
                    if (batchLastIndex > total) {
                        List<T> list = dbList.subList(index, total);

                        func.accept(modelMapper, list);

                        sqlSession.flushStatements();
                        sqlSession.commit();
                        break;
                    } else {
                        List<T> list = dbList.subList(index, batchLastIndex);
                        func.accept(modelMapper, list);

                        if (batchLastIndexToSubmit++ >= batchCountToSubmit) {
                            //如果可以批量提交，则提交
                            sqlSession.flushStatements();
                            sqlSession.commit();
                            batchLastIndexToSubmit = 0;
                        }
                        index = batchLastIndex;// 设置下一批下标
                        batchLastIndex = index + (batchCountToSQL - 1);
                    }
                }
            } else {
                func.accept(modelMapper, dbList);
                sqlSession.commit();
            }
        } finally {
            closeSqlSession(sqlSession, sqlSessionFactory);
        }
    }

//    public static void main(String[] args) {
//        //批量插入
//        mybatisBatchExecutor.insertOrUpdateBatch(list, BatchTestMapper.class, (mapper, data)-> {
//            mapper.insertList(data);
//        });
//
////批量更新
//        mybatisBatchExecutor.insertOrUpdateBatch(list, BatchTestMapper.class, (mapper, data)-> {
//            mapper.updateBatchByPrimaryKeySelective(data);
//        });
//    }

}
