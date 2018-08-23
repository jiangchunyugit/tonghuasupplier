package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;

@ApiModel("首页报表汇总")
public class IndexReportVO {


    private IndexProjectReportVO indexProjectReportVO;

    private IndexUserReportVO indexUserReportVO;


    public IndexReportVO(){

    }

    public IndexReportVO(IndexProjectReportVO indexProjectReportVO, IndexUserReportVO indexUserReportVO) {
        this.indexProjectReportVO = indexProjectReportVO;
        this.indexUserReportVO = indexUserReportVO;
    }

    public IndexProjectReportVO getIndexProjectReportVO() {
        return indexProjectReportVO;
    }

    public void setIndexProjectReportVO(IndexProjectReportVO indexProjectReportVO) {
        this.indexProjectReportVO = indexProjectReportVO;
    }

    public IndexUserReportVO getIndexUserReportVO() {
        return indexUserReportVO;
    }

    public void setIndexUserReportVO(IndexUserReportVO indexUserReportVO) {
        this.indexUserReportVO = indexUserReportVO;
    }
}