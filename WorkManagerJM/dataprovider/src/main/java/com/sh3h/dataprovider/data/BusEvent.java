package com.sh3h.dataprovider.data;


public class BusEvent {

    public static class AuthenticationError { }

    public static class StatisticsUpload {
        //上传成功多少
        private int successTextCount;
        //上传失败多少
        private int failTextCount;
        //上传成功多少
        private int successMediaCount;
        //上传失败多少
        private int failMediaCount;

        public int getSuccessTextCount() {
            return successTextCount;
        }

        public void setSuccessTextCount(int successTextCount) {
            this.successTextCount = successTextCount;
        }

        public int getFailTextCount() {
            return failTextCount;
        }

        public void setFailTextCount(int failTextCount) {
            this.failTextCount = failTextCount;
        }

        public int getSuccessMediaCount() {
            return successMediaCount;
        }

        public void setSuccessMediaCount(int successMediaCount) {
            this.successMediaCount = successMediaCount;
        }

        public int getFailMediaCount() {
            return failMediaCount;
        }

        public void setFailMediaCount(int failMediaCount) {
            this.failMediaCount = failMediaCount;
        }
    }
}
