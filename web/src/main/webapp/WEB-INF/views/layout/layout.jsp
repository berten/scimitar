<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><tiles:insertAttribute name="title"/></title>

    <!-- Bootstrap Core CSS de layout yo-->
    <link
        href="<c:url value="/resources/sbadmin2/bower_components/bootstrap/dist/css/bootstrap.min.css"/>"
        rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link
        href="<c:url value="/resources/sbadmin2/bower_components/metisMenu/dist/metisMenu.min.css"/>"
        rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="<c:url value="/resources/sbadmin2/dist/css/timeline.css"/>" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="<c:url value="/resources/sbadmin2/dist/css/sb-admin-2.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/katana.css"/>" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="<c:url value="/resources/sbadmin2/bower_components/morrisjs/morris.css"/>"
        rel="stylesheet">

    <!-- Custom Fonts -->
    <script src="https://use.fontawesome.com/35d978723c.js"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script
        src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script
        src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- jQuery -->
    <script
        src="<c:url value="/resources/sbadmin2/bower_components/jquery/dist/jquery.min.js"/>"></script>

    <!-- Bootstrap Core JavaScript -->
    <script
        src="<c:url value="/resources/sbadmin2/bower_components/bootstrap/dist/js/bootstrap.min.js"/>"></script>


</head>

<body>

<div id="wrapper">
    <nav class="navbar navbar-default navbar-static-top" role="navigation"
        style="margin-bottom: 0">
        <tiles:insertAttribute name="header"/>

        <tiles:insertAttribute name="menu"/>


    </nav>

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header"><tiles:insertAttribute name="title"/></h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>

        <tiles:insertAttribute name="body"/>
    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


<!-- Metis Menu Plugin JavaScript -->
<script
    src="<c:url value="/resources/sbadmin2/bower_components/metisMenu/dist/metisMenu.min.js"/>"></script>

<!-- Morris Charts JavaScript -->
<script
    src="<c:url value="/resources/sbadmin2/bower_components/raphael/raphael-min.js"/>"></script>
<script
    src="<c:url value="/resources/sbadmin2/bower_components/morrisjs/morris.min.js"/>"></script>
<script src="<c:url value="/resources/js/morris-data.js"/>"></script>

<!-- Custom Theme JavaScript -->
<script src="<c:url value="/resources/sbadmin2/dist/js/sb-admin-2.js"/>"> </script>

</body>

</html>
