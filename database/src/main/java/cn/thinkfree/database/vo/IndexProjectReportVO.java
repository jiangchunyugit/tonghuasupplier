package cn.thinkfree.database.vo;

public class IndexProjectReportVO {

        /**
         * 待上线
         */
        private Integer  notOnLine;
        /**
         * 未开始
         */
        private Integer waitStart;
        /**
         * 进行中
         */
        private Integer working;
        /**
         * 停工
         */
        private Integer  stopTheWork;
        /**
         * 竣工
         */
        private Integer complete ;


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
    }
