package com.framework.model;

import java.util.List;

/**
 * @author zhangzhiqiang
 * @date 2019/5/6.
 * description：
 */
public class TestBean {

    /**
     * date : 2019-05-06
     * week : {"name":"周一","value":1,"description":"周一"}
     * shiftName : 广州蜘点商业日常
     * shiftType : {"name":"新增班次","value":0,"description":"新增班次"}
     * historyId : 1b1987c6f01b441fa39a6d7f3e42f878
     * teamName : null
     * isMachine : {"name":"不允许","value":0,"description":"不允许"}
     * isPhone : {"name":"允许","value":1,"description":"允许"}
     * timeList : [{"historyDetailsId":"56479b55064d42de8a57041821a1d6f9","startTime":"09:00","startRecordTime":null,"startName":{"name":"缺卡","value":1,"description":"缺卡"},"startCheckType":{"name":"0","value":0,"description":null},"startCheckPlace":null,"endTime":"18:00","endRecordTime":null,"endName":{"name":"缺卡","value":1,"description":"缺卡"},"endCheckType":{"name":"0","value":0,"description":null},"endCheckPlace":null}]
     */

    private String date;
    private WeekBean week;
    private String shiftName;
    private ShiftTypeBean shiftType;
    private String historyId;
    private Object teamName;
    private IsMachineBean isMachine;
    private IsPhoneBean isPhone;
    private List<TimeListBean> timeList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WeekBean getWeek() {
        return week;
    }

    public void setWeek(WeekBean week) {
        this.week = week;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public ShiftTypeBean getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftTypeBean shiftType) {
        this.shiftType = shiftType;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public Object getTeamName() {
        return teamName;
    }

    public void setTeamName(Object teamName) {
        this.teamName = teamName;
    }

    public IsMachineBean getIsMachine() {
        return isMachine;
    }

    public void setIsMachine(IsMachineBean isMachine) {
        this.isMachine = isMachine;
    }

    public IsPhoneBean getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(IsPhoneBean isPhone) {
        this.isPhone = isPhone;
    }

    public List<TimeListBean> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeListBean> timeList) {
        this.timeList = timeList;
    }

    public static class WeekBean {
        /**
         * name : 周一
         * value : 1
         * description : 周一
         */

        private String name;
        private int value;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class ShiftTypeBean {
        /**
         * name : 新增班次
         * value : 0
         * description : 新增班次
         */

        private String name;
        private int value;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class IsMachineBean {
        /**
         * name : 不允许
         * value : 0
         * description : 不允许
         */

        private String name;
        private int value;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class IsPhoneBean {
        /**
         * name : 允许
         * value : 1
         * description : 允许
         */

        private String name;
        private int value;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class TimeListBean {
        /**
         * historyDetailsId : 56479b55064d42de8a57041821a1d6f9
         * startTime : 09:00
         * startRecordTime : null
         * startName : {"name":"缺卡","value":1,"description":"缺卡"}
         * startCheckType : {"name":"0","value":0,"description":null}
         * startCheckPlace : null
         * endTime : 18:00
         * endRecordTime : null
         * endName : {"name":"缺卡","value":1,"description":"缺卡"}
         * endCheckType : {"name":"0","value":0,"description":null}
         * endCheckPlace : null
         */

        private String historyDetailsId;
        private String startTime;
        private Object startRecordTime;
        private StartNameBean startName;
        private StartCheckTypeBean startCheckType;
        private Object startCheckPlace;
        private String endTime;
        private Object endRecordTime;
        private EndNameBean endName;
        private EndCheckTypeBean endCheckType;
        private Object endCheckPlace;

        public String getHistoryDetailsId() {
            return historyDetailsId;
        }

        public void setHistoryDetailsId(String historyDetailsId) {
            this.historyDetailsId = historyDetailsId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Object getStartRecordTime() {
            return startRecordTime;
        }

        public void setStartRecordTime(Object startRecordTime) {
            this.startRecordTime = startRecordTime;
        }

        public StartNameBean getStartName() {
            return startName;
        }

        public void setStartName(StartNameBean startName) {
            this.startName = startName;
        }

        public StartCheckTypeBean getStartCheckType() {
            return startCheckType;
        }

        public void setStartCheckType(StartCheckTypeBean startCheckType) {
            this.startCheckType = startCheckType;
        }

        public Object getStartCheckPlace() {
            return startCheckPlace;
        }

        public void setStartCheckPlace(Object startCheckPlace) {
            this.startCheckPlace = startCheckPlace;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Object getEndRecordTime() {
            return endRecordTime;
        }

        public void setEndRecordTime(Object endRecordTime) {
            this.endRecordTime = endRecordTime;
        }

        public EndNameBean getEndName() {
            return endName;
        }

        public void setEndName(EndNameBean endName) {
            this.endName = endName;
        }

        public EndCheckTypeBean getEndCheckType() {
            return endCheckType;
        }

        public void setEndCheckType(EndCheckTypeBean endCheckType) {
            this.endCheckType = endCheckType;
        }

        public Object getEndCheckPlace() {
            return endCheckPlace;
        }

        public void setEndCheckPlace(Object endCheckPlace) {
            this.endCheckPlace = endCheckPlace;
        }

        public static class StartNameBean {
            /**
             * name : 缺卡
             * value : 1
             * description : 缺卡
             */

            private String name;
            private int value;
            private String description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class StartCheckTypeBean {
            /**
             * name : 0
             * value : 0
             * description : null
             */

            private String name;
            private int value;
            private Object description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }
        }

        public static class EndNameBean {
            /**
             * name : 缺卡
             * value : 1
             * description : 缺卡
             */

            private String name;
            private int value;
            private String description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class EndCheckTypeBean {
            /**
             * name : 0
             * value : 0
             * description : null
             */

            private String name;
            private int value;
            private Object description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }
        }
    }
}
