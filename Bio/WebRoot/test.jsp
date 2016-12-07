<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Demo</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="$config.site_desc">
  <meta name="author" content="">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <!-- Le styles -->
  <link href="newstatic/css/bootstrap.css" rel="stylesheet">
  <link href="newstatic/css/todc-bootstrap.css" rel="stylesheet">
  <link href="newstatic/css/sticky-footer.css" rel="stylesheet">
  <link href="newstatic/css/select2.css" rel="stylesheet">
  <style type="text/css">
  body {padding-top: 46px; }
  textarea {resize: none}
  </style>
     
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->

  <script src="newstatic/js/jquery.min.js"></script>
  <script src="newstatic/js/bootstrap.min.js"></script>
  <script src="newstatic/js/select2.js"></script>
</head>
         
<body>

  <div class="navbar navbar-masthead navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Demo</a>
      </div>
      <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
          <li class="if_active('home')"><a href="$config.root">Home</a></li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </div>

<div id="wrap">
  <!-- Begin page content -->
  <div class="container">


<script src="newstatic/js/jquery.min.js"></script>
<script src="newstatic/js/bootstrap.min.js"></script>
<script src="newstatic/js/cytoscape.min.js"></script>
<script src="newstatic/js/bootstrap-slider.js"></script>
<script src="newstatic/js/cytoscape.js-panzoom.js"></script>
<script src="newstatic/js/cytoscape-cxtmenu.js"></script>
<script src="newstatic/js/d3.v3.js"></script>
<script src="newstatic/js/nv.d3.js"></script>
<script src="newstatic/js/spin.min.js"></script>
<script src="newstatic/js/ladda.min.js"></script>
<script src="newstatic/js/jquery.qtip.min.js"></script>
<script src="newstatic/js/cytoscape-qtip.js"></script>
<script src="newstatic/js/cola.v3.min.js"></script>

<link href="newstatic/css/bootstrap.css" rel="stylesheet">
<link href="newstatic/css/todc-bootstrap.css" rel="stylesheet">
<link href="newstatic/css/ladda-themeless.min.css" rel="stylesheet">
<link href="newstatic/css/bootstrap-slider.css" rel="stylesheet">
<link href="newstatic/font-awesome-4.0.3/css/font-awesome.css" rel="stylesheet">
<link href="newstatic/css/cytoscape.js-panzoom.css" rel="stylesheet">
<link href="newstatic/css/jquery.qtip.min.css" rel="stylesheet">

<script type="text/javascript">



  // load data on page init
 
  var data = {
	nodes: [
	  {data: {id: 'foo', label: 'test', foo: 'test'}},
	  {data: {id: 'bar'}},
	  {data: {id: 'bar1'}},
	  {data: {id: 'bar2'}},
	  {data: {id: 'bar3'}},
	],
	edges: [
	  {data: {id: 'foo-bar', source: 'foo', target: 'bar', weight: 0.9}},
	  {data: {id: 'foo-bar1', source: 'foo', target: 'bar1', weight: 0.9}},
	  {data: {id: 'foo-bar2', source: 'foo', target: 'bar2', weight: 0.9}},
	  {data: {id: 'foo-bar3', source: 'bar2', target: 'bar3', weight: 0.9}},
	]
  };

  var data2 = {
  	nodes: [
  		{data:{id:'1', label: 'test' , foo: 'test'}}
  	],
  	
  };
  

  var cy = cytoscape({
    container: document.getElementById('cy'),
	elements: data2,
    ready: function() {
      // $('#cy').cytoscapeNavigator();
    },
   
  });

 
</script>

<style>
.ctrl-item{
  margin-top:10px;
}
  #bar-chart svg{
    /*height: 300px*/
  }
  .bar rect {
  fill: steelblue;
  shape-rendering: crispEdges;
}

.bar text {
  fill: #000;
}

.axis path, .axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}
  .item{margin-top: 25px;}
  #cy {
    height: 800px;
    width: 100%;
    position: absolute;
    left: 0;
    top: 100px;
  }
  #f{
    position: absolute;
    top: 80px;
    /*width: 100%;*/
    height: 20px;
  }
  #annotation{
    position: absolute;
    right: 0;
  }
  .ann-item{
    z-index: 1000;
    width: 350px;
    word-wrap: normal;
    overflow-x: hidden;
    /*overflow-y: scroll;*/
    background-color: rgba(250,250,250,.85);
    padding: 20px;
    border: 1px solid #ccc;
    display: none;
  }

  .ui-cytoscape-panzoom{
    position: fixed !important;
    top: 60px;
  }

  .control-area{
    position: fixed;
    left: 0;

    z-index: 1000;
    width: 350px;
    word-wrap: normal;
    overflow-x: hidden;
    /*overflow-y: scroll;*/
    background-color: rgba(250,250,250,.8);
    padding: 20px;
    padding-bottom: 5px;
    border: 1px solid #ccc;
  }
  .ctrl-item .form-group{
    /*margin-top: 10px;*/
    margin-bottom: 5px !important;
  }
  .slider.slider-horizontal .slider-track{
    /*left: 10px !important;*/
  }
  #up-down, #down-up{
    width: 50px;
    margin: 0 auto;
  }
  .up-down-icon, .down-up-icon{
    font-size: 30px;
    color: #ccc;
  }
  .up-down-icon:hover, .down-up-icon:hover{
    color: #909090;
    cursor: pointer;
  }
  #gene{
    /*overflow-y:hidden;*/
  }
  .empty-text{
    position: absolute;
    top: 0px;
    right: 30px;
  }
  #neighborhood-list-text{
    width: 100%;
  }
  #buttons-ctrl{
    border-bottom: 1px #ccc dashed;
    padding-bottom: 5px;
    margin-top: 0;
    margin-bottom: 5px;
  }

  .bar-container{
    bottom: 0px;
    border-bottom: none !important;
  }
  #mask{
	position: absolute;
	top: -0px;
	left: -0px;
	width: 100%;
	height: 100%;
	background: #ccc;
	opacity: 0.7;
	z-index: 10000;
	display: none;
  }
</style>

  <div id="f">

  </div>

  <div id="cy"></div> 

  <div id="annotation">
    <div class="ann-item" id="ann-0">
      <button type="button" class="close-ann close pull-right"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <h3 class="ann-title"><small></small></h3>
      <p class="desc"></p>
      <table>
        <tr>
          <th>GO id</th>
          <th>GO name</th>
        </tr>
      </table>
    </div>
    <div class="ann-item" id="ann-1">
      <button type="button" class="close-ann close pull-right"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <h3 class="ann-title"><small></small></h3>
      <p class="desc"></p>
      <table>
        <tr>
          <th>GO id</th>
          <th>GO name</th>
        </tr>
      </table>
    </div>
    <div id="neighborhood-list" class="ann-item">
      <button type="button" class="close-ann close pull-right"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <h3 class="ann-title">Neighbor of selected gene</h3>
      <textarea style="border:1px #ccc solid;" name="" id="neighborhood-list-text" rows="5"></textarea>
    </div>
  </div>


  <div id="main-control" class="control-area form-horizontal">
    <div id="mask"></div>
    <div id="buttons-ctrl" class="ctrl-item">
      <a id="download-result" href="$config.root/download/result?run_id=$run_id" class="btn btn-default">Download Result File</a>
      <a id="download-png" class="btn btn-default">Download Img</a>
    </div>

    <div id="threshold-ctrl" class="ctrl-item">
      <div class="form-group">
        <label for="inputEmail3" class="col-sm-3 control-label" data-tip="Set a threshold for visualizing the semantic similarity result.">Threshold</label>
        <div class="col-sm-9">
          <input id="threshold-slider" type="text" data-slider-id='threshold-slider' type="text" data-slider-min="0.0" data-slider-max="1.0" data-slider-step="0.01" data-slider-value="1">
          <input type="text" class="form-control" id="threshold" name="threshold" value="1.0">
        </div>
      </div>
    </div>

    <div id="layout-ctrl" class="ctrl-item" style="border-bottom: 1px #ccc dashed;">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label">Layout</label>
        <div class="col-sm-9">
          <select name="layout" id="layout-select">
            <option value="concentric">concentric</option>
            <option value="breadthfirst">breadthfirst</option>
            <option value="circle">circle</option>
            <option value="grid">grid</option>
            <option value="cose">cose</option>
            <option value="cola">cola</option>
          </select>
        </div>
      </div>
    </div>
<!--
    <div id="node-label-ctrl" class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label">Name Display</label>
        <div class="col-sm-9">
          <label class="radio-inline">
            <input type="radio" name="node_label" value="on"> On
          </label>
          <label class="radio-inline">
            <input type="radio" name="node_label" value="off" checked> Off
          </label>
        </div>
      </div>
    </div>
-->
    
    <div id="gene-ctrl" class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label" data-tip="You could input a specific subset of input gene list to visualize.">Gene</label>
        <div class="col-sm-9">
          <textarea rows="3" class="form-control" id="gene" name="gene" placeholder="You could input a specific subset   of input gene list to visualize."></textarea>
          <button type="button" class="empty-text close pull-right"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        </div>
      </div>
    </div>

<!--
    <div id="edge-label-ctrl" class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label">Weight Display</label>
        <div class="col-sm-9">
          <label class="radio-inline">
            <input type="radio" name="edge_label" value="on"> On
          </label>
          <label class="radio-inline">
            <input type="radio" name="edge_label" value="off" checked> Off
          </label>
        </div>
      </div>
    </div>
-->

    <div class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label"></label>
        <div class="col-sm-9">
          <button id="reload" style="display:none"  class="btn btn-primary ladda-button" data-style="expand-right"><span class="ladda-label">Reload</span></button>
          <button id="select" class="btn btn-primary ladda-button" data-style="expand-right"><span class="ladda-label">Select</span></button>
        </div>
      </div>
    </div>

    <div class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label"></label>
        <div class="col-sm-9">
          <button id="show-neighbor" class="btn btn-default" >Toggle Neighbor Display</button>
        </div>
      </div>
    </div>

    <div class="ctrl-item">
      <div class="form-group">
        <label for="" class="col-sm-3 control-label"></label>
        <div class="col-sm-9">
          <button id="show-label" class="btn btn-default" >Toggle Name Display</button>
        </div>
      </div>
    </div>

    <div id="up-down">
      <div>
        <span id="up-icon" class="up-down-icon glyphicon glyphicon-chevron-up"></span>
      </div>
    </div>
  </div>

  <div id="" class="control-area form-horizontal bar-container" >
    
    <div id="down-up">
      <div>
        <span id="down-icon" class="down-up-icon glyphicon glyphicon-chevron-down"></span>
      </div>
    </div>
    <h4>Overall distribution of similarity scores</h4>
    <div id="bar-chart">
    </div>
    <div style="margin-top:-10px;">
      <p style="text-align:center;">Similarity Score</p>
    </div>

  </div>

      <a style="position:fixed;top:10px;right:10px;font-size:15px;z-index:100000;" href="$config.root/result?run_id=$run_id" >Back to result page</a>
</div>
</div>
</body>
</html>
