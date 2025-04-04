package com.yidatec.mapper;

import com.yidatec.vo.ProjectVO;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/8/17.
 */
public class ProjectQueryProvider {

    public String selectProject(final ProjectVO projectVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT GROUP_CONCAT(f.name) as designerName,b.name as customerName,c.name as campaignName,c.type as campaignType,c.startDate,c.endDate,D.*,DU.`name` as  developSaleName,U3.`NAME` AS ownerName " +
                "FROM T_PROJECT  as D LEFT JOIN T_CUSTOMER b " +
                "on D.customerId=b.id " +
                "LEFT JOIN T_PROJECT_DESIGNER a "+
                "on D.id =a.projectId "+
                "LEFT JOIN T_USER f "+
                "on a.designerId = f.id "+
                "LEFT JOIN T_CAMPAIGN c " +
                "on D.campaignId = c.id  " +
                "left join T_USER DU on DU.id = D.developSaleId " +
                "left join T_USER U3 on U3.id = D.ownerId " +
                "WHERE 1=1");

        if(!StringUtils.isEmpty(projectVO.getName())){
            sb.append(" AND D.name LIKE CONCAT('%',#{name},'%')");
        }
        if(!StringUtils.isEmpty(projectVO.getCampaignType())){
            sb.append(" AND c.type = #{campaignType}");// 活动类型,不是项目类型别弄混了
        }
        if(!StringUtils.isEmpty(projectVO.getActivityTime())){
            sb.append(" AND (Date(#{activityTime}) between Date(c.startDate) and  Date(c.endDate))");
        }
        if(!StringUtils.isEmpty(projectVO.getCode())){
            sb.append(" AND D.code LIKE CONCAT('%',#{code},'%')");
        }
        if(!StringUtils.isEmpty(projectVO.getDegreeOfImportance())){
            sb.append(" AND D.degreeOfImportance = #{degreeOfImportance}");
        }
        if(!StringUtils.isEmpty(projectVO.getPmId())){
            sb.append(" AND D.pmId = #{pmId}");
        }

        if(!StringUtils.isEmpty(projectVO.getPotential())){
            sb.append(" AND D.potential = #{potential}");
        }

        if(!StringUtils.isEmpty(projectVO.getDevelopSaleId())){
            sb.append(" AND D.developSaleId = #{developSaleId}");
        }
        if(!StringUtils.isEmpty(projectVO.getClosingTime())){
            sb.append(" AND D.closingTime = Date(#{closingTime})");
        }

        if (!StringUtils.isEmpty(projectVO.getDesignProgress())){
            sb.append(" and find_in_set(#{designProgress},D.designProgress)");
        }

        if(!StringUtils.isEmpty(projectVO.getState())){
            sb.append(" AND D.state = #{state}");
        }
        //
        if(!StringUtils.isEmpty(projectVO.getSearch())){
            sb.append(" AND D.name LIKE CONCAT('%',#{search},'%') OR D.code LIKE CONCAT('%',#{search},'%') OR c.name LIKE CONCAT('%',#{search},'%')");
        }

        if(!StringUtils.isEmpty(projectVO.getDesignerIdVO())){
            sb.append(" AND #{designerIdVO} in (SELECT pd.designerId FROM T_PROJECT p LEFT JOIN T_PROJECT_DESIGNER pd on p.id = pd.projectid where D.id = p.id)");
        }
        if(!StringUtils.isEmpty(projectVO.getOwnerName())){// 所有者条件
            sb.append(" AND U3.`name` LIKE CONCAT('%',#{ownerName},'%')");
        }
        if(projectVO.getIsAll() == 0){//是我的
            sb.append(" AND (");
            sb.append(" D.pmId = #{id}");
            sb.append(" or D.developSaleId = #{id}");
            sb.append(" or D.traceSaleId = #{id}");
            sb.append(" or D.ownerId = #{id}");// 别人分配给你的客户你自己能看到.
            sb.append(" OR #{id} in (SELECT pd.designerId FROM T_PROJECT p LEFT JOIN T_PROJECT_DESIGNER pd on p.id = pd.projectid where D.id = p.id)");
            sb.append(" )");
        }
        sb.append("  GROUP BY D.id ORDER BY D.modifyTime DESC LIMIT #{start},#{length} ");
        return sb.toString();
    }
    public String countProject(final ProjectVO projectVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(*) FROM T_PROJECT  as D LEFT JOIN T_CUSTOMER b\n" +
                "on D.customerId=b.id\n" +
                "LEFT JOIN T_CAMPAIGN c\n" +
                "on D.campaignId = c.id " +
                "left join T_USER DU on DU.id = D.developSaleId " +
                "left join T_USER U3 on U3.id = D.ownerId " +
                "WHERE 1=1");

        if(!StringUtils.isEmpty(projectVO.getName())){
            sb.append(" AND D.name LIKE CONCAT('%',#{name},'%')");
        }
        if(!StringUtils.isEmpty(projectVO.getCampaignType())){
            sb.append(" AND c.type = #{campaignType}");
        }
        if(!StringUtils.isEmpty(projectVO.getCode())){
            sb.append(" AND D.code LIKE CONCAT('%',#{code},'%')");
        }
        if(!StringUtils.isEmpty(projectVO.getDegreeOfImportance())){
            sb.append(" AND D.degreeOfImportance = #{degreeOfImportance}");
        }
        if(!StringUtils.isEmpty(projectVO.getPmId())){
            sb.append(" AND D.pmId = #{pmId}");
        }

        if(!StringUtils.isEmpty(projectVO.getPotential())){
            sb.append(" AND D.potential = #{potential}");
        }

        if(!StringUtils.isEmpty(projectVO.getDevelopSaleId())){
            sb.append(" AND D.developSaleId = #{developSaleId}");
        }

        if(!StringUtils.isEmpty(projectVO.getClosingTime())){
            sb.append(" AND D.closingTime = Date(#{closingTime})");
        }

        if (!StringUtils.isEmpty(projectVO.getDesignProgress())){
            sb.append(" and find_in_set(#{designProgress},D.designProgress)");
        }

        if(!StringUtils.isEmpty(projectVO.getDesignerIdVO())){
            sb.append(" AND #{designerIdVO} in (SELECT pd.designerId FROM T_PROJECT p LEFT JOIN T_PROJECT_DESIGNER pd on p.id = pd.projectid where D.id = p.id)");
        }

        if(!StringUtils.isEmpty(projectVO.getOwnerName())){// 所有者条件
            sb.append(" AND U3.`name` LIKE CONCAT('%',#{ownerName},'%')");
        }

        if(projectVO.getIsAll() == 0){//是我的
            sb.append(" AND (");
            sb.append(" D.pmId = #{id}");
            sb.append(" or D.developSaleId = #{id}");
            sb.append(" or D.traceSaleId = #{id}");
            sb.append(" or D.ownerId = #{id}");// 别人分配给你的客户你自己能看到.
            sb.append(" OR #{id} in (SELECT pd.designerId FROM T_PROJECT p LEFT JOIN T_PROJECT_DESIGNER pd on p.id = pd.projectid where D.id = p.id)");
            sb.append(" )");
        }
        if(!StringUtils.isEmpty(projectVO.getState())){
            sb.append(" AND D.state = #{state}");
        }
        if(!StringUtils.isEmpty(projectVO.getSearch())){
            sb.append(" AND D.name LIKE CONCAT('%',#{search},'%') OR D.code LIKE CONCAT('%',#{search},'%') OR c.name LIKE CONCAT('%',#{search},'%')");
        }
        return sb.toString();
    }
}
