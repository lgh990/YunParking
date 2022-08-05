/*
* @Author: Administrator
* @Date:   2017-06-19 16:42:48
* @Last Modified by:   Administrator
* @Last Modified time: 2017-09-16 14:07:34
*/

// 'use strict';


var href=window.location.href;
var protocol=window.location.protocol;
var host=window.location.host;
var hostname=window.location.hostname;
//var url="192.168.1.116:8765";
var url="http://jxcadmin.iok.la";

function get_alipay_html(){
    return url+"/api/parking/alipay.html";
}

function get_wxShow_html(){
    return url+"/api/parking/wxShow.html";
}

function get_query_order_status(){
    return url+"/api/parking/pay/queryOrderStatus";
}

function get_topay_servlet(){
    return url+"/api/parking/pay/topayServlet";
}

function get_query_opnId(){
    return url+"/api/parking/pay/query_opnId";
}

function get_alipay_config(){
    return url+"/api/parking/pay/get_alipay_config";
}

function queryOrderList(){
    return url+"/api/parking/pay/queryOrderList";
}










