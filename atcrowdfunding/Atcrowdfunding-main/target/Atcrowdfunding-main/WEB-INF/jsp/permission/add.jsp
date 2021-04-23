<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/doc.min.css">
	<style>
		.tree li {
			list-style-type: none;
			cursor:pointer;
		}
	</style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 许可维护</a></div>
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
			<ol class="breadcrumb">
				<li><a href="#">首页</a></li>
				<li><a href="#">数据列表</a></li>
				<li class="active">新增</li>
			</ol>
			<div class="panel panel-default">
				<div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
				<div class="panel-body">
					<form id="addForm" role="form">
						<div class="form-group">
							<label for="fname">许可名称</label>
							<input type="text" class="form-control" id="fname" placeholder="请输入许可名称">
						</div>
						<div class="form-group">
							<label for="furl">许可URL</label>
							<input type="text" class="form-control" id="furl" placeholder="请输入许可URL">
						</div>
						<%--<div class="form-group">
							<label for="ficon">许可logo</label>
							<select id="ficon" placeholder="">
								<option ><div class="glyphicon glyphicon-user"/>11</option>
								<option ><div class="glyphicon glyphicon-king">11</div></option>
								<option ><div class="glyphicon glyphicon-lock">1111</div></option>
								<option ><div class="glyphicon glyphicon-check">1111</div></option>
								<option ><div class="glyphicon glyphicon-picture">111111</div></option>
								<option ><div class="glyphicon glyphicon-equalizer">1111111</div></option>
								<option ><div class="glyphicon glyphicon-random">11111</div></option>
								<option ><div class="glyphicon glyphicon-hdd"></div></option>
								<option ><div class="glyphicon glyphicon-comment"></div></option>
								<option ><div class="glyphicon glyphicon-list"></div></option>
								<option ><div class="glyphicon glyphicon-tags"></div></option>
							</select>
						</div>--%>
						<button id="addBtn" type="button" class="btn btn-success"><i class="glyphicon glyphicon-plus"></i> 新增</button>
						<button id="resetBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title" id="myModalLabel">帮助</h4>
			</div>
			<div class="modal-body">
				<div class="bs-callout bs-callout-info">
					<h4>测试标题1</h4>
					<p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
				</div>
				<div class="bs-callout bs-callout-info">
					<h4>测试标题2</h4>
					<p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
				</div>
			</div>
			<!--
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
            -->
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
	$("#addBtn").click(function () {
		var fname=$("#fname");
		var furl=$("#furl");
		var index=-1;
		$.ajax({
			type:"POST",
			cancel:false,
			data:{
				"name":fname.val(),
				"url":furl.val(),
				"pid":"${param.id}"
			},
			url:"${pageContext.request.contextPath}/permission/doAdd.do",
			beforeSend:function(){
				index=layer.msg('处理中', {icon: 16});
				return true;
			},
			success:function(ajaxResult){
				layer.close(index);
				if (ajaxResult.success){
					window.location.href="${pageContext.request.contextPath}/permission/toIndex.htm";
				}else {
					layer.msg(ajaxResult.message,{time:1000,icon:5,shift:6});
				}
			},
			error:function(){

				layer.msg("保存权限树数据失败！",{time:1000,icon:5,shift:6});
			}
		});
	})

	$("#resetBtn").click(function () {
		$("#addForm")[0].reset();
	})
</script>
</body>
</html>
