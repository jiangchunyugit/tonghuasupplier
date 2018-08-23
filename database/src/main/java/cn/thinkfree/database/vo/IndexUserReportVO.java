package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("首页用户汇总")
public class IndexUserReportVO{
        /**
         * 管家
         */
        @ApiModelProperty("管家")
        private Integer  steward;
        /**
         * 项目经理
         */
        @ApiModelProperty("项目经理")
        private Integer projectManager;
        /**
         * 工长
         */
        @ApiModelProperty("工长")
        private Integer foreman;
        /**
         * 质检
         */
        @ApiModelProperty("质检")
        private Integer qualityInspector;

        public Integer getSteward() {
            return steward;
        }

        public void setSteward(Integer steward) {
            this.steward = steward;
        }

        public Integer getProjectManager() {
            return projectManager;
        }

        public void setProjectManager(Integer projectManager) {
            this.projectManager = projectManager;
        }

        public Integer getForeman() {
            return foreman;
        }

        public void setForeman(Integer foreman) {
            this.foreman = foreman;
        }

        public Integer getQualityInspector() {
            return qualityInspector;
        }

        public void setQualityInspector(Integer qualityInspector) {
            this.qualityInspector = qualityInspector;
        }
    }