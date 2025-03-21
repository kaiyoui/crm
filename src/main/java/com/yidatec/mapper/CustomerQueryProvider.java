package com.yidatec.mapper;

import com.yidatec.vo.CustomerVO;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/7/19.
 */
public class CustomerQueryProvider {
    public String selectCustomer(final CustomerVO customerVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT MAX(fh.followTime) as followTime,fh.nextTime,C.name as creator,D.createTime AS createTime1,D.*,U2.`NAME` AS ownerName  FROM T_CUSTOMER  as D " +
                " LEFT JOIN T_CUSTOMER_HISTORY ch on D.id = ch.customerId " +
                " LEFT JOIN T_FOLLOW_HISTORY fh on fh.id = ch.historyId" +
                " LEFT JOIN T_USER C on D.creatorId=C.id LEFT JOIN T_USER AS U2 ON U2.ID = D.ownerId WHERE 1=1 ");

        if(!StringUtils.isEmpty(customerVO.getName())){
            sb.append(" AND D.name LIKE CONCAT('%',#{name},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getNature())){
            sb.append(" AND D.nature = #{nature}");
        }
        if(!StringUtils.isEmpty(customerVO.getAddress())){
            sb.append(" AND D.address LIKE CONCAT('%',#{address},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getProvince())){
            sb.append(" AND D.province LIKE CONCAT('%',#{province},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getCity())){
            sb.append(" AND D.city LIKE CONCAT('%',#{city},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getIndustry())){
            sb.append(" AND D.industry = #{industry}");
        }
        if(!StringUtils.isEmpty(customerVO.getFollowStartTime())&&!StringUtils.isEmpty(customerVO.getFollowEndTime())){
            sb.append(" AND (Date(followTime) between Date(#{followStartTime}) and  Date(#{followEndTime}))");
        }
        if(!StringUtils.isEmpty(customerVO.getNextStartTime())&&!StringUtils.isEmpty(customerVO.getNextEndTime())){
            sb.append(" AND (Date(nextTime) between Date(#{nextStartTime}) and  Date(#{nextEndTime}))");
        }
        if(!StringUtils.isEmpty(customerVO.getState())){
            sb.append(" AND D.state = #{state}");
        }

        if(!StringUtils.isEmpty(customerVO.getCreator())){// 创建人
            sb.append(" AND C.name LIKE CONCAT('%',#{creator},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getOwnerName())){// 所有者条件
            sb.append(" AND U2.`name` LIKE CONCAT('%',#{ownerName},'%')");
        }
        if(customerVO.getIsAll() == 0){ // 查看我的客户
            if(!StringUtils.isEmpty(customerVO.getCreatorId())){
//                sb.append(" AND D.creatorId = #{creatorId}");
                sb.append(" AND D.ownerId = #{creatorId}");// 别人分配给你的客户你自己能看到.
            }
        }

        sb.append(" GROUP BY D.id ORDER BY D.modifyTime DESC LIMIT #{start},#{length}");
        return sb.toString();
    }
    public String countCustomer(final CustomerVO customerVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(a.id) FROM( SELECT MAX(fh.followTime) as followTime,C.name as creator,D.*,U2.`NAME` AS ownerName FROM T_CUSTOMER  as D " +
                " LEFT JOIN T_CUSTOMER_HISTORY ch on D.id = ch.customerId " +
                " LEFT JOIN T_FOLLOW_HISTORY fh on fh.id = ch.historyId" +
                " LEFT JOIN T_USER C on D.creatorId=C.id LEFT JOIN T_USER AS U2 ON U2.ID = D.ownerId WHERE 1=1 ");

        if(!StringUtils.isEmpty(customerVO.getName())){
            sb.append(" AND D.name LIKE CONCAT('%',#{name},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getNature())){
            sb.append(" AND D.nature = #{nature}");
        }
        if(!StringUtils.isEmpty(customerVO.getFollowStartTime())&&!StringUtils.isEmpty(customerVO.getFollowEndTime())){
            sb.append(" AND (Date(followTime) between Date(#{followStartTime}) and  Date(#{followEndTime}))");
        }
        if(!StringUtils.isEmpty(customerVO.getAddress())){
            sb.append(" AND D.address LIKE CONCAT('%',#{address},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getProvince())){
            sb.append(" AND D.province LIKE CONCAT('%',#{province},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getCity())){
            sb.append(" AND D.city LIKE CONCAT('%',#{city},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getIndustry())){
            sb.append(" AND D.industry = #{industry}");
        }
        if(!StringUtils.isEmpty(customerVO.getState())){
            sb.append(" AND D.state = #{state}");
        }
        if(!StringUtils.isEmpty(customerVO.getCreator())){
            sb.append(" AND C.name LIKE CONCAT('%',#{creator},'%')");
        }
        if(!StringUtils.isEmpty(customerVO.getOwnerName())){// 所有者条件
            sb.append(" AND U2.`name` LIKE CONCAT('%',#{ownerName},'%')");
        }
        if(customerVO.getIsAll() == 0){
            if(!StringUtils.isEmpty(customerVO.getCreatorId())){
//                sb.append(" AND D.creatorId = #{creatorId}");
                sb.append(" AND D.ownerId = #{creatorId}");// 别人分配给你的客户你自己能看到.
            }
        }

        sb.append(" GROUP BY D.id ORDER BY D.modifyTime DESC ) a");
        return sb.toString();
    }

}
