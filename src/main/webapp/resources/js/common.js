var GLOBALPARAMS={
		start : 0,
		length: 10
	};

$(function () { $("[data-toggle='tooltip']").tooltip(); $("[data-toggle='popover']").popover();});

/*
 * 接收url参数
 *  调用方法：
 *	alert(GetQueryString("参数名1"));
 */

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
} 
//删除数组中的某个
Array.prototype.remove=function(dx)
{
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
} 
//实现拷贝包含对象或者数组的这种情况
var deepCopy = function(o) {
    if (o instanceof Array) {
        var n = [];
        for (var i = 0; i < o.length; ++i) {
            n[i] = deepCopy(o[i]);
        }
        return n;

    } else if (o instanceof Object) {
        var n = {}
        for (var i in o) {
            n[i] = deepCopy(o[i]);
        }
        return n;
    } else {
        return o;
    }
}
//function that gathers IDs of checked nodes
function checkedNodeIds(nodes, checkedNodes) {
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].checked) {
            checkedNodes.push(nodes[i].id);
        }

        if (nodes[i].hasChildren) {
            checkedNodeIds(nodes[i].children.view(), checkedNodes);
        }
    }
}

//全局ajax设置，处理ajax请求时session超时
$.ajaxSetup({
//    contentType:"application/x-www-form-urlencoded;charset=utf-8",  
    complete:function(XMLHttpRequest,textStatus){  
        //通过XMLHttpRequest取得响应头，sessionstatus，  
        var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); 
        if(sessionstatus=="timeout"){  
        //如果超时就处理 ，指定要跳转的页面
//        	if(confirm("由于您长时间没有操作，session已过期，请重新登录。")){
        		window.location.reload();
//        	}
        }  
    }  
}); 