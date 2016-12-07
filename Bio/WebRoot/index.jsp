<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-with,initial-scale=1">
    <title>Biosearch</title>
    <link href="css/pages/bootstrap.min.css" rel="stylesheet">
     <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/jquery.js"></script>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <style>
        body{
            padding: 100px 15px;
            text-align:center;
        }
        .starter{
            padding-bottom:50px;
            /*text-align:center;*/
        }
        .btn-group{
            padding-top: 15px;
        }

        footer{
            color:#808080;
            top: 95%;
            left: 42%;
            align:center;
            position: absolute;
            }

    </style>
    <script>

  

    
    function search(){
    //get all value of search condition
    	var query = document.getElementById("querystring").value;
    	//window.location.href="searchWHG.action?query2="+query;
    	window.location.href="jumpTo.action?query2="+query;
    }
   
    
    </script>
 
</head>
<body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <a href="#" class="navbar-brand">Biosearch</a>
            </div>
           
        </div>
    </nav>

    <div class="container">
        <div class="starter">
            <h1>Welcome to Biosearch !</h1>
            <p class="lead"></p>
        </div>

        <div class="container" align="center" id="containerself">
            <div class="input-group input-group-lg col-lg-7">
          
            
                <input type="text" id="querystring" class="form-control" placeholder="Please input the query" onkeyup="instantSearch()">
              
              
               <span class="input-group-btn">
                    <button class="btn btn-info" type="button" onclick="search()">Search</button>
                    
                   
                </span>
                
                
               
               
               
            
                
               
            </div>
            <div id="insttable"  class="input-group input-group-lg col-lg-7" align="left">
            
            </div>

        </div>

        <div class="btn-group" role="group" aria-label="...">

            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    Any country
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#">Dropdown link</a></li>
                    <li><a href="#">Dropdown link</a></li>
                </ul>
            </div>

            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    Any  time
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#">Dropdown link</a></li>
                    <li><a href="#">Dropdown link</a></li>
                </ul>
            </div>

            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    All results
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#">gene expression</a></li>
                    <li><a href="#">gene therapy</a></li>
                    <li><a href="#">generationk</a></li>
                    <li class="divider"></li>
                    <li><a href="#">generation sequencing</a></li>
                </ul>
            </div>
        </div>


    </div>


</div>
    <footer>
        <div class="wrap">
            <p>Copyright&copy; <a href="http://www.biosearch.com" title="Biosearch">HIT Biosearch Lab</a> 2015 , All Rights Reserved</p>
        </div>
    </footer>
    <script src="js/jquery-2.1.1.min.js"></script>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>

</body>
</html>