package cn.thinkfree.database.vo;

public class IndexUserReportVO{
        /**
         * 管家
         */
        private Integer  steward;
        /**
         * 项目经理
         */
        private Integer projectManager;
        /**
         * 工长
         */
        private Integer foreman;
        /**
         * 质检
         */
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