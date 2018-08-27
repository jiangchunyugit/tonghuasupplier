package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "项目状态统计")
public class IndexProjectReportVO {

        /**
         * 待上线
         */
        @ApiModelProperty("待上线")
        private Integer  notOnLine = 0;
        /**
         * 未开始
         */
        @ApiModelProperty("未开始")
        private Integer waitStart = 0;
        /**
         * 进行中
         */
        @ApiModelProperty("进行中")
        private Integer working = 0;
        /**
         * 停工
         */
        @ApiModelProperty("停工")
        private Integer  stopTheWork = 0;
        /**
         * 竣工
         */
        @ApiModelProperty("竣工")
        private Integer complete =0 ;


        public Integer getNotOnLine() {
            return notOnLine;
        }

        public void setNotOnLine(Integer notOnLine) {
            this.notOnLine = notOnLine;
        }

        public Integer getWaitStart() {
            return waitStart;
        }

        public void setWaitStart(Integer waitStart) {
            this.waitStart = waitStart;
        }

        public Integer getWorking() {
            return working;
        }

        public void setWorking(Integer working) {
            this.working = working;
        }

        public Integer getStopTheWork() {
            return stopTheWork;
        }

        public void setStopTheWork(Integer stopTheWork) {
            this.stopTheWork = stopTheWork;
        }

        public Integer getComplete() {
            return complete;
        }

        public void setComplete(Integer complete) {
            this.complete = complete;
        }

        public IndexProjectReportVO init(){
            return new IndexProjectReportVO();
        }
}
