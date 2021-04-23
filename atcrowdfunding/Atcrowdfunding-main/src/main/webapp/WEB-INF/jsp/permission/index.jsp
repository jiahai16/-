<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/ztree/zTreeStyle.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color: #cc0000;}
    </style>

</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 许可维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <%@include file="/WEB-INF/jsp/common/top.jsp"%>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <ul style="padding-left:0px;" class="list-group">
                    <%@include file="/WEB-INF/jsp/common/botton.jsp"%>
                </ul>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 许可树</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">

                            <ul id="treeDemo" class="ztree"></ul>

                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/script/docs.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/ztree/js/jquery.ztree.core-3.5.js"></script>
<script src="${pageContext.request.contextPath}/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/jquery/layer/layer.js"></script>

<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
        loadData();
    });
    var setting={
        view:{
            addDiyDom: function (treeId,treeNode) {
                var icoObj=$("#"+treeNode.tId+"_ico");
                if(treeNode.icon){
                    icoObj.removeClass("button ico_docu ico_open").addClass(treeNode.icon).css("background","");
                }
            },
            addHoverDom: function(treeId, treeNode){
                var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
                aObj.attr("href", "#").attr("target","_parent");
                if (treeNode.editNameFlag || $("#btnGroup"+treeNode.tId).length>0) return;
                var s = '<span id="btnGroup'+treeNode.tId+'">';
                if ( treeNode.level == 0 ) {//根节点
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="window.location.href=\'${pageContext.request.contextPath}/permission/toAdd.htm?id='+treeNode.id+'\'" style="margin-left:10px;padding-top:0px;" href="#" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                } else if ( treeNode.level == 1 ) {//分支节点
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="window.location.href=\'${pageContext.request.contextPath}/permission/toUpdate.htm?id='+treeNode.id+'\'" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                    if (treeNode.children.length == 0) {
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="deletePermission('+treeNode.id+',\''+treeNode.name+'\')" style="margin-left:10px;padding-top:0px;" href="#" >&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                    }
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="window.location.href=\'${pageContext.request.contextPath}/permission/toAdd.htm?id='+treeNode.id+'\'" style="margin-left:10px;padding-top:0px;" href="#" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                } else if ( treeNode.level == 2 ) {//叶子节点
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="window.location.href=\'${pageContext.request.contextPath}/permission/toUpdate.htm?id='+treeNode.id+'\'" style="margin-left:10px;padding-top:0px;"  href="#" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" onclick="deletePermission('+treeNode.id+',\''+treeNode.name+'\')" style="margin-left:10px;padding-top:0px;" href="#">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                }

                s += '</span>';
                aObj.after(s);
            },
            removeHoverDom: function(treeId, treeNode){
                $("#btnGroup"+treeNode.tId).remove();
            }
        }
    };
    function loadData() {
        $.ajax({
            type:"POST",
            url:"${pageContext.request.contextPath}/permission/loadData.do",
            beforeSend:function(){
                return true;
            },
            success:function(ajaxResult){
                if(ajaxResult.success){
                    var zNodes=ajaxResult.data;
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                }else {
                    alert(ajaxResult.message);
                }
            },
            error:function(){
                alert("数据加载失败!");
            }
        });
    }
    function deletePermission(id,name) {
        layer.confirm("确认要删除["+name+"]用户吗？",{icon:3,title:'提示'},function (cindex) {
            layer.close(cindex);
            $.ajax({
                type:"POST",
                data:{"id":id},
                url:"${pageContext.request.contextPath}/permission/deletePermission.do",
                beforeSend:function () {
                    return true;
                },
                success:function (ajaxResult) {
                    if(ajaxResult.success){
                        loadData();
                    }else{
                        layer.msg(ajaxResult.message,{time:1000,icon:5,shift:6});
                    }
                },
                error:function(){
                    layer.msg("删除许可信息失败！",{time:1000,icon:5,shift:6});
                }
            });
        },function (cindex) {
            layer.close(cindex);
        });
    }
</script>

</body>
</html>

