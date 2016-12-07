<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
body {
	padding-top: 46px;
}

textarea {
	resize: none
}

.mybtn {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: normal;
	line-height: 1.42857143;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #fff;;
	background-color: #449d44;
}

.page {
	margin: 2em;
}

.page a {
	text-decoration: none;
	display: inline-block;
	line-height: 14px;
	padding: 2px 5px;
	color: #1e29dc;
	border: 1px solid #ccc;
	margin: 0 2px;
}

.page a:hover,.page a.on {
	background: #1a26ff;
	color: #fff;
	border: 1px solid #333;
}

.page a.unclick,.page a.unclick:hover {
	background: none;
	border: 1px solid #eee;
	color: #1e29dc;
	cursor: default;
}
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

	<div class="navbar navbar-masthead navbar-default navbar-fixed-top"
		role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Demo</a>
			</div>
			
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="if_active('home')"><a href="$config.root">Home</a>
					</li>
				</ul>
				<form class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control col-lg-7" placeholder="Search">
                    </div>

                    <button type="submit" class="btn btn-info">Search<tton>
                </form>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#" id="showa">Show</a>
					</li>
				</ul>
			</div>
			<!--/.nav-collapse -->


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

			<!-- <script src="newstatic/js/jquery.qtip.min.js"></script> 
<link href="newstatic/css/jquery.qtip.min.css" rel="stylesheet"> -->

			<script src="newstatic/js/jquery.qtip.js"></script>
			<link href="newstatic/css/jquery.qtip.css" rel="stylesheet">
			<!-- <link href="newstatic/css/jquery.qtip.min.css" rel="stylesheet">  -->
			<!-- <link rel="stylesheet" type="text/css" href="http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.css">
 -->

			<!-- <script src="http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.js">
<link rel="stylesheet" type="text/css" href="http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.css"> -->



			<script src="newstatic/js/cytoscape-qtip.js"></script>
			<script src="newstatic/js/cola.v3.min.js"></script>

			<link href="newstatic/css/bootstrap.css" rel="stylesheet">
			<link href="newstatic/css/todc-bootstrap.css" rel="stylesheet">
			<link href="newstatic/css/ladda-themeless.min.css" rel="stylesheet">
			<link href="newstatic/css/bootstrap-slider.css" rel="stylesheet">
			<link href="newstatic/font-awesome-4.0.3/css/font-awesome.css"
				rel="stylesheet">
			<link href="newstatic/css/cytoscape.js-panzoom.css" rel="stylesheet">

			<link rel="stylesheet" href="newstatic/css/bootstrap-theme.min.css">

			<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
 -->
			<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/fastclick/1.0.6/fastclick.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/3.10.0/lodash.min.js"></script> -->
			<script src="newstatic/js/fastclick.min.js"></script>
			<script src="newstatic/js/lodash.min.js"></script>
			<link href="whgpack/style.css" rel="stylesheet" />
			<!-- <script src="whgpack/code.js"></script> -->

			<script type="text/javascript">
 
 var display_flag = true;
 var display_neighbor_flag = true;
 
 $("#showa").hide(100);


 $(function(){
  'use strict';
  Ladda.bind( '#reload' );
  var spinner = new Spinner();

  function show_mask(){
    var target = document.getElementById('cy');
	$("#mask").show();
    spinner.spin(target);
  }

  function hide_mask(){
	$("#mask").hide();
    spinner.stop();
  }

  $(document).ajaxStart(function(){
	show_mask();
  });

  $(document).ajaxStop(function(){
	hide_mask();
  });

  $('[data-tip!=""]').qtip({
    content:{
      attr: 'data-tip'
    },
    hide: {
      fixed: true,
      delay: 300
    },
    position:{
      target: 'mouse',
      adjust:{
        mouse: false
      }
    },
    style: {
    }
  });

  var layout_name = 'cola';

  var turn = 0;
  var last_gene = "";

  var locked_gene = Array();
  var selected_gene = Array();


  // load data on page init
 


  var data2 = {
  	nodes: [
  		{data:{id:'1', label: 'test' , foo: 'test'}}
  	],
  	
  };
  

  var cy = cytoscape({
    container: document.getElementById('cy'),
	//elements: data,
    ready: function() {
      // $('#cy').cytoscapeNavigator();
    },
    style: cytoscape.stylesheet()
    .selector('node')
      .css({
        'content': 'data(title)',
        'text-valign': 'center',
        //'text-halign': 'right',
        'color': 'black',
        //'text-outline-width': 1,
        //'text-outline-color': '#888',
        'width': 25,
        'height': 25,
        //'min-zoomed-font-size': 1,
        //'background-color': '#fff',
        //'font-size': 1
      })
      
     
      
 /*     .selector('label')
       .css({
         'font-size': 40
       }) */
       
    .selector('edge')
      .css({
        //'target-arrow-shape': 'triangle-backcurve',

        //'line-color' : 'mapData(weight, 0.1, 0.95, green, red)',
        'line-color' : '#99CCFF',
        'opacity': 0.70,
       // 'width' : 'mapData(weight, 0.1, 1.0, 1, 5)',
       //'content': 'data(id)'
      })
    .selector(':selected')
      .css({
        'background-color': 'blue',
        'line-color': 'blue',
        'target-arrow-color': 'black',
        'source-arrow-color': 'black'
      })
    .selector('.faded')
      .css({
        'opacity': 0.25,
        'text-opacity': 0
      }),
    layout : {name: layout_name},
    minZoom: 0.4,
    maxZoom: 2,
    wheelSensitivity: 0.1,
  });




  cy.panzoom();

  cy.cxtmenu({
    commands: [
      {
        content: '<span class="fa fa-plus fa-2x"></span>',
        select: function(){
          console.log('plus');
          
          add_to_selected_list(this.data("id"));

          var pre_content = $.trim($("#gene").val());
          var genes = pre_content.split('\n');
          if(genes.indexOf(this.data('id')) >= 0){
            return;
          }
          if(genes.length >= 3 && genes.length <= 5){
            $("#gene").attr('rows', genes.length + 1);
          }
          if(pre_content.length < 2){
            pre_content = "";
          }else{
            pre_content += '\n';
          }
          $("#gene").val(pre_content+this.data('id'));
          console.log(pre_content);
          console.log($.trim(pre_content));
        }
      },
      {
        content: '<span class="fa fa-flag fa-2x"></span>',
        select: function(){
          var selector = "node[id='"+this.data('id')+"']";
          var s = cy.$(selector)[0];
          if(s.css('background-color') == 'green')
            s.css('background-color', '#777');
          else
            s.css('background-color', 'green');
          console.log('flag');
        }
      },
      {
        content: '<span class="fa fa-lock fa-2x"></span>',
        select: function(){
          console.log('lock');
          var g = this.data('id');
          for (var i = 0; i < locked_gene.length; i++) {
            if(locked_gene[i] == g){
              console.log('has item in locked_gene');
              return;
            }
          };
          locked_gene.push(g);
          cy.$("node[id='"+g+"']")[0].css('background-color', 'red');
          on_lock_unlock();
          
          console.log(locked_gene);
        }
      },
      {
        content: '<span class="fa fa-unlock fa-2x"></span>',
        select: function(){
          console.log('unlock');
          var g = this.data('id');
          for (var i = 0; i < locked_gene.length; i++) {
            if(locked_gene[i] == g){
              locked_gene.splice(i, 1);
              cy.$("node[id='"+g+"']")[0].css('background-color', '#777');

              on_lock_unlock();

              break;
            }
          };
          if(locked_gene.length == 0){
            cy.$("node").css({'opacity': 1});
            cy.$("edge").css({'opacity': 0.95});
          }
          console.log(locked_gene);
        }
      },
      {
        content: '<span class="fa fa-info fa-2x"></span>',
        select: function(){
          console.log('info');
          get_gene_info(this.data('id'));
        }
      }
    ]
  });

  function on_lock_unlock(){
    cy.$("node").css({'opacity': 0.50});
    cy.$("edge").css({'opacity': 0.05});

    for (var i = 0; i < locked_gene.length; i++) {
      highlight_neighbor(locked_gene[i]);
    };

    for (var i = 0; i < selected_gene.length; i++) {
      highlight_neighbor(selected_gene[i]);
    };
  }

  $("#select").click(function(){
    var genes = $("#gene").val().split('\n');
    console.log(genes);
    for (var i = 0; i < selected_gene.length; i++) {
      cy.$("node[id='"+selected_gene[i]+"']")[0].css('background-color', '#777');
    };

    selected_gene = [];

    for (var k = 0; k < genes.length; k++) {
      var g = genes[k];
      if(g.length == 0) continue;
      console.log(g);
      selected_gene.push(g);
      cy.$("node[id='"+g+"']")[0].css('background-color', 'yellow');
    };

    console.log(selected_gene);
    on_lock_unlock();
  });

 
   cy.on("zoom", function(e){
    // return;
    var zoom_level = cy.zoom();
    console.log(zoom_level);
    // zoom_level = 1 + 0.01 * zoom_level;
    var font_size = 15 / zoom_level;
    var node_width = 25 / zoom_level;
    var node_height = 25 / zoom_level;
    //console.log(node_width);

    var edge_min_width = 0.2 / zoom_level;
    var edge_max_width = 1.5 / zoom_level;
    
    cy.style()
    .selector("node").css({
      'font-size': font_size,
      'width': node_width,
      'height': node_height
    })
    .selector("edge").css({
      //'width': 'mapData(weight, 0.5, 1.0, '+ edge_min_width +','+edge_max_width+')'
      'width': edge_max_width
    }).update();
    // cy.forceRender(); 
  });  
 

  cy.on("click", function(e){
    console.log(e);
    if(e.cyTarget == cy){
      console.log('cy');
      cy.$("node").css({'opacity': 1});
      cy.$("edge").css({'opacity': 0.95});
      return false;
    }else if(e.cyTarget.isEdge()){
    	return false;
    	//showedgebox(e.cyTarget.id());
    }else if(!e.cyTarget.isNode()){
    	return false;
    }

    cy.$("node").css({'opacity': 0.50});
    cy.$("edge").css({'opacity': 0.05});

    var g = e.cyTarget.id();
    highlight_neighbor(g);
    fill_neighbor_list(g);

    for (var i = 0; i < locked_gene.length; i++) {
      highlight_neighbor(locked_gene[i]);
    };

    //get_gene_info(g);
  });

	function showedgebox(id){
		var edge = cy.$("edge[id='"+id+"']")[0];
		var info = "";
		info += "description: " + edge.data('description') + '\n' + "evidence: " + edge.data('evidence');
		$("#edge-info").val(info).parent().show();
	}

  function highlight_neighbor(g){
    if(display_neighbor_flag){
      var neighbors = cy.$("node[id='"+g+"']")[0].closedNeighborhood();
    }else{
      var neighbors = cy.$("node[id='"+g+"']");
    }
    neighbors.css({'opacity':1});
    neighbors.addClass('hightlight');
    for (var i = 0; i< selected_gene.length; i++) {
      cy.$("edge[source='"+g+"'][target='"+selected_gene[i]+"']").css({'opacity':1});
    }
  }

  function fill_neighbor_list(g){
    var neighbors = cy.$("node[id='"+g+"']")[0].neighborhood();
    //console.log(neighbors);
    var list = "";
    var info = "";
    var node = cy.$("node[id='"+g+"']")[0];
    info += "link: " +node.data('link') + '\n' + "abstract: " + node.data('abstracts') + '\n'
         +"database: " + node.data('database');
    
    
    
    
    for (var i = 0; i < neighbors.length; i++) {
      if(neighbors[i].isNode()){
        list += neighbors[i].data('id') + '\n';
      }
    };
    
    $("#neighborhood-list-text").val(list);
    
    $("#nodeinfo-list-text").val(info).parent().show();
  }


  $("#neighborhood-list-text").hover(function(){
    $(this).select();
  });
  
  var globaljson;
  var query = $("#queryString").val();
  $.getJSON("getData222.action?query="+query, 
    function(data){
     // console.log(data);
      
      cy.load(data.map);
      //cy.load(data.map);

      globaljson = data;
      refresh_style(data.map.types,data);
	 
    });


	function refresh_style(types,data){
		//循环外层用type的个数来控制
		//alert(cy.nodes()[0].data("id"));
		var color = ['#FF99CC','#FFFF99','#CC99CC','#666633','#CC9999','#FF6666','#CCCC00','#0066CC','#CCCC99','#FFCC99'];
		for(var i=0;i<types.nodetype.length;i++){
		//	cy.style().selector("node[type = '"+types.nodetype[i].type+"']").css('background-color',color[i]);
			if (types.nodetype[i].type = 'queried')
				cy.style().selector("node[type = '"+types.nodetype[i].type+"']").css('background-color',color[0]);
			else
				cy.style().selector("node[type = '"+types.nodetype[i].type+"']").css('background-color',color[2]);
			
		} 
		
		
	
		
		
		checkbox_node(color,data.map.types.nodetype);
	    checkbox_edge(color,data.map.types.edgetype);
	     $("#choosenode").hide();
		 $("#chooseedge").hide();
		//添加length个颜色的按钮
		//	
		
		//cy.style().selector('node[]').css('line-color', 'mapData(weight, '+e.value+', 0.9, blue, red').update();
    //console.log('mapData(weight, '+e.value+', 0.95, blue, red)');
	
	}

	function makeedgebox(id){
		var edge = cy.$("edge[id='"+id+"']");
		var info = "";
		info += "<h4>type: " + edge.data('type')  + "</h4><h4>description: " + edge.data('description') + '</h4>' + "<h4>evidence: <a href = '"+ edge.data('evidence')+"'>"+edge.data('evidence')+"</a></h4>";
		return info;
	}

  function refresh_tip(){
    cy.nodes().qtip({
      content: function(){return this.data('abstracts');},
      show:{event: 'mouseover'},
      hide:{event: 'mouseout'},
      style: {
        classes: 'qtip-bootstrap',
        tip: {width: 16, height: 8 }
      },
      position: {
        target: 'mouse',
        viewport: $(window),
        at: 'top center',
        my: 'bottom center',
        adjust: {x: 10.1,y: 10.1}
        
      }
    });
    cy.edges().qtip({
      content: function(){
      		var id = this.data('id');
      		var edge = makeedgebox(id);
      		return edge;
      	},
      show:{event: 'mouseover'},
      hide:{event: 'mouseout'},
      style: {
        classes: 'qtip-bootstrap',   //qtip-tipsy'
        tip: {width: 16, height: 8 }
      }
    });
  }

  $("#threshold-slider").slider({});

  $("#threshold-slider").on('slide', function(e){
    $("#threshold").val(e.value);
    cy.style().selector('edge').css('line-color', 'mapData(weight, '+e.value+', 0.9, blue, red').update();
    console.log('mapData(weight, '+e.value+', 0.95, blue, red)');
    // $("#reload").trigger('click');
  });
  
  $("#threshold").blur(function(){
    $("#reload").trigger('click');
  });
  $("#threshold-slider").mouseup(function(){
    $("#reload").trigger('click');
  });

  $("#threshold").bind('keypress', function(event){
    if(event.keyCode == "13"){
	  $("#reload").trigger('click');
	}
  });

  $("select[name=layout]").change(function(){
    layout_name = $(this).val();
    cy.layout({name: layout_name});
  });

  $("#threshold-slider").change(function(){
    alert($(this).val());
  });

  $("input[name=edge_label]").change(function(){
    var content = "";
    if($(this).val() == 'on'){
      content = "data(weight)";
    }else{
      content = "";
    }
    cy.style().selector('edge').css({
      "content" : content
    }).update();
  });

  $("#show-label").click(function(){
    if(display_flag){
      cy.nodes().trigger('mouseover');
      display_flag = false;
    }else{
      cy.nodes().trigger('data');
      display_flag = true;
    }
  });

  $("#show-neighbor").click(function(){
    if(display_neighbor_flag){
      display_neighbor_flag = false;
      on_lock_unlock();
    }else{
      display_neighbor_flag = true;
      on_lock_unlock();
    }
  });


  $(".close-ann").click(function(){
    $(this).parent().hide();
  });

  $(".empty-text").click(function(){
    $("#gene").val("").attr('rows', 3);
  });

  var up_down_show_status = true;
  var down_up_show_status = true;

  $("#up-down").click(function(){
    if(up_down_show_status){
      $("#main-control").animate({top:'-270px'}, 'fast', function(){
        $(".up-down-icon").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
        $(".ui-cytoscape-panzoom").css('left', '0');
      });
      up_down_show_status = false;
    }else{
      $(".ui-cytoscape-panzoom").css('left', '350px');
      $("#main-control").animate({top:'45px'}, 'fast', function(){
        $(".up-down-icon").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
      });
      up_down_show_status = true;
    }
  });

  $("#down-up").click(function(){
    if(down_up_show_status){
      $(".bar-container").animate({bottom:'-260px'}, 'fast', function(){
        $(".down-up-icon").removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-up");
      });
      down_up_show_status = false;
    }else{
      $(".bar-container").animate({bottom:'0px'}, 'fast', function(){
        $(".down-up-icon").removeClass("glyphicon-chevron-up").addClass("glyphicon-chevron-down");
      });
      down_up_show_status = true;
    }
  });

  // $("select").select2();
  var select = $("#layout-select");
  // select.select2();
/*
  $.getJSON('$config.root/result/sjson', 
    {run_id:'$run_id'}, 
    function(data){
      // var values = get_values(data);
      var values = data.edges;
      plot_histogram(values);
    });
*/
  function get_values(data){
    var vs = Array();
    for (var i = 0; i <= data.edges.length - 1; i++) {
      // if(data.edges[i].data.weight==0) continue;
      vs[i] = data.edges[i].data.weight;
    };
    console.log(vs);
    return vs;
  }


	
$(".checkbox-node").click(function(){
	
	if($(this).is(':checked')){
		
		cy.$("node[type = '"+$(this).attr("value")+"']").css({'visibility': "visible"});
		
	}else{
		cy.$("node[type = '"+$(this).attr("value")+"']").css({'visibility': "hidden"});
		
	}
});


$(".checkbox-edge").click(function(){
	if($(this).is(':checked')){
		
		cy.$("edge[type = '"+$(this).attr("value")+"']").css({'visibility': "visible"});
		
	}else{
		cy.$("edge[type = '"+$(this).attr("value")+"']").css({'visibility': "hidden"});
		
	}
});


$(".num-select").change(function(){

  var no = 0;
  var temp = {nodes: [], edges: []};
  //alert($(this).children('option:selected').val());
  for(var i = 0;i<globaljson.map.nodes.length ;i++)
  {
  	var rank = globaljson.map.nodes[i].data.noderank;
  	var selectvalue	 =	$(this).children('option:selected').val();
    if(rank <= selectvalue && rank > 0 && no< selectvalue)
    {
      
      temp.nodes.push(globaljson.map.nodes[i]);
      no++;
    }
  }

  no=0;
  var flagsource=0;
  var flagtarget=0;
  for(var i = 0;i<globaljson.map.edges.length ;i++)
  {
  	  flagsource=0;
  	  flagtarget=0;
	  for(var j = 0;j<temp.nodes.length ;j++)
	  {
	  	if(globaljson.map.edges[i].data.source ==temp.nodes[j].data.id )
	  	{
	  		flagsource=1;
	  	}
	  	
	  	if(globaljson.map.edges[i].data.target ==temp.nodes[j].data.id )
	  	{
	  		flagtarget=1;
	  	}
	  }
	  
	  if(flagsource == 1 && flagtarget == 1)
	  {
	  	temp.edges.push(globaljson.map.edges[i]);
	  }
  }
  $(".checkbox-node").prop('checked',true);
  $(".checkbox-edge").prop('checked',true);
	
  //alert(temp.nodes.length);
  //alert(temp.edges.length);
  cy.load(temp);
  cy.layout({name: "circle"});
  fill_node_list("all",temp.nodes);
  
  fill_list_all_item(globaljson.map.types.nodetype,temp.nodes);
  refresh_tip();
  //setPage(document.getElementById("pageall"),temp.nodes.length,1);
  
});


function fill_list_all_item(types,data){
	for(var i =0 ; i< types.length ; i++){
		var myArray=new Array();
		for(var j=0; j<data.length;j++){
			if(types[i].type==data[j].data.type){
				myArray.push(data[j]);
			}
		}
		fill_node_list(types[i].type,myArray);
	}
}


function tabtital(type){
	var object = $("#navtab");
	var objectcon = $("#contenttab");
	
	//tab content
	for (var i = 0; i < type.length ; i++)
	{
		var con = "<div role='tabpanel' class='tab-pane' id='"
				+type[i].type
				+"'>"
				+" <ol class='"
				+type[i].type
				+"-node-list'> </ol>"
				+"<div class='page' id='page"
				+type[i].type
				+"'></div>"
				+"</div>";
				objectcon.append(con); 
	}
	
	
	//tab
	for (var i = 0; i < type.length ; i++)
	{
		var li = "<li role='presentation'><a role='tab' data-toggle='tab' href='#"
		     	+type[i].type
		     	+"'>"
		     	+type[i].type
		     	+"</a></li>";
		
		object.append(li);  
		  	
	}
	
		$("#navtab li:eq(0)").show();
		$("#navtab li:eq(1)").show();
		$("#navtab li:eq(2)").show();
		$("#navtab li:eq(3)").show();
		$("#navtab li:eq(4)").hide();
		$("#navtab li:eq(5)").hide();
		$("#navtab li:eq(6)").hide();
		$("#navtab li:eq(7)").hide();
	
	$("#navtab li").click(function(e){
		console.log("hi");
		e.preventDefault();
		$(this).tab("show");
/* 		var a = $(this).text();
		
		var b = "#"+a
		$(b).tab("show"); */
	});
	
	
	$("#navtab li:eq(2) a").click(function(){
		
		$("#navtab li:eq(0)").show(50);
		$("#navtab li:eq(1)").show(50);
		$("#navtab li:eq(2)").show(50);
		$("#navtab li:eq(3)").show(50);
		
		$("#navtab li:eq(4)").hide(50);
		$("#navtab li:eq(5)").hide(50);
		$("#navtab li:eq(6)").hide(50);
		$("#navtab li:eq(7)").hide(50);		
	}); 
	
	$("#navtab li:eq(3) a").click(function(){
		
		$("#navtab li:eq(0)").hide(50);
		$("#navtab li:eq(1)").hide(50);
		$("#navtab li:eq(2)").show(50);
		$("#navtab li:eq(3)").show(50);
		$("#navtab li:eq(4)").show(50);
		$("#navtab li:eq(5)").show(50);
		$("#navtab li:eq(6)").hide(50);
		$("#navtab li:eq(7)").hide(50);
	}); 
	
	$("#navtab li:eq(4) a").click(function(){
		
		$("#navtab li:eq(0)").hide(50);
		$("#navtab li:eq(1)").hide(50);
		$("#navtab li:eq(2)").show(50);
		$("#navtab li:eq(3)").show(50);
		$("#navtab li:eq(4)").show(50);
		$("#navtab li:eq(5)").show(50);
		$("#navtab li:eq(6)").hide(50);
		$("#navtab li:eq(7)").hide(50);
	}); 
	
	$("#navtab li:eq(5) a").click(function(){
		
		$("#navtab li:eq(0)").hide(50);
		$("#navtab li:eq(1)").hide(50);
		$("#navtab li:eq(2)").hide(50);
		$("#navtab li:eq(3)").hide(50);
		$("#navtab li:eq(4)").show(50);
		$("#navtab li:eq(5)").show(50);
		$("#navtab li:eq(6)").show(50);
		$("#navtab li:eq(7)").show(50);
	}); 
	
}





function checkbox_node(type, data){
    var ol = $("#choosenode");
    console.log(ol);
    for (var i = 0; i <= data.length - 1; i++) {
      var item = data[i];
      var li = "<button  class='mybtn' style='background-color: " 
      		  +type[i]
      		  +"'>"
              + "<input type='checkbox' value=" + item.type
              + " class='checkbox-node'"
              + "checked>"
	          +  item.type		  
              + "</button>";
      // console.log(li);
      ol.append(li);
    }
    
    ol.on('change', '.checkbox-node', function(){
      console.log($(this).is(":checked"));
      if($(this).is(":checked")){
      	cy.$("node[type = '"+$(this).attr("value")+"']").css({'visibility': "visible"});
      }else{
		cy.$("node[type = '"+$(this).attr("value")+"']").css({'visibility': "hidden"});
      }
      return false;
    });
}


function checkbox_edge(type, data){
    var ol = $("#chooseedge");
    console.log(ol);
    for (var i = 0; i <= data.length - 1; i++) {
      var item = data[i];
      var li = "<button class='mybtn' style='background-color:"
      		  + type[i]
      		  +"'>"
              + "<input type='checkbox' value=" + item.type
              + " class='checkbox-edge'"
              + "checked>"
			  +  item.type
              + "</button>";
      // console.log(li);
      ol.append(li);
    }
    
    ol.on('change', '.checkbox-edge', function(){
      console.log($(this).is(":checked"));
      if($(this).is(":checked")){
      	cy.$("edge[type = '"+$(this).attr("value")+"']").css({'visibility': "visible"});
      }else{
		cy.$("edge[type = '"+$(this).attr("value")+"']").css({'visibility': "hidden"});
      }
      return false;
    });
}


    //container 容器，totalterm总条数, pageindex 当前页数
    function setPage(containerid, totalterm, pageindex) {
        var container = document.getElementById("page"+containerid);
        var count = Math.floor((totalterm-1)/10)+1;
        var pageindex = pageindex;
        var a = [];
        //总页数少于10 全部显示,大于10 显示前3 后3 中间3 其余....
        if (pageindex == 1) {
            a[a.length] = "<a href=\"#\" class=\"prev unclick\">Prev</a>";
        } else {
            a[a.length] = "<a href=\"#\" class=\"prev\">Prev</a>";
        }
        function setPageList() {
            if (pageindex == i) {
                a[a.length] = "<a href=\"#\" class=\"on\">" + i + "</a>";
            } else {
                a[a.length] = "<a href=\"#\">" + i + "</a>";
            }
        }
        //总页数小于10
        if (count <= 10) {
            for (var i = 1; i <= count; i++) {
                setPageList();
            }
        }
        //总页数大于10页
        else {
            if (pageindex <= 4) {
                for (var i = 1; i <= 5; i++) {
                    setPageList();
                }
                a[a.length] = "...<a href=\"#\">" + count + "</a>";
            } else if (pageindex >= count - 3) {
                a[a.length] = "<a href=\"#\">1</a>...";
                for (var i = count - 4; i <= count; i++) {
                    setPageList();
                }
            }
            else { //当前页在中间部分
                a[a.length] = "<a href=\"#\">1</a>...";
                for (var i = pageindex - 2; i <= pageindex + 2; i++) {
                    setPageList();
                }
                a[a.length] = "...<a href=\"#\">" + count + "</a>";
            }
        }
        if (pageindex == count) {
            a[a.length] = "<a href=\"#\" class=\"next unclick\">Next</a>";
        } else {
            a[a.length] = "<a href=\"#\" class=\"next\">Next</a>";
        }
        container.innerHTML = a.join("");
        //事件点击
        var pageClick = function() {
            var oAlink = container.getElementsByTagName("a");
            var inx = pageindex; //初始的页码
            oAlink[0].onclick = function() { //点击上一页
                if (inx == 1) {
                    return false;
                }
                inx--;
                setPage(containerid, totalterm, inx);
                showhide(containerid,inx,totalterm);
                return false;
            }
            for (var i = 1; i < oAlink.length - 1; i++) { //点击页码
                oAlink[i].onclick = function() {
                    inx = parseInt(this.innerHTML);
                    setPage(containerid, totalterm, inx);

                    showhide(containerid,inx,totalterm);

                    return false;

                }
            }
            oAlink[oAlink.length - 1].onclick = function() { //点击下一页
                if (inx == count) {
                    return false;
                }
                
                inx++;
                setPage(containerid, totalterm, inx);
                showhide(containerid,inx,totalterm);
                return false;
            }
        } ()
        showhide(containerid,1,50);
    }

    

    function showhide(containerid,page,totalnum){
        for(var i=0; i<totalnum ; i++){
            var item = "#"+containerid+" li:eq("+i+")";
            //var item = containerid+" .all-node-list-item:eq("+i+")";
            $(item).hide();
            //hideitem(item);
        }
        for(var j=(page-1)*10 ; j<page*10 && j<totalnum ; j++){
            var item = "#"+containerid+" li:eq("+j+")";
            //showitem(item);
            $(item).show();
        }
    }//showhide


$("#showa").click(function(){
	$("#annotation").show(100);
	$("#showa").hide(100);

});

$("#btnhidea").click(function(){
	$("#annotation").hide(100);
	$("#showa").show(100);
});


$("#choicenode").click(function(){
	if($(this).attr("value")==1){
		$("#choosenode").hide(100);
		$(this).attr("value",0);
		$(this).html("<big>Nodes choices</big>&nbsp;&nbsp;&nbsp;show>>");
	}
	else{
		$("#choosenode").show(100);
		$(this).attr("value",1);
		$(this).html("<big>Nodes choices</big>&nbsp;&nbsp;&nbsp;hide<<");
	}
	
});

$("#choiceedge").click(function(){
	if($(this).attr("value")==1){
		$("#chooseedge").hide(100);
		$(this).attr("value",0);
		$(this).html("<big>Edges choices</big>&nbsp;&nbsp;&nbsp;show>>");
	}
	else{
		$("#chooseedge").show(100);
		$(this).attr("value",1);
		$(this).html("<big>Edges choices</big>&nbsp;&nbsp;&nbsp;hide<<");
	}
	
});










  $('.GO-term-amigo').qtip({
    content: function(){return 'term details for "'+$(this).html()+'" in Amigo website';}
  });

  /*****************************************/

  var d = [
    {name: 'foo', description: 'This is a short description of ATR1'},
    {name: 'bar', description: 'This is a short description of ATR2'},
    {name: 'bar1', description: 'This is a short description of ATR3'},
    // {name: 'ATR4', description: 'This is a short description of ATR4'},
  ];
  var d2 = [
    {name: 'foo', description: '22This is a short description of ATR1'},
    {name: 'bar', description: 'This is a short description of ATR2'},
    {name: '33bar1', description: 'This is a short description of ATR3'},
    // {name: 'ATR4', description: 'This is a short description of ATR4'},
  ];

  function findAllPath(from, to){
  	
  }
  $("#refine").click(function(){
    //trigger refine_network event after click the button
    //get selected_node_list first
    
    var test = selected_node_list;
    var from = cy.$("node[id='"+test[0]+"']")[0];
    var to = cy.$("node[id='"+test[1]+"']")[0];
    //找到所有的路径
    var temp = {nodes: [], edges: []}; 
    var net = globaljson.map;
    
    var allpath = {path:[], weight:[]};
    
    var dfs = cy.elements().dfs(from, function(i,depth){
    	
    });
    
   
  });
  
  var selected_node_list = Array();

  function highlight_selected_item(item){
    cy.$("node[id='"+item+"']").css({"color":"red"});
  }

  function un_highlight_selected_item(item){
    //TODO: restroe node's color
    cy.$("node[id='"+item+"']").css({"color":"#777"});
  }

  function add_to_selected_list(item){
    // checked checkbox
    $(".node-item-"+item).each(function(){
      if(!$(this).is(":checked")){
        $(this).prop("checked", true);
        $(this).parent().show();
      }
    });
    highlight_selected_item(item);

    // update selected node list array
    for (var i = selected_node_list.length - 1; i >= 0; i--) {
      if(selected_node_list[i] == item){
        return;
      }
    }
    selected_node_list.push(item);
  }

  function remove_from_selected_list(item){
    // un checked checkbox
    $(".node-item-"+item).each(function(){
      if($(this).is(":checked")){
        $(this).prop("checked", false);
      }
    })
    un_highlight_selected_item(item);

    // update selected node list array
    for (var i = selected_node_list.length - 1; i >= 0; i--) {
      if(selected_node_list[i] == item){
        selected_node_list.splice(i, 1);
        return;
      }
    }
  }

  function fill_node_list(type, data){
  	// $("#all-node-list").empty();
    var ol = $("#"+type+" ol");
    ol.empty();
    console.log(ol);
   for (var i = 0; i <= data.length - 1; i++) {
      var item = data[i].data;
      var li = "<li class='all-node-list-item'>"
              + "<input type='checkbox' name='" + item.id + "' value='' "
              + " class='checkbox-item node-item-" + item.id + "'>"
              + "<a href='"+item.link+"'>" + item.title + "</a>"
              + "<p>" + item.abstracts + "</p>"
              + "</li>";
      // console.log(li);
      ol.append(li);
    }
    
    ol.on('change', '.checkbox-item', function(){
      console.log($(this).is(":checked"));
      if($(this).is(":checked")){
        // add to selected list
        add_to_selected_list($(this).attr("name"));
        console.log(selected_node_list);
      }else{
        // remove from selected list
        remove_from_selected_list($(this).attr("name"));
        console.log(selected_node_list);
      }
      return false;
      });
    var obj= type
    setPage(obj,data.length,1);
  };
	
  // fill_node_list('all', d);
  // fill_node_list('gene', d2);

});

</script>

			<style>
.ctrl-item {
	margin-top: 10px;
}

#bar-chart svg { /*height: 300px*/
	
}

.bar rect {
	fill: steelblue;
	shape-rendering: crispEdges;
}

.bar text {
	fill: #000;
}

.axis path,.axis line {
	fill: none;
	stroke: #000;
	shape-rendering: crispEdges;
}

.item {
	margin-top: 25px;
}

#cy {
	height: 800px;
	width: 100%;
	position: absolute;
	left: 0;
	top: 100px;
}

#f {
	position: absolute;
	top: 80px;
	/*width: 100%;*/
	height: 20px;
}

#annotation {
	position: absolute;
	right: 0;
}

.ann-item {
	z-index: 1000;
	width: 360px;
	word-wrap: normal;
	overflow-x: hidden;
	/*overflow-y: scroll;*/
	background-color: rgba(250, 250, 250, .85);
	padding: 20px;
	border: 1px solid #ccc;
	display: none;
}

.ui-cytoscape-panzoom {
	left: 10px;
	top: 60px;
}

.control-area {
	position: fixed;
	left: 0;
	z-index: 1000;
	width: 350px;
	word-wrap: normal;
	overflow-x: hidden;
	/*overflow-y: scroll;*/
	background-color: rgba(250, 250, 250, .8);
	padding: 20px;
	padding-bottom: 5px;
	border: 1px solid #ccc;
}

.ctrl-item .form-group { /*margin-top: 10px;*/
	margin-bottom: 5px !important;
}

.slider.slider-horizontal .slider-track { /*left: 10px !important;*/
	
}

#up-down,#down-up {
	width: 50px;
	margin: 0 auto;
}

.up-down-icon,.down-up-icon {
	font-size: 30px;
	color: #ccc;
}

.up-down-icon:hover,.down-up-icon:hover {
	color: #909090;
	cursor: pointer;
}

#gene { /*overflow-y:hidden;*/
	
}

.empty-text {
	position: absolute;
	top: 0px;
	right: 30px;
}

#neighborhood-list-text {
	width: 100%;
}

#nodeinfo-list-text {
	width: 100%;
}

#buttons-ctrl {
	border-bottom: 1px #ccc dashed;
	padding-bottom: 5px;
	margin-top: 0;
	margin-bottom: 5px;
}

.bar-container {
	bottom: 0px;
	border-bottom: none !important;
}

#mask {
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

			<div id="f"></div>

			<div id="cy">
        <div id="mask"></div>   
      </div>


			<input type="hidden" value="<s:property value='query2'/>"
				id="queryString">

			<div id="annotation">

				

				<div class="ann-item" id="ann-0">
					<button type="button" class="close-ann close pull-right">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="ann-title">
						<small></small>
					</h3>
					<p class="desc"></p>
					<table>
						<tr>
							<th>GO id</th>
							<th>GO name</th>
						</tr>
					</table>
				</div>
				<div class="ann-item" id="ann-1">
					<button type="button" class="close-ann close pull-right">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="ann-title">
						<small></small>
					</h3>
					<p class="desc"></p>
					<table>
						<tr>
							<th>GO id</th>
							<th>GO name</th>
						</tr>
					</table>
				</div>
				<div id="neighborhood-list" class="ann-item">
					<button type="button" class="close-ann close pull-right">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="ann-title">Node Information</h3>
					<textarea style="border:1px #ccc solid;" name=""
						id="nodeinfo-list-text" rows="5"></textarea>
					<h3 class="ann-title">Neighbors</h3>
					<textarea style="border:1px #ccc solid;" name=""
						id="neighborhood-list-text" rows="5"></textarea>

				</div>
				
				<div id="edge-info-box" class="ann-item">
					<button type="button" class="close-ann close pull-right">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h3 class="ann-title">Edge Information</h3>
					<textarea style="border:1px #ccc solid;" name=""
						id="edge-info" rows="5"></textarea>
					

				</div>
				
			</div>





			<a
				style="position:fixed;top:10px;right:10px;font-size:15px;z-index:100000;"
				href="$config.root/result?run_id=$run_id">Back to result page</a>
		</div>
	</div>
</body>
</html>
