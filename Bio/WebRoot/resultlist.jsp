<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>


    <!-- Bootstrap Core CSS -->
    <link href="bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link href="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet">

    <!-- DataTables Responsive CSS -->
    <link href="bower_components/datatables-responsive/css/dataTables.responsive.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        body{
            margin: 0px;
            background-color: white;

        }

        #total{
            width: 100%;
            height: 100%;
            padding-left: 100px;
            padding-right: 100px;
        }

        #left{
            width: 50%;
            height: 100%;
            background-color: white;
            float:left
        }

        #right{
            width: 50%;
            height: 100%;
            background-color: white;
            float:right
        }

    </style>
 
</head>
<body >
    <!--头顶的-->
    <nav class="navbar navbar-default navbar-static-top active" role="navigation" style="margin-bottom: 0 ;background-color: #23c29c">
        <div class="navbar-header" style="margin-left: 50px;margin-right: 50px">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.html"><font color="white">Biosearch </font> </a>
        </div>
        <!-- /.navbar-header -->
    </nav>

    <!--主体-->
    <div   class="container" id="total" >

        <!--左半部分的表-->
        <div class="container" id="left" >
            123
            <div class="panel panel-default">
                <div class="panel-heading">
                    Basic Tabs
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#gene" data-toggle="tab">Gene</a>
                        </li>
                        <li><a href="#disease" data-toggle="tab">Disease</a>
                        </li>
                        <li><a href="#protein" data-toggle="tab">Protein</a>
                        </li>
                        <li><a href="#settings" data-toggle="tab">Settings</a>
                        </li>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
<%--                         <div class="tab-pane fade in active" id="gene">
                            
                        	<s:iterator value="clinset">
                        		
                        		<a href="searchWHG2.action?query2=<s:property value='genesymbol'/>" target="_blank"><h4><s:property value="genesymbol"/></h4></a>
                        		
                        		<p>
                        			<s:property value="variation"/>
                        		</p>
                        	</s:iterator>
                        </div> --%>
                        
                        <div class="tab-pane fade in active" id="gene">
                            <div class="panel-body" >
                                <div class="dataTable_wrapper" style="padding-top: 25px">
                                    <table class="table   table-hover" id="dataTables-example">
                                        <thead>
                                        <tr>
                                            <th>

                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        
                                        <s:iterator value="clinset">
                                        <tr class="odd gradeX">
                                        	<td>
												<a href="searchWHG2.action?query2=<s:property value='genesymbol'/>" target="_blank"><h2><s:property value="genesymbol"/></h2></a>
                        		
                        						<p>
                        							<s:property value="variation"/>
                        						</p>
                        					</td>
                        				
                        				</s:iterator>
                                        </tr>

                                    </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        
                        <div class="tab-pane fade" id="disease">
                        	<s:iterator value="clinset">
                        		
                        		<a href="searchWHG2.action?query2=<s:property value='genesymbol'/>" target="_blank"><h4><s:property value="disease"/></h4></a>
                        		
                        		<p>
                        			<s:property value="genesymbol"/>
                        		</p>
                        	</s:iterator>
                        </div>
                        <div class="tab-pane fade" id="protein">
                            <h4>Messages Tab</h4>
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
                        </div>
                        <div class="tab-pane fade" id="settings">
                            <h4>Settings Tab</h4>
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
                        </div>
                    </div>
                </div>
                <!-- /.panel-body -->
            </div>
        </div>

        <!--右半部分的图-->
        <div class="container" id="right" >
            <h1>这边显示实体描述</h1>
        </div>

    </div>

	<script src="bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- DataTables JavaScript -->
    <script src="bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    <script src="bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="dist/js/sb-admin-2.js"></script>

    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
        $(document).ready(function() {
            $('#dataTables-example').DataTable({
                responsive: true
            });
        });
    </script>
	
	
<%--     <!-- jQuery -->
    <script src="bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="dist/js/sb-admin-2.js"></script> --%>
</body>
</html>