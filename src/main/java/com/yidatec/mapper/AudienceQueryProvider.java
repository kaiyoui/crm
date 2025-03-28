package com.yidatec.mapper;

import com.yidatec.vo.AudienceVO;
import com.yidatec.vo.UserVO;
import org.springframework.util.StringUtils;

/**
 * Created by jrw on 17-7-18.
 */
public class AudienceQueryProvider {

    public String selectAudienceList(final AudienceVO audienceVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT U.*, GROUP_CONCAT(DISTINCT(C.name)) campaignName  FROM T_AUDIENCE U LEFT JOIN T_AUDIENCE_CAMPAIGN B ON U.id = B.audienceId " +
                " left join T_CAMPAIGN C on B.campaignId = C.id  WHERE 1=1 ");

        if (!StringUtils.isEmpty(audienceVO.getName())){
            sb.append(" AND U.name LIKE CONCAT('%',#{name},'%')");
        }
        sb.append(" GROUP BY U.id");
        sb.append(" ORDER BY U.modifyTime DESC");
        sb.append(" LIMIT #{start},#{length}");
        return sb.toString();
    }
    public String countSelectAudienceList(final AudienceVO audienceVO)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT count(*) FROM ( ");
        sb.append("SELECT count(*) FROM T_AUDIENCE U LEFT JOIN T_AUDIENCE_CAMPAIGN B ON U.id = B.audienceId " +
                " left join T_CAMPAIGN C on B.campaignId = C.id  WHERE 1=1 ");

        if (!StringUtils.isEmpty(audienceVO.getName())){
            sb.append(" AND U.name LIKE CONCAT('%',#{name},'%')");
        }
        sb.append(" GROUP BY U.id");
        sb.append(" ) a");
        return sb.toString();
    }

}
