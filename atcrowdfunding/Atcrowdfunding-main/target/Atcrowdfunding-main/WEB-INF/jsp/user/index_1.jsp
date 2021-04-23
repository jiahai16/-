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
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 用户维护</a></div>
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
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="queryText" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" id="queryBtn()" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;" onclick="window.location.href='${pageContext.request.contextPath}/user/toDelete.htm'"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${pageContext.request.contextPath}/user/toAdd.htm'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${page.data}" var="user" varStatus="status">
                                    <tr>
                                        <td>${status.count}</td>
                                        <td><input type="checkbox"></td>
                                        <td>${user.loginacct}</td>
                                        <td>${user.username}</td>
                                        <td>${user.email}</td>
                                        <td>
                                            <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                            <button type="button" class="btn btn-primary btn-xs" onclick="window.location.href='${pageContext.request.contextPath}/user/toUpdate.htm?id='+'${user.id}'"><i class=" glyphicon glyphicon-pencil"></i></button>
                                            <button type="button" class="btn btn-danger btn-xs" onclick="deleteUser(${user.id},${user.loginacct})"><i class=" glyphicon glyphicon-remove"></i></button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <c:if test="${page.pageno==1}"><li class="disabled"><a href="#">上一页</a></li></c:if>
                                        <c:if test="${page.pageno!=1}"><li><a href="#" onclick="pageChange(${page.pageno-1})">上一页</a></li> </c:if>
                                        <c:forEach begin="1" end="${page.totalno}" var="num">
                                            <li <c:if test="${page.pageno==num}">class="active"</c:if>
                                            ><a href="#" onclick="pageChange(${num})">${num}</a></li>
                                        </c:forEach>
                                        <c:if test="${page.pageno==page.totalno}"><li class="disabled"><a href="#">下一页</a></li></c:if>
                                        <c:if test="${page.pageno!=page.totalno}"><li><a href="#" onclick="pageChange(${page.pageno+1})">下一页</a></li> </c:if>
                                    </ul>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/script/docs.min.js"></script>
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
    });
    function pageChange(pageno){
        window.location.href="${pageContext.request.contextPath}/user/index.do?pageno="+pageno;
    }
    function queryBtn(){
        var queryText=$("#queryText").val();
    }
    function deleteUser(id) {
            layer.confirm("确认要删除[]用户吗？",{icon:3,title:'提示'},function (cindex) {
            layer.close(cindex);
            $.ajax({
                type:"POST",
                data:{"id":id},
                url:"${pageContext.request.contextPath}/user/deleteUser.do",
                beforeSend:function () {
                    return true;
                },
                success:function (ajaxResult) {
                    if(ajaxResult.success){
                        window.location.href="${pageContext.request.contextPath}/user/index.do";
                    }else{
                        layer.msg(ajaxResult.message,{time:1000,icon:5,shift:6});
                        //alert("登陆失败!")
                    }
                },
                error:function(){
                    //alert(ajaxResult.message);
                    layer.msg("删除用户失败！",{time:1000,icon:5,shift:6});
                }
            });
        },function (cindex) {
            layer.close(cindex);
        });
    }
</script>
</body>
</html>

