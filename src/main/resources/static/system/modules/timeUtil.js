var timeUtil = null;
var getTimeUtil = function () {};
getTimeUtil.prototype = {
    compareCurrentTime:function(targetTime) {
        if(targetTime==null) return true;
        var start = new Date(targetTime.replace("-", "/").replace("-", "/"));
        var end = new Date(timeUtil.getCurrentTime().replace("-", "/").replace("-", "/"));
        if(start > end) return true;
        else return false;
    },
    compareTime:function(startTime,endTime) {
        var start = new Date(startTime.replace("-", "/").replace("-", "/"));
        var end = new Date(endTime.replace("-", "/").replace("-", "/"));
        if(end < start) return true;
        else return false;
    },
    getCurrentTime :function () {
        var myDate = new Date();
        var year=myDate.getFullYear();        //获取当前年
        var month=myDate.getMonth()+1;   //获取当前月
        var date=myDate.getDate();            //获取当前日
        var h=myDate.getHours();              //获取当前小时数(0-23)
        var m=myDate.getMinutes();          //获取当前分钟数(0-59)
        var s=myDate.getSeconds();
        return year+'-'+timeUtil.getNow(month)+"-"+timeUtil.getNow(date)+" "+timeUtil.getNow(h)+':'+timeUtil.getNow(m)+":"+timeUtil.getNow(s);
    },
    //判断是否在前面加0
    getNow :function (s) {
        return s < 10 ? '0' + s: s;
    },
    CurTime: function(){
        return Date.parse(new Date())/1000;
    },
    DateToUnix: function(string) {
        var f = string.split(' ', 2);
        var d = (f[0] ? f[0] : '').split('-', 3);
        var t = (f[1] ? f[1] : '').split(':', 3);
        return (new Date(
            parseInt(d[0], 10) || null,
            (parseInt(d[1], 10) || 1) - 1,
            parseInt(d[2], 10) || null,
            parseInt(t[0], 10) || null,
            parseInt(t[1], 10) || null,
            parseInt(t[2], 10) || null
        )).getTime() / 1000;
    },
    UnixToDate: function(unixTime, isFull, timeZone) {
        if (typeof (timeZone) == 'number'){
            unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
        }
        var time = new Date(unixTime * 1000);
        var ymdhis = "";
        ymdhis += time.getUTCFullYear() + "-";
        ymdhis += (time.getUTCMonth()+1) + "-";
        ymdhis += time.getUTCDate();
        if (isFull === true){
            ymdhis += "" + time.getUTCHours() + ":";
            ymdhis += time.getUTCMinutes() + ":";
            ymdhis += time.getUTCSeconds();
        }
        return ymdhis;
    }
};
timeUtil = new getTimeUtil();